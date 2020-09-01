package ru.mpei.feature_dashboard

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.NavigationHolder
import kekmech.ru.common_navigation.ReplaceScreen
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.android.ext.android.inject
import ru.mpei.feature_dashboard.items.NewsAdapterItem
import ru.mpei.feature_dashboard.mvi.*
import ru.mpei.feature_dashboard.mvi.DashboardEvent.Wish

class DashboardFragment : BaseFragment<DashboardEvent, DashboardEffect, DashboardState, DashboardFeature>() {
    override val initEvent: DashboardEvent get() = Wish.System.Init

    private val router: Router by inject()
    private val dashboardFeatureFactory: DashboardFeatureFactory by inject()
    override fun createFeature(): DashboardFeature = dashboardFeatureFactory.create()

    override var layoutId = R.layout.fragment_list

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