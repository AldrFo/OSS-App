package ru.mpei.feature_news

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_mvi.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.android.ext.android.inject
import ru.mpei.feature_news.items.NewsAdapterItem
import ru.mpei.feature_news.mvi.*
import ru.mpei.feature_news.mvi.NewsEvent.Wish

class NewsFragment : BaseFragment<NewsEvent, NewsEffect, NewsState, NewsFeature>() {
    override val initEvent: NewsEvent get() = Wish.System.Init

    private val newsFeatureFactory: NewsFeatureFactory by inject()
    override fun createFeature(): NewsFeature = newsFeatureFactory.create()

    override var layoutId = R.layout.fragment_news

    private lateinit var adapter: BaseAdapter

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        adapter = createAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun render(state: NewsState) {
        adapter.update(state.listOfNews)
    }

    override fun handleEffect(effect: NewsEffect) = when(effect) {
        is NewsEffect.ShowError -> Unit
    }

    private fun createAdapter() = BaseAdapter(
        NewsAdapterItem()
    )
}