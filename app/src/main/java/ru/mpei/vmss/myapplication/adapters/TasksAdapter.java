package ru.mpei.vmss.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ru.mpei.vmss.myapplication.R;
import ru.mpei.vmss.myapplication.pojo.Task;

import static ru.mpei.vmss.myapplication.fragments.User.isAuth;
import static ru.mpei.vmss.myapplication.fragments.User.userId;

public class TasksAdapter extends BaseExpandableListAdapter {

    private List<Task> elements;
    private LayoutInflater inflater;
    private Context context;

    public TasksAdapter(Context context, List<Task> elements, int type) {
        this.elements = elements;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = inflater.inflate(R.layout.available_tasks_element_child, parent, false);
        }

        TextView availableDescription = view.findViewById(R.id.availableDescriptionText);
        availableDescription.setText(getTasksElement(groupPosition).getTaskDescription());

        TextView availableBeginTime = view.findViewById(R.id.availableBeginText);
        availableBeginTime.setText("Начало: " + getTasksElement(groupPosition).getStartDate());

        TextView availableEndTime = view.findViewById(R.id.availableEndText);
        availableEndTime.setText("Конец: " + getTasksElement(groupPosition).getEndDate());

        TextView availableRefuseBefore = view.findViewById(R.id.availableCommentText);
        availableRefuseBefore.setText("От задания можно отказаться не позднее, чем: " + getTasksElement(groupPosition).getRefuse_info());

        view.findViewById(R.id.availableAgreeButton).setOnClickListener(v -> {
            if(isAuth) {
                try {
                    JSONObject body = new JSONObject();
                    body.put("user_id", userId);
                    body.put("task_id", getTasksElement(groupPosition).getID());
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.takeTaskUrl), body,
                            response -> {
                            },
                            error -> {
                            });

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(request);

                    elements.remove(groupPosition);
                    notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Необходимо авторизоваться", Toast.LENGTH_SHORT).show();
            }
        });


       return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
