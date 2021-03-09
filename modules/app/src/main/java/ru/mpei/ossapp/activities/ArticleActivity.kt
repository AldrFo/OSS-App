package ru.mpei.ossapp.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.squareup.picasso.Picasso
import ru.mpei.ossapp.R
class ArticleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

        val articleToolbar = findViewById<Toolbar>(R.id.articleToolbar)
        val articleHeader = findViewById<TextView>(R.id.articleHeader)
        val articleDate = findViewById<TextView>(R.id.articleDate)
        val articleText = findViewById<TextView>(R.id.articleText)
        val articleImage = findViewById<ImageView>(R.id.articleImage)

        articleToolbar.title = intent.getStringExtra("head")
        articleToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        articleToolbar.setNavigationOnClickListener { finish() }

        articleHeader.text = intent.getStringExtra("head")

        articleDate.text = intent.getStringExtra("date")

        articleText.text = intent.getStringExtra("content")

        Picasso.get()
            .load(intent.getStringExtra("imageUrl"))
            .fit()
            .into(articleImage)
    }
}