package ru.mpei.feature_news.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kotlinx.android.extensions.LayoutContainer
import ru.mpei.domain_news.dto.NewsItem
import ru.mpei.feature_news.R

interface NewsViewHolder {
    fun setName(name: String)
    fun setDate(chislo: String, month: String, hour: String)
}

class NewsViewHolderImpl(
        override val containerView: View
) : RecyclerView.ViewHolder(containerView), NewsViewHolder, LayoutContainer {

    override fun setDate(chislo: String, month: String, hour: String) {
        // todo
    }

    override fun setName(name: String) {
        containerView.findViewById<TextView>(R.id.articleElementText).setText(name)
    }
}

class NewsItemBinder : BaseItemBinder<NewsViewHolder, NewsItem>() {

    override fun bind(vh: NewsViewHolder, model: NewsItem, position: Int) {
        vh.setDate(model.chislo, model.month, model.hour)
        vh.setName(model.name)
    }
}

class NewsAdapterItem : AdapterItem<NewsViewHolder, NewsItem>(
    isType = { it is NewsItem },
    layoutRes = R.layout.item_article,
    itemBinder = NewsItemBinder(),
    viewHolderGenerator = ::NewsViewHolderImpl
)