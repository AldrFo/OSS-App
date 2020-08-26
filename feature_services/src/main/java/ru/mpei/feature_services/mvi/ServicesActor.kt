package ru.mpei.feature_services.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor

class ServicesActor : Actor<ServicesActor, ServicesEvent> {
    override fun execute(action: ServicesActor): Observable<ServicesEvent> {
        TODO("Not yet implemented")
    }
}