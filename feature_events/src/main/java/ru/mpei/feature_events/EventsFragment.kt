package ru.mpei.feature_events

import android.os.Bundle
import android.view.View
import kekmech.ru.common_mvi.ui.BaseFragment
import ru.mpei.feature_events.mvi.*

class EventsFragment: BaseFragment<EventsEvent, EventsEffect, EventsState, EventsFeature>() {

    override val initEvent: EventsEvent get() = EventsEvent.Wish.System.Init

    override fun createFeature(): EventsFeature = EventsFeatureFactory().create()

    override var layoutId = R.layout.fragment_events

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        //
    }

    override fun render(state: EventsState) {
        //
    }

    override fun handleEffect(effect: EventsEffect) = when(effect) {
        is EventsEffect.ShowError -> Unit
    }
}