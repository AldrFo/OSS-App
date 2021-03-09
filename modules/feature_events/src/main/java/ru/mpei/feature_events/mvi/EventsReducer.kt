package ru.mpei.feature_events.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias EventsResult = Result<EventsState, EventsEffect, EventsAction>

class EventsReducer : BaseReducer<EventsState, EventsEvent, EventsEffect, EventsAction> {

    override fun reduce(event: EventsEvent, state: EventsState): EventsResult {
        TODO("Not yet implemented")
    }
}