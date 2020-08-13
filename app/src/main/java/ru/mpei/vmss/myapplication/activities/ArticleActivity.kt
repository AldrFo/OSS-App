package ru.mpei.vmss.myapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_article.*
import ru.mpei.vmss.myapplication.R

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