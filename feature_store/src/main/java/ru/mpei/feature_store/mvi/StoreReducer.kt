package ru.mpei.feature_store.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias StoreResult = Result<StoreState, StoreEffect, StoreAction>

class StoreReducer : BaseReducer<StoreState, StoreEvent, StoreEffect, StoreAction>{
    override fun reduce(event: StoreEvent, state: StoreState):StoreResult {
        TODO("Not yet implemented")
    }
}