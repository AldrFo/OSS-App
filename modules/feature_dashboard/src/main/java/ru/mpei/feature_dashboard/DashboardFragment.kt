package ru.mpei.feature_dashboard
/**
 * Андрей Турлюк
 * А-08-17
 */

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.feature_dashboard.databinding.FragmentDashboardBinding
import ru.mpei.feature_dashboard.items.DashboardEventsAdapterItem
import ru.mpei.feature_dashboard.items.DashboardItem
import ru.mpei.feature_dashboard.items.DashboardItem.Companion.ID_EVENTS_ITEM
import ru.mpei.feature_dashboard.items.DashboardItem.Companion.ID_NEWS_ITEM
import ru.mpei.feature_dashboard.items.DashboardNewsAdapterItem
import ru.mpei.feature_dashboard.items.NewsAdapterItem
import ru.mpei.feature_dashboard.mvi.*
import ru.mpei.feature_dashboard.mvi.DashboardEvent.Wish

// Класс фрагмента ленты новостей и афиши
class DashboardFragment : BaseFragment<DashboardEvent, DashboardEffect, DashboardState, DashboardFeature>() {
    // Событие при инициализации
    override val initEvent: DashboardEvent get() = Wish.System.Init

    // Вспомогательные обекты для создания фичи, доступа к фрагментам, связывания разметки с калассом
    private val dashboardFeatureFactory: DashboardFeatureFactory by inject()
    private val router: Router by inject()
    private val binding by viewBinding(FragmentDashboardBinding::bind)
    override var layoutId = R.layout.fragment_dashboard

    // Позиция карусели списков
    private var listPos: Int = 0

    // Набор адапетров для списков
    private val newsAdapter: BaseAdapter by fastLazy { createAdapter() }
    private val eventsAdapter: BaseAdapter by fastLazy { createAdapter() }
    private val viewPagerAdapter by fastLazy {
        BaseAdapter(
            DashboardEventsAdapterItem(eventsAdapter),
            DashboardNewsAdapterItem(newsAdapter)
        ).apply {
            update(listOf(DashboardItem(id = ID_EVENTS_ITEM), DashboardItem(id = ID_NEWS_ITEM)))
        }
    }

    // Функция создания фичи
    override fun createFeature(): DashboardFeature = dashboardFeatureFactory.create()

    // При внутреннем создании отображения
    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            // связываем адаптеры
            dashboardViewPager.adapter = viewPagerAdapter

            // отслеживание состояния перкключателя списков
            dashboardViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    feature.accept(Wish.OnPageChange(position))
                    listPos = position
                }
            })
            selectorAfisha.setOnClickListener { feature.accept(Wish.OnPageChange(0)) }
            selectorNews.setOnClickListener { feature.accept(Wish.OnPageChange(1)) }

            // определение логики при обновлении списков
            swipeRefresh.setColorSchemeResources(R.color.mpei_blue)
            swipeRefresh.setOnRefreshListener {
                if (listPos == 0) {
                    feature.accept(Wish.GetEvents)
                } else {
                    feature.accept(Wish.GetNews)
                }
            }
        }
    }

    // При пересборке отображения
    override fun render(state: DashboardState) {
        // Обновляем списки для поддержания актуальной информации
        newsAdapter.update(state.newsList)
        eventsAdapter.update(state.eventsList)
        // Обновляем селектор отображаемого списка
        binding.dashboardViewPager.currentItem = state.selectedPage
        renderTabView(state)
    }

    // Отрисовка селектора отображаемого списка
    private fun renderTabView(state: DashboardState) = with(binding) {
        val selectedColor = resources.getColor(R.color.mpei_blue)
        val defaultColor = resources.getColor(R.color.mpei_white)
        if (state.selectedPage == 0) {
            selectorAfisha.backgroundTintList = ColorStateList.valueOf(selectedColor)
            selectorAfisha.setTextColor(defaultColor)
            selectorNews.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            selectorNews.setTextColor(selectedColor)
            emptyDashboardLabel.isVisible = state.eventsList.isEmpty()
            emptyNewsLabel.visibility = View.GONE
        } else {
            selectorAfisha.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            selectorAfisha.setTextColor(selectedColor)
            selectorNews.backgroundTintList = ColorStateList.valueOf(selectedColor)
            selectorNews.setTextColor(defaultColor)
            emptyNewsLabel.isVisible = state.newsList.isEmpty()
            emptyDashboardLabel.visibility = View.GONE
        }
    }

    // Обработка эффектов
    override fun handleEffect(effect: DashboardEffect) = when(effect) {
        is DashboardEffect.NewsListLoaded -> binding.swipeRefresh.isRefreshing = false
        is DashboardEffect.EventsListLoaded -> binding.swipeRefresh.isRefreshing = false
        is DashboardEffect.ShowError -> Toast.makeText(context, "Возникла проблема: "+effect.throwable.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    // Создание адаптеров
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