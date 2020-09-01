package ru.mpei.feature_dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleHeader.text = arguments?.getString("head")

        articleDate.text = arguments?.getString("date")

        articleText.text = arguments?.getString("content")

        Picasso.get()
                .load(arguments?.getString("imageUrl"))
                .into(articleImage)
    }
}