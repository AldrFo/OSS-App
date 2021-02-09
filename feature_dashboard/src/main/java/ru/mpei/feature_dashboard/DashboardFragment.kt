package ru.mpei.feature_dashboard

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_dashboard.*
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
        ).apply {
            update(listOf(DashboardItem(id = ID_NEWS_ITEM), DashboardItem(id = ID_EVENTS_ITEM)))
        }
    }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        dashboardViewPager.adapter = viewPagerAdapter

        dashboardViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                feature.accept(Wish.OnPageChange(position))
            }
        })
        selector_afisha.setOnClickListener { feature.accept(Wish.OnPageChange(0)) }
        selector_news.setOnClickListener { feature.accept(Wish.OnPageChange(1)) }
    }

    override fun render(state: DashboardState) {
        newsAdapter.update(state.newsList)
        eventsAdapter.update(state.eventsList)
        dashboardViewPager.currentItem = state.selectedPage
        renderTabView(state)
    }

    private fun renderTabView(state: DashboardState) {
        val selectedColor = resources.getColor(R.color.mpei_blue)
        val defaultColor = resources.getColor(R.color.mpei_white)
        if (state.selectedPage == 0) {
            selector_afisha.backgroundTintList = ColorStateList.valueOf(selectedColor)
            selector_afisha.setTextColor(defaultColor)
            selector_news.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            selector_news.setTextColor(selectedColor)
        } else {
            selector_afisha.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            selector_afisha.setTextColor(selectedColor)
            selector_news.backgroundTintList = ColorStateList.valueOf(selectedColor)
            selector_news.setTextColor(defaultColor)
        }
    }

    override fun handleEffect(effect: DashboardEffect) = when(effect) {
        is DashboardEffect.ShowError -> Unit

        is DashboardEffect.ChangeSelector -> {
            if (effect.position == 0) {
                Toast.makeText(context, "0", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, "1", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createAdapter() = BaseAdapter(
        NewsAdapterItem {
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            val fragment = ArticleFragment()
            fragment.arguments = bundle
            router.executeCommand(AddScreenForward { fragment })
        }
    )
}