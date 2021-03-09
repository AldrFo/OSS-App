package ru.mpei.feature_services.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor

class ServicesActor() : Actor<ServicesAction, ServicesEvent> {
    override fun execute(action: ServicesAction): Observable<ServicesEvent> {
        TODO("Not yet implemented")
    }
}