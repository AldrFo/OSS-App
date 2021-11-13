package ru.mpei.feature_dashboard

/**
 * Андрей Турлюк
 * А-08-17
 */

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.domain_news.dto.NewsItem
import ru.mpei.feature_dashboard.databinding.FragmentArticleBinding

// Класс фрагмента с описанием статьи
class ArticleFragment : Fragment(R.layout.fragment_article) {

    // Объект для обеспечения ехнологии внедрения зависимостей
    private val router: Router by inject()
    // объект для связывания разметки и кода
    private val binding by viewBinding(FragmentArticleBinding::bind)

    // при создании отображения заполняем поля всеми необходимыми данными
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