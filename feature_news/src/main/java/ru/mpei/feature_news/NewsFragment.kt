package ru.mpei.feature_news

import android.os.Bundle
import android.view.View
import kekmech.ru.common_mvi.ui.BaseFragment
import ru.mpei.feature_news.mvi.*
import ru.mpei.feature_news.mvi.NewsEvent.Wish

class NewsFragment : BaseFragment<NewsEvent, NewsEffect, NewsState, NewsFeature>() {
    override val initEvent: NewsEvent get() = Wish.System.Init

    override fun createFeature(): NewsFeature = NewsFeatureFactory().create()

    override var layoutId = R.layout.fragment_news

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        //
    }

    override fun render(state: NewsState) {
        //
    }

    override fun handleEffect(effect: NewsEffect) = when(effect) {
        is NewsEffect.ShowError -> Unit
    }
}