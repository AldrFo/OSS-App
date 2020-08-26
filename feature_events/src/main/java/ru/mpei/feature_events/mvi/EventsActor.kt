package ru.mpei.feature_events.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor

class EventsActor : Actor<EventsAction, EventsEvent> {
    override fun execute(action: EventsAction): Observable<EventsEvent> {
        TODO("Not yet implemented")
    }

}