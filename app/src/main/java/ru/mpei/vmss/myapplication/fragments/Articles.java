package ru.mpei.vmss.myapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import ru.mpei.vmss.myapplication.activities.ArticleActivity;
import ru.mpei.vmss.myapplication.adapters.ArticleAdapter;
import ru.mpei.vmss.myapplication.pojo.Article;

public class Articles extends Fragment {

    private Context context;
    private ArticleAdapter adapter = null;
    private List<Article> dataList = new ArrayList<>();
    private String url;
    private String header;
    private String prefix;
    private int type;

    public Articles(){}

    public Articles(int type) {
        this.type = type;
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = Objects.requireNonNull(getActivity()).getApplicationContext();

        if(type == 0){
            this.url = context.getString(R.string.dashboardUrl);
            this.header = context.getString(R.string.dashboard);
            this.prefix = context.getString(R.string.imagesDashboardUrl);
        } else if(type == 1){
            this.url = context.getString(R.string.newsUrl);
            this.header = context.getString(R.string.news);
            this.prefix = context.getString(R.string.imagesNewsUrl);
        }

        View rootView = inflater.inflate(R.layout.fragment_articles, container, false);

        TextView title = rootView.findViewById(R.id.articlesHeader);
        title.setText(header);

        RecyclerView list = rootView.findViewById(R.id.articlesList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        list.setLayoutManager(layoutManager);

        adapter = new ArticleAdapter(getActivity().getApplicationContext(), dataList, article -> {
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra("head", article.getTitle());
            intent.putExtra("date", article.getDate());
            intent.putExtra("content", article.getContent());
            intent.putExtra("imageUrl", article.getImageUrl());
            startActivity(intent);
        });
        list.setAdapter(adapter);
        updateList();

        SwipeRefreshLayout refresher = rootView.findViewById(R.id.articlesRefresher);
        refresher.setColorSchemeColors(context.getColor(R.color.bgBottomNavigation));
        refresher.setOnRefreshListener(() -> {
            updateList();
            refresher.setRefreshing(false);
        });

        return rootView;
    }

    private void updateList(){
        dataList.clear();

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                this.url, response -> {
            for (int i = 0; i < response.length(); i++){
                JSONObject obj;
                try {
                    obj = response.getJSONObject(i);
                    dataList.add(new Article(obj.getString("name"),
                            obj.getString("content"),
                            obj.getString("chislo") + " " + obj.getString("month") + " в " + obj.getString("hour"),
                            null,
                            prefix+obj.getString("image_src")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.setArticles(dataList);
        },
                error -> Toast.makeText(context, "Connection error", Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
