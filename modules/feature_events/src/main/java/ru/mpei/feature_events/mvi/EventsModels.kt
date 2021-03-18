package ru.mpei.feature_events.mvi

import kekmech.ru.common_mvi.Feature

typealias EventsFeature = Feature<EventsState, EventsEvent, EventsEffect>

data class EventsState(
    val isLoading: Boolean = false,
    val listOfNews: List<Any> = emptyList()
)

sealed class EventsEvent {
    sealed class Wish: EventsEvent(){
        object System{
            object Init : Wish()
        }

        sealed class Events : EventsEvent(){

        }
    }
}

sealed class EventsEffect{
    data class ShowError(val throwable: Throwable) : EventsEffect()
}

sealed class EventsAction{

}