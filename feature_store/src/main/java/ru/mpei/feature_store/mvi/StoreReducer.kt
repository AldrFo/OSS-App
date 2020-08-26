package ru.mpei.feature_store.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias ProfileResult = Result<StoreState, StoreEffect, StoreAction>

class StoreReducer : BaseReducer<StoreState, StoreEvent, StoreEffect, StoreAction>{
    override fun reduce(event: StoreEvent, state: StoreState):ProfileResult {
        TODO("Not yet implemented")
    }
}