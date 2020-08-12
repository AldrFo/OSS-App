package ru.mpei.vmss.myapplication.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.mpei.vmss.myapplication.R

class ArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        val mToolbar = findViewById<Toolbar>(R.id.articleToolbar)
        mToolbar.title = intent.getStringExtra("head")
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        mToolbar.setNavigationOnClickListener { view: View? -> finish() }
        var textView = findViewById<TextView>(R.id.articleHeader)
        textView.text = intent.getStringExtra("head")
        textView = findViewById(R.id.articleDate)
        textView.text = intent.getStringExtra("date")
        textView = findViewById(R.id.articleText)
        textView.text = intent.getStringExtra("content")
        val imageView = findViewById<ImageView>(R.id.articleImage)
        Glide.with(this@ArticleActivity)
                .load(intent.getStringExtra("imageUrl"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }
}