package ru.mpei.feature_dashboard.items

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_adapter.BaseItemBinder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_dashboard.*
import ru.mpei.feature_dashboard.R
import ru.mpei.feature_dashboard.items.DashboardItem.Companion.ID_EVENTS_ITEM
import ru.mpei.feature_dashboard.items.DashboardItem.Companion.ID_NEWS_ITEM

data class DashboardItem(
    val id: String
) {
    companion object {
        const val ID_NEWS_ITEM = "NewsItem"
        const val ID_EVENTS_ITEM = "EventsItem"
    }
}

interface DashboardViewHolder {
    fun update(newAdapter: BaseAdapter)
}

class DashboardViewHolderImpl(
    override val containerView: View
): RecyclerView.ViewHolder(containerView), DashboardViewHolder, LayoutContainer {

    override fun update(newAdapter: BaseAdapter) {
        recyclerView.apply {
            if (layoutManager == null) layoutManager = LinearLayoutManager(context)
            if (adapter == null) adapter = newAdapter
        }
    }
}

class DashboardItemBinder(
    private val adapter: BaseAdapter
) : BaseItemBinder<DashboardViewHolder, DashboardItem>() {

    override fun bind(vh: DashboardViewHolder, model: DashboardItem, position: Int) {
        vh.update(adapter)
    }
}

class DashboardNewsAdapterItem(
    adapter: BaseAdapter
): AdapterItem<DashboardViewHolder, DashboardItem>(
    isType = { it is DashboardItem && it.id == ID_NEWS_ITEM },
    layoutRes = R.layout.item_dashboard,
    itemBinder = DashboardItemBinder(adapter),
    viewHolderGenerator = ::DashboardViewHolderImpl,
    areItemsTheSame = { a, b -> a.id == b.id }
)

class DashboardEventsAdapterItem(
    adapter: BaseAdapter
): AdapterItem<DashboardViewHolder, DashboardItem>(
    isType = { it is DashboardItem && it.id == ID_EVENTS_ITEM },
    layoutRes = R.layout.item_dashboard,
    itemBinder = DashboardItemBinder(adapter),
    viewHolderGenerator = ::DashboardViewHolderImpl,
    areItemsTheSame = { a, b -> a.id == b.id }
)