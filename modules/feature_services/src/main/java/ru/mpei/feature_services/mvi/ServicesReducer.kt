package ru.mpei.feature_services.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias ServicesResult = Result<ServicesState, ServicesEffect, ServicesAction>

// Обработчик собтыий
class ServicesReducer : BaseReducer<ServicesState, ServicesEvent,ServicesEffect, ServicesAction> {

    override fun reduce(event: ServicesEvent, state: ServicesState): ServicesResult {
        return ServicesResult(
            state = state.copy()
        )
    }
}