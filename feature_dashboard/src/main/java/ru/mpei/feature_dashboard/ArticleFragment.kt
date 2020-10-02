package ru.mpei.feature_dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_article.*
import ru.mpei.domain_news.dto.NewsItem

class ArticleFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val it: NewsItem = arguments?.get("data") as NewsItem

        articleHeader.text = it.name

        articleDate.text = "${it.hour} ${it.chislo} ${it.month}"

        articleText.text = it.content

        Picasso.get()
                .load(it.imageUrl)
                .into(articleImage)
    }
}