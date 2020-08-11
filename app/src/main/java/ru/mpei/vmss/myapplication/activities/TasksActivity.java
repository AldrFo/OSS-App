package ru.mpei.vmss.myapplication.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ru.mpei.vmss.myapplication.R;
import ru.mpei.vmss.myapplication.pojo.Task;

import static ru.mpei.vmss.myapplication.fragments.User.userId;

public class TasksActivity extends AppCompatActivity {

    private Context context;
    private specialAdapter adapter;
    private List<Task> dataList = new ArrayList<>();
    private int type;
    private ImageView editReportImage;
    private String currentPhotoPath;
    private AlertDialog dialog;
    private ExpandableListView list;

    private final int TAKE_PHOTO = 0;
    private final int CHOOSE_PHOTO = 1;
    private final int TASKS_IN_PROCESS = 0;
    private final int TASKS_IN_CHECK = 1;
    private final int TASKS_FINISHED = 2;
    private final int TASKS_DECLINED = 3;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        context = Objects.requireNonNull(getApplicationContext());
        type = (int) getIntent().getLongExtra("type", 0);

        Toolbar mToolbar = findViewById(R.id.tasksTypeToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        mToolbar.setNavigationOnClickListener(view -> finish());

        list = findViewById(R.id.tasksTypeList);
        list.setOnGroupExpandListener(groupPosition -> {
            for (int g = 0; g < adapter.getGroupCount(); g++) {
                if (g != groupPosition) { list.collapseGroup(g);
                }
            }
        });

        switch(type){

            case TASKS_IN_PROCESS://индекс 0 - кнопка выполняемые задания
                mToolbar.setTitle("ВЫПОЛНЯЕМЫЕ ЗАДАНИЯ");
                break;

            case TASKS_IN_CHECK://индекс 1 - кнопка задания на проверке
                mToolbar.setTitle("ЗАДАНИЯ НА ПРОВЕРКЕ");
                break;

            case TASKS_FINISHED://индекс 2 - кнопка завершенные задания
                mToolbar.setTitle("ЗАВЕРШЕННЫЕ ЗАДАНИЯ");
                break;

            case TASKS_DECLINED://индекс 3 - кнопка отклоненные заданя
                mToolbar.setTitle("ОТКЛОНЕННЫЕ ЗАДАНИЯ");
                break;
        }

        adapter = new specialAdapter(context, dataList, type);
        list.setAdapter(adapter);
        updateList(type);

        SwipeRefreshLayout refresher = findViewById(R.id.tasksTypeRefresher);
        refresher.setColorSchemeColors(context.getColor(R.color.bgBottomNavigation));
        refresher.setOnRefreshListener(() -> {
            updateList(type);
            refresher.setRefreshing(false);
        });

    }

    private void updateList(int type) {
        dataList.clear();

        JSONObject body = new JSONObject();
        try {
            body.put("id", userId);

            switch(type){
                case 1:
                    body.put("type", "inCheck");
                    break;

                case 2:
                    body.put("type", "accepted");
                    break;

                case 3:
                    body.put("type", "refused");
                    break;

                default:
                    body.put("type", "taken");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                context.getString(R.string.getTasksUrl), body, response -> {
            for (int i = 0; i < response.length(); i++){
                JSONObject obj;
                Task line;
                try {
                    obj = response.getJSONObject(i);
                     line = new Task(obj.getString("description_short"),
                            obj.getString("description_full"),
                            obj.getString("place"),
                            obj.getString("reward"),
                            obj.getString("start"),
                            obj.getString("end"),
                            obj.getString("refuse_before"));
                    if (type == 1){
                        if ((obj.getString("status").equals("onchecking"))) {
                            line.setStatus("Проверка выполнения");
                        } else {
                            line.setStatus("Проверка штрафа");
                        }
                        line.setChangeBefore(obj.getString("changeBefore"));
                    }
                    line.setID(obj.getString("id_task"));
                    line.setPenalty(obj.getString("penalty"));
                    dataList.add(line);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        },
                error -> Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    @SuppressLint("SetTextI18n")
    public void showDialog(String type, Task task){
            AlertDialog.Builder builder = new AlertDialog.Builder(TasksActivity.this);
            switch(type){
                case "finish":
                    View finishView = getLayoutInflater().inflate(R.layout.finish_dialog, null);

                    Button finishWithReport = finishView.findViewById(R.id.withReport);
                    finishWithReport.setOnClickListener(v->{
                        JSONObject body = new JSONObject();
                        try {
                            body.put("user_id", userId);
                            body.put("task_id", task.getID());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                                context.getString(R.string.confirmTaskUrl),
                                body,
                                response -> {},
                                error -> {});

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(request);
                        dialog.cancel();
                        closeAll();
                        updateList(TASKS_IN_PROCESS);
                        showDialog("editReport", task);
                    });

                    Button finishNoReport = finishView.findViewById(R.id.noReport);
                    finishNoReport.setOnClickListener(v->{
                        JSONObject body = new JSONObject();
                        try {
                            body.put("user_id", userId);
                            body.put("task_id", task.getID());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                                context.getString(R.string.confirmTaskUrl),
                                body,
                                response -> {},
                                error -> {});

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(request);
                        dialog.cancel();
                        closeAll();
                        updateList(TASKS_IN_PROCESS);
                    });

                    builder.setView(finishView);
                    break;

                case "refuseNormal":
                    View refuseNormalView = getLayoutInflater().inflate(R.layout.decline_normal_dialog, null);

                    Button refuseNormal = refuseNormalView.findViewById(R.id.declineNormalButton);
                    refuseNormal.setOnClickListener(v->{
                        JSONObject body = new JSONObject();
                        try {
                            body.put("user_id", userId);
                            body.put("task_id", task.getID());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                                context.getString(R.string.declineTaskUrl),
                                body,
                                response -> {},
                                error -> {});

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(request);
                        dialog.cancel();
                        closeAll();
                        updateList(TASKS_IN_PROCESS);
                    });

                    builder.setView(refuseNormalView);
                    break;

                case "refusePenalty":
                    View refusePenaltyView = getLayoutInflater().inflate(R.layout.decline_penalty_dialog, null);
                    TextView penalty = refusePenaltyView.findViewById(R.id.declinePenalty);

                    Button refuseWithReason = refusePenaltyView.findViewById(R.id.declinePenaltyReason);
                    refuseWithReason.setOnClickListener(v->{
                        JSONObject body = new JSONObject();
                        try {
                            body.put("user_id", userId);
                            body.put("task_id", task.getID());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                                context.getString(R.string.declineTaskUrl),
                                body,
                                response -> {},
                                error -> {});

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(request);
                        dialog.cancel();
                        closeAll();
                        updateList(TASKS_IN_PROCESS);
                        showDialog("editReport", task);
                    });

                    Button refuseNoReason = refusePenaltyView.findViewById(R.id.declinePenaltyNoReason);
                    refuseNoReason.setOnClickListener(v->{
                        JSONObject body = new JSONObject();
                        try {
                            body.put("user_id", userId);
                            body.put("task_id", task.getID());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                                context.getString(R.string.declineTaskUrl),
                                body,
                                response -> {},
                                error -> {});

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(request);
                        dialog.cancel();
                        closeAll();
                        updateList(TASKS_IN_PROCESS);
                    });

                    penalty.setText("Со счета будет вычтен штраф " + task.getPenalty() + " баллов");
                    builder.setView(refusePenaltyView);
                    break;

                case "editReport":
                    View editView = getLayoutInflater().inflate(R.layout.edit_report_dailog, null);

                    editReportImage = editView.findViewById(R.id.editReportImage);
                    editReportImage.setVisibility(View.GONE);

                    TextView comment = editView.findViewById(R.id.editReportTextBox);

                    Button chooseAttachType = editView.findViewById(R.id.editReportAttachImage);
                    chooseAttachType.setOnClickListener(v->{
                        PopupMenu p = new PopupMenu(TasksActivity.this, chooseAttachType);
                        p.getMenuInflater().inflate(R.menu.choose_image_type_popup, p.getMenu());
                        p.setOnMenuItemClickListener(item -> {
                            if (item.getTitle().equals("Сделать фотографию")){
                                //получение фото через камеру
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                // Ensure that there's a camera activity to handle the intent
                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    // Create the File where the photo should go
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ignored) {
                                    }
                                    // Continue only if the File was successfully created
                                    if (photoFile != null) {
                                        Uri photoURI = FileProvider.getUriForFile(TasksActivity.this,
                                                "ru.mpei.vmss.myapplication.fileprovider",
                                                photoFile);
                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                        startActivityForResult(takePictureIntent, TAKE_PHOTO);
                                    }
                                }
                            } else {
                                //получение фото из галереи
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , CHOOSE_PHOTO);//one can be replaced with any action code
                            }

                            return true;
                        });
                        p.show();
                    });

                    Button editReport = editView.findViewById(R.id.editReportButton);
                    editReport.setOnClickListener(v->{

                        JSONObject body = new JSONObject();
                        try {
                            body.put("user_id", userId);
                            body.put("task_id", task.getID());
                            body.put("comment", comment.getText());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(editReportImage.getDrawable() == null){
                            Toast.makeText(context, "no", Toast.LENGTH_SHORT).show();
                        }

                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                                context.getString(R.string.editReportUrl),
                                body,
                                response -> {},
                                error -> {});

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(request);

                        dialog.cancel();
                    });

                    builder.setView(editView);
                    break;
            }
            dialog = builder.create();
            dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {

            case TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                    editReportImage.setVisibility(View.VISIBLE);
                    editReportImage.setImageURI(Uri.parse(currentPhotoPath));
                }
                break;

            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    editReportImage.setVisibility(View.VISIBLE);
                    editReportImage.setImageURI(selectedImage);
                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void closeAll(){
        for (int g = 0; g < adapter.getGroupCount(); g++) {
             list.collapseGroup(g);
        }
    }


    class specialAdapter extends BaseExpandableListAdapter{

        private List<Task> elements;
        private LayoutInflater inflater;
        private int type;

        public specialAdapter(Context context, List<Task> elements, int type) {
            this.elements = elements;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.type = type;
        }

        @Override
        public int getGroupCount() {
            return elements.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return elements.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null){
                view = inflater.inflate(R.layout.tasks_element, parent, false);
            }

            TextView taskNameText = view.findViewById(R.id.taskNameText);
            taskNameText.setText(getTasksElement(groupPosition).getTaskName());

            TextView taskPriceText = view.findViewById(R.id.taskPriceText);
            taskPriceText.setText(getTasksElement(groupPosition).getPrice() + " б.");

            TextView taskLocationText = view.findViewById(R.id.taskLocationText);
            taskLocationText.setText("Место: " + getTasksElement(groupPosition).getLocation());

            return view;
        }

        private Task getTasksElement(int position) {
            return (Task) getGroup(position);
        }

        @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;

            switch (this.type) {

                case TASKS_IN_PROCESS://задания в процессе
                    if(view == null){
                        view = inflater.inflate(R.layout.taken_tasks_element_child, parent, false);
                    }

                    TextView takenDescription = view.findViewById(R.id.takenDescriptionText);
                    takenDescription.setText(getTasksElement(groupPosition).getTaskDescription());

                    TextView takenBeginTime = view.findViewById(R.id.takenBeginText);
                    takenBeginTime.setText("Начало: " + getTasksElement(groupPosition).getStartDate());

                    TextView takenEndTime = view.findViewById(R.id.takenEndText);
                    takenEndTime.setText("Конец: " + getTasksElement(groupPosition).getEndDate());

                    TextView takenRefuseBefore = view.findViewById(R.id.takenCommentText);
                    takenRefuseBefore.setText("От задания можно отказаться не позднее, чем: " + getTasksElement(groupPosition).getRefuse_info());

                    Button finishBtn = view.findViewById(R.id.takenFinishButton);
                    finishBtn.setOnClickListener(v-> showDialog("finish", getTasksElement(groupPosition)));

                    Button declineBtn = view.findViewById(R.id.takenDeclineButton);
                    declineBtn.setOnClickListener(v->{

                        Date time = null;
                        try {
                            time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(getTasksElement(groupPosition).getRefuse_info());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        assert time != null;
                        if (0 >= (time.getTime() - new Date().getTime() + 30000)){
                            showDialog("refusePenalty", getTasksElement(groupPosition));
                        } else {
                            showDialog("refuseNormal", getTasksElement(groupPosition));
                        }

                    });
                    break;

                case TASKS_IN_CHECK://задания на проверке
                    if(view == null){
                        view = inflater.inflate(R.layout.checking_tasks_element_child, parent, false);
                    }

                    TextView checkingStatus = view.findViewById(R.id.checkingStatusText);
                    checkingStatus.setText("Статус: " + getTasksElement(groupPosition).getStatus());

                    TextView changeTime = view.findViewById(R.id.checkingChangeTimeText);
                    changeTime.setText("Отредактировать отчет можно до: " + getTasksElement(groupPosition).getChangeBefore());

                    Button editReportBtn = view.findViewById(R.id.checkingAgreeButton);
                    editReportBtn.setOnClickListener(v-> showDialog("editReport", getTasksElement(groupPosition)));

                    break;

                default://завершенные и отклоненные задания
                    if(view == null){
                        view = inflater.inflate(R.layout.finished_tasks_element_child, parent, false);
                    }

                    TextView description = view.findViewById(R.id.finishedDescriptionText);
                    description.setText(getTasksElement(groupPosition).getTaskDescription());

                    break;
            }

            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

    }
}