package ru.mpei.feature_dashboard.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import ru.mpei.domain_news.dto.NewsItem
import ru.mpei.feature_dashboard.R
import ru.mpei.feature_dashboard.databinding.ItemArticleBinding

interface NewsViewHolder {
    fun setDate(chislo: String, month: String, hour: String)
    fun setName(name: String)
    fun setText(text: String)
    fun setOnClickListener(onClick: () -> Unit)
}

class NewsViewHolderImpl(
    containerView: View
) : RecyclerView.ViewHolder(containerView), NewsViewHolder {

    private val binding = ItemArticleBinding.bind(containerView)

    override fun setDate(chislo: String, month: String, hour: String) {
        with(binding) {
            itemArticleDate.text = chislo.trim(' ')
            itemArticleMonth.text = month.trim(' ')
            itemArticleTime.text = hour.trim(' ')
        }
    }
    override fun setText(text: String) {
        binding.itemArticleText.text = text
    }

    override fun setName(name: String) {
        binding.itemArticleName.text = name
    }

    override fun setOnClickListener(onClick: () -> Unit) {
        binding.root.setOnClickListener { onClick() }
    }
}

class NewsItemBinder(
    private val onClick: (NewsItem) -> Unit
) : BaseItemBinder<NewsViewHolder, NewsItem>() {

    override fun bind(vh: NewsViewHolder, model: NewsItem, position: Int) {
        vh.setName(model.name)
        vh.setDate(model.chislo, model.month, model.hour)
        vh.setText(model.describtion)
        vh.setOnClickListener { onClick(model) }
    }
}

class NewsAdapterItem(onClick: (NewsItem) -> Unit) : AdapterItem<NewsViewHolder, NewsItem>(
    isType = { it is NewsItem },
    layoutRes = R.layout.item_article,
    itemBinder = NewsItemBinder(onClick),
    viewHolderGenerator = ::NewsViewHolderImpl
)