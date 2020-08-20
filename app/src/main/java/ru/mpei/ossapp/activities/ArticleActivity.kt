package ru.mpei.ossapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_article.*
import ru.mpei.ossapp.R

class ArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        articleToolbar.title = intent.getStringExtra("head")
        articleToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        articleToolbar.setNavigationOnClickListener { finish() }

        articleHeader.text = intent.getStringExtra("head")

        articleDate.text = intent.getStringExtra("date")

        articleText.text = intent.getStringExtra("content")

        Glide.with(this@ArticleActivity)
                .load(intent.getStringExtra("imageUrl"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(articleImage)
    }
}