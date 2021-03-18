package ru.mpei.feature_events.mvi

import kekmech.ru.common_mvi.BaseFeature

class EventsFeatureFactory {

    fun create(): EventsFeature = BaseFeature(
            initialState = EventsState(),
            reducer = EventsReducer(),
            actor = EventsActor()
    ).start()

}