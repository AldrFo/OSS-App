package ru.mpei.feature_dashboard.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kotlinx.android.extensions.LayoutContainer
import ru.mpei.domain_news.dto.NewsItem
import ru.mpei.feature_dashboard.R

interface DashboardViewHolder {
}

class DashboardViewHolderImpl(
    override val containerView: View
): RecyclerView.ViewHolder(containerView), DashboardViewHolder, LayoutContainer {

}

class AdapterItemBinder(): BaseItemBinder<DashboardViewHolder, NewsItem>() {

    override fun bind(vh: DashboardViewHolder, model: NewsItem, position: Int) {
    }
}

class DashboardAdapterItem: AdapterItem<DashboardViewHolder, NewsItem>(
        isType = { it is NewsItem },
        layoutRes = R.layout.fragment_list,
        itemBinder = AdapterItemBinder(),
        viewHolderGenerator = ::DashboardViewHolderImpl
)