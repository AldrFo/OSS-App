package ru.mpei.feature_dashboard

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.ReplaceScreen
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.recyclerView
import kotlinx.android.synthetic.main.item_dashboard.view.*
import org.koin.android.ext.android.inject
import ru.mpei.feature_dashboard.items.DashboardEventsAdapterItem
import ru.mpei.feature_dashboard.items.DashboardItem
import ru.mpei.feature_dashboard.items.DashboardItem.Companion.ID_EVENTS_ITEM
import ru.mpei.feature_dashboard.items.DashboardItem.Companion.ID_NEWS_ITEM
import ru.mpei.feature_dashboard.items.DashboardNewsAdapterItem
import ru.mpei.feature_dashboard.items.NewsAdapterItem
import ru.mpei.feature_dashboard.mvi.*
import ru.mpei.feature_dashboard.mvi.DashboardEvent.Wish

class DashboardFragment : BaseFragment<DashboardEvent, DashboardEffect, DashboardState, DashboardFeature>() {
    override val initEvent: DashboardEvent get() = Wish.System.Init

    private val dashboardFeatureFactory: DashboardFeatureFactory by inject()
    override fun createFeature(): DashboardFeature = dashboardFeatureFactory.create()

    private val router: Router by inject()

    override var layoutId = R.layout.fragment_dashboard

    private val newsAdapter: BaseAdapter by fastLazy { createAdapter() }
    private val eventsAdapter: BaseAdapter by fastLazy { createAdapter() }
    private val viewPagerAdapter by fastLazy {
        BaseAdapter(
            DashboardNewsAdapterItem(newsAdapter),
            DashboardEventsAdapterItem(eventsAdapter)
        )
    }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {

        dashboardViewPager.adapter = viewPagerAdapter

        viewPagerAdapter.update(
            listOf(
                DashboardItem(id = ID_NEWS_ITEM),
                DashboardItem(id = ID_EVENTS_ITEM)
            )
        )
        feature.accept(Wish.GetEvents)
    }

    override fun render(state: DashboardState) {
        newsAdapter.update(state.newsList)
        eventsAdapter.update(state.eventsList)
    }

    override fun handleEffect(effect: DashboardEffect) = when(effect) {
        is DashboardEffect.ShowError -> Unit
    }

    private fun createAdapter() = BaseAdapter(
        NewsAdapterItem {
            val bundle = Bundle()
            bundle.putString("head", it.name)
            bundle.putString("date", it.hour + " " + it.chislo + " " + it.month)
            bundle.putString("content", it.content)
            bundle.putString("imageUrl", it.imageUrl)
            val fragment = ArticleFragment()
            fragment.arguments = bundle
            router.executeCommand( ReplaceScreen { fragment } )
        }
    )
}