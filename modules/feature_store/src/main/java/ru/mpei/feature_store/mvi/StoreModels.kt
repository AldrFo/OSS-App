package ru.mpei.feature_store.mvi

import kekmech.ru.common_mvi.Feature

typealias StoreFeature = Feature<StoreState, StoreEvent, StoreEffect>

data class StoreState(
        val isLoading: Boolean = false,
        val ListOfNews: List<Any> = emptyList()
)

sealed class StoreEvent{

    sealed class Wish: StoreEvent(){
        object System{
            object Init: Wish()
        }
    }

    sealed class Store : StoreEvent() {

    }
}

sealed class StoreEffect{
    data class ShowError(val throwable: Throwable): StoreEffect()
}

sealed class StoreAction{
    
}