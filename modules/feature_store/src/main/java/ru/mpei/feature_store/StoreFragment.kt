package ru.mpei.feature_store

import android.os.Bundle
import android.view.View
import kekmech.ru.common_mvi.ui.BaseFragment
import ru.mpei.feature_store.mvi.*
import ru.mpei.feature_store.mvi.StoreEvent.*

class StoreFragment : BaseFragment<StoreEvent, StoreEffect, StoreState, StoreFeature>(){
    override val initEvent: StoreEvent get() = Wish.System.Init

    override fun createFeature(): StoreFeature {
        TODO("Not yet implemented")
    }

    override var layoutId: Int = R.layout.fragment_store

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        //
    }

    override fun render(state: StoreState) {
        //
    }

    override fun handleEffect(effect: StoreEffect) = when(effect) {
        is StoreEffect.ShowError -> Unit
    }
}