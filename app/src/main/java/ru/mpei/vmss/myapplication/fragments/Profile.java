package ru.mpei.vmss.myapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import ru.mpei.vmss.myapplication.R;
import ru.mpei.vmss.myapplication.activities.MainActivity;
import ru.mpei.vmss.myapplication.activities.TasksActivity;

import static ru.mpei.vmss.myapplication.activities.MainActivity.deleteData;
import static ru.mpei.vmss.myapplication.fragments.User.updateLayout;

public class Profile extends Fragment {

    private String hashPass;
    private String userId;
    private String userName;
    private String userSurname;
    private String userCapital;
    private Context context;

    public Profile(){}

    public Profile(String pass, String id) {
        hashPass = pass;
        userId = id;
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = Objects.requireNonNull(getActivity()).getApplicationContext();

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView name = rootView.findViewById(R.id.profileName);
        TextView capital = rootView.findViewById(R.id.profileCoins);

        getUserData(name, capital);

        rootView.findViewById(R.id.profileExitButton).setOnClickListener(v->{
            MainActivity.MyAdapter.tasksFragment.updateList();
            deleteData();
            updateLayout();
        });

        rootView.findViewById(R.id.profileShopButton).setOnClickListener(v->{
            Intent browserIntent = new
                    Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.shopUrl)));
            startActivity(browserIntent);
        });

        ListView list = rootView.findViewById(R.id.profileTaskTypeList);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(context, TasksActivity.class);
            intent.putExtra("type", id);
            startActivity(intent);
        });

        return rootView;
    }

    private void getUserData(TextView name, TextView capital){

        try {
            JSONObject body = new JSONObject();
            body.put("id", userId);
            body.put("pass", hashPass);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.lkUrl), body,
                    response -> {
                        boolean result = response.optBoolean("error");
                        if (!result){
                            userName = response.optString("name");
                            userSurname = response.optString("surname");
                            userCapital = response.optString("capital");
                            updateInfo(name, capital);
                        } else {
                            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(getContext(), "Возникла проблема, попробуйте позже", Toast.LENGTH_SHORT).show());

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("SetTextI18n")
    private void updateInfo(TextView name, TextView capital){
        name.setText(userName + " " + userSurname);
        capital.setText("Ваш баланс: " + userCapital + " р.");
    }
}
