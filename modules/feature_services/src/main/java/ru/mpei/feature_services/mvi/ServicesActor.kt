package ru.mpei.feature_services.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor

// Обработчик запросов к серверу
class ServicesActor() : Actor<ServicesAction, ServicesEvent> {
    override fun execute(action: ServicesAction): Observable<ServicesEvent> {
        TODO("Not yet implemented")
    }
}