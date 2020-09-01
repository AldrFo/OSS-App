package ru.mpei.feature_dashboard

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_mvi.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.android.ext.android.inject
import ru.mpei.feature_dashboard.items.NewsAdapterItem
import ru.mpei.feature_dashboard.mvi.*
import ru.mpei.feature_dashboard.mvi.DashboardEvent.Wish

class DashboardFragment : BaseFragment<DashboardEvent, DashboardEffect, DashboardState, DashboardFeature>() {
    override val initEvent: DashboardEvent get() = Wish.System.Init

    private val dashboardFeatureFactory: DashboardFeatureFactory by inject()
    override fun createFeature(): DashboardFeature = dashboardFeatureFactory.create()

    override var layoutId = R.layout.fragment_news

    private lateinit var adapter: BaseAdapter

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        adapter = createAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun render(state: DashboardState) {
        adapter.update(state.listOfItems)
    }

    override fun handleEffect(effect: DashboardEffect) = when(effect) {
        is DashboardEffect.ShowError -> Unit
    }

    private fun createAdapter() = BaseAdapter(
        NewsAdapterItem { newsItem ->
            println("You clicked on $newsItem")
        }
    )
}