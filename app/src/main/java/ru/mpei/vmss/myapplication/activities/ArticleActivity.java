package ru.mpei.vmss.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import ru.mpei.vmss.myapplication.R;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar mToolbar = findViewById(R.id.articleToolbar);
        mToolbar.setTitle(getIntent().getStringExtra("head"));
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        mToolbar.setNavigationOnClickListener(view -> finish());

        TextView textView = this.findViewById(R.id.articleHeader);
        textView.setText(getIntent().getStringExtra("head"));

        textView = this.findViewById(R.id.articleDate);
        textView.setText(getIntent().getStringExtra("date"));

        textView = this.findViewById(R.id.articleText);
        textView.setText(getIntent().getStringExtra("content"));

        ImageView imageView = this.findViewById(R.id.articleImage);
        Glide.with(ArticleActivity.this)
                .load(getIntent().getStringExtra("imageUrl"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}