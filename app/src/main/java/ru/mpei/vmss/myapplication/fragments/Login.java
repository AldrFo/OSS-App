package ru.mpei.vmss.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static ru.mpei.vmss.myapplication.activities.MainActivity.hideKeyboard;
import static ru.mpei.vmss.myapplication.fragments.User.isAuth;

public class Login extends Fragment {

    public Login() {
    }

    private Context context;
    private AppCompatEditText email;
    private AppCompatEditText password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = Objects.requireNonNull(getActivity()).getApplicationContext();

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        email = rootView.findViewById(R.id.loginEmail);
        password = rootView.findViewById(R.id.loginPassword);

        rootView.findViewById(R.id.enterButton).setOnClickListener(v -> {
            if(Objects.requireNonNull(email.getText()).toString().trim().length() == 0 || Objects.requireNonNull(password.getText()).toString().trim().length() == 0){
                Toast.makeText(getContext(), "Проверьте заполнение полей", Toast.LENGTH_LONG).show();
                return;
            }


            try {
            JSONObject body = new JSONObject();
            body.put("email", email.getText().toString());
            body.put("password", password.getText().toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.authUrl), body,
                        response -> {
                    boolean result = response.optBoolean("error");
                    if (!result){
                        MainActivity.MyAdapter.tasksFragment.updateList();
                        isAuth(true, response.optString("pass"), response.optString("id"));
                        hideKeyboard(getActivity());
                    } else {
                        Toast.makeText(getContext(), response.optString("key"), Toast.LENGTH_SHORT).show();
                    }
                },
                        error -> Toast.makeText(getContext(), "Возникла проблема, попробуйте позже", Toast.LENGTH_SHORT).show());

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(request);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        rootView.findViewById(R.id.registerButton).setOnClickListener(v -> {
            Intent browserIntent = new
                    Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.regUrl)));
            startActivity(browserIntent);
        });

        return rootView;
    }
}
