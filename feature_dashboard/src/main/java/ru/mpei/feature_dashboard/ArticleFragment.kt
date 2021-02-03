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

        fragment_article_toolbar.title = it.name
        fragment_article_toolbar.setNavigationIcon(R.drawable.ic_arrow_back_mpei_blue_24dp)

        fragment_article_date.text = it.chislo
        fragment_article_month.text = it.month
        fragment_article_time.text = it.hour

        fragment_article_name.text = it.name
        fragment_article_description.text = it.describtion

        fragment_article_text.text = it.content

        Picasso.get()
                .load(it.imageUrl)
                .into(fragment_article_image)
    }
}