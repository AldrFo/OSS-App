package ru.mpei.feature_dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.domain_news.dto.NewsItem
import ru.mpei.feature_dashboard.databinding.FragmentArticleBinding

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private val router: Router by inject()
    private val binding by viewBinding(FragmentArticleBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val it: NewsItem = arguments?.get("data") as NewsItem

        with(binding) {
            with(fragmentArticleToolbar) {
                setNavigationIcon(R.drawable.ic_arrow_back)
                setNavigationOnClickListener { router.executeCommand(ClearBackStack()) }
            }
            fragmentArticleToolbarText.text = it.name

            fragmentArticleDate.text = it.chislo
            fragmentArticleMonth.text = it.month
            fragmentArticleTime.text = it.hour

            fragmentArticleName.text = it.name
            fragmentArticleDescription.text = it.describtion

            fragmentArticleText.text = it.content

            Picasso.get()
                .load(it.imageUrl)
                .into(fragmentArticleImage)
        }
    }
}