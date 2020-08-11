package ru.mpei.vmss.myapplication.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.mpei.vmss.myapplication.R;
import ru.mpei.vmss.myapplication.adapters.TasksAdapter;
import ru.mpei.vmss.myapplication.pojo.Task;

import static ru.mpei.vmss.myapplication.fragments.User.isAuth;
import static ru.mpei.vmss.myapplication.fragments.User.userId;

public class Tasks extends Fragment {

    private Context context;
    private TasksAdapter adapter;
    private List<Task> dataList = new ArrayList<>();

    public Tasks() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = Objects.requireNonNull(getActivity()).getApplicationContext();

        View rootView = inflater.inflate(R.layout.fragment_tasks, container, false);
        ExpandableListView list = rootView.findViewById(R.id.tasksList);

        adapter = new TasksAdapter(context, dataList, 0);
        list.setAdapter(adapter);
        list.setOnGroupExpandListener(groupPosition -> {
            for (int g = 0; g < adapter.getGroupCount(); g++) {
                if (g != groupPosition) { list.collapseGroup(g);
                }
            }
        });
        updateList();

        SwipeRefreshLayout refresher = rootView.findViewById(R.id.tasksRefresher);
        refresher.setColorSchemeColors(context.getColor(R.color.bgBottomNavigation));
        refresher.setOnRefreshListener(() -> {
            updateList();
            refresher.setRefreshing(false);
        });

        return rootView;
    }

    public void updateList() {
        dataList.clear();

        JSONObject body = new JSONObject();
        try {
            if(isAuth) {
                body.put("id", userId);
            } else {
                body.put("id", 0);
            }
            body.put("type", "available");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST,
                context.getString(R.string.getTasksUrl), body, response -> {
                    for (int i = 0; i < response.length(); i++){
                        JSONObject obj;
                        try {
                            obj = response.getJSONObject(i);
                            dataList.add( new Task(obj.getString("description_short"),
                                    obj.getString("description_full"),
                                    obj.getString("place"),
                                    obj.getString("reward"),
                                    obj.getString("start"),
                                    obj.getString("end"),
                                    obj.getString("refuse_before"),
                                    obj.getString("id")));

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

}
