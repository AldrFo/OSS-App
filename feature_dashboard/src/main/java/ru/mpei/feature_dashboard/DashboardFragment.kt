package ru.mpei.feature_dashboard

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.ReplaceScreen
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.inject
import ru.mpei.feature_dashboard.items.DashboardItem
import ru.mpei.feature_dashboard.items.DashboardItem.Companion.ID_EVENTS_ITEM
import ru.mpei.feature_dashboard.items.DashboardItem.Companion.ID_NEWS_ITEM
import ru.mpei.feature_dashboard.items.DashboardNewsAdapterItem
import ru.mpei.feature_dashboard.items.NewsAdapterItem
import ru.mpei.feature_dashboard.mvi.*
import ru.mpei.feature_dashboard.mvi.DashboardEvent.Wish

class DashboardFragment : BaseFragment<DashboardEvent, DashboardEffect, DashboardState, DashboardFeature>() {
    override val initEvent: DashboardEvent get() = Wish.System.Init

    private val router: Router by inject()
    private val dashboardFeatureFactory: DashboardFeatureFactory by inject()
    override fun createFeature(): DashboardFeature = dashboardFeatureFactory.create()

    override var layoutId = R.layout.fragment_list

    private val newsAdapter: BaseAdapter by fastLazy { createAdapter() }
    private val eventsAdapter: BaseAdapter by fastLazy { createAdapter() }
    private val viewPagerAdapter by fastLazy {
        BaseAdapter(
            DashboardNewsAdapterItem(newsAdapter),
            DashboardNewsAdapterItem(eventsAdapter)
        )
    }
    private lateinit var adapter: BaseAdapter

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        adapter = createAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewPagerAdapter.update(
            listOf(
                DashboardItem(id = ID_NEWS_ITEM),
                DashboardItem(id = ID_EVENTS_ITEM)
            )
        )

        view.setOnClickListener { feature.accept(Wish.OnSwipeRefresh) }
    }

    override fun render(state: DashboardState) {
        adapter.update(state.listOfItems)
        // newsAdapter.update(state.newsList)
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
            bundle.putString("imageUrl", "http://cy37212.tmweb.ru/images/news/" + it.imageUrl)
            val fragment = ArticleFragment()
            fragment.arguments = bundle
            router.executeCommand(ReplaceScreen { fragment } )
        }
    )
}