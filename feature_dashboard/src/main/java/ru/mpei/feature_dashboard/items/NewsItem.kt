package ru.mpei.feature_dashboard.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kotlinx.android.extensions.LayoutContainer
import ru.mpei.domain_news.dto.NewsItem
import ru.mpei.feature_dashboard.R

interface NewsViewHolder {
    fun setDate(chislo: String, month: String, hour: String)
    fun setName(name: String)
    fun setText(text: String)
    fun setOnClickListener(onClick: () -> Unit)
}

class NewsViewHolderImpl(
        override val containerView: View
) : RecyclerView.ViewHolder(containerView), NewsViewHolder, LayoutContainer {

    override fun setDate(chislo: String, month: String, hour: String) {
        containerView.findViewById<TextView>(R.id.item_article_date).text = chislo.trim(' ')
        containerView.findViewById<TextView>(R.id.item_article_month).text = month.trim(' ')
        containerView.findViewById<TextView>(R.id.item_article_time).text = hour.trim(' ')
    }
    override fun setText(text: String) {
        containerView.findViewById<TextView>(R.id.item_article_text).text = text
    }

    override fun setName(name: String) {
        containerView.findViewById<TextView>(R.id.item_article_name).text = name
    }

    override fun setOnClickListener(onClick: () -> Unit) {
        containerView.setOnClickListener { onClick() }
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