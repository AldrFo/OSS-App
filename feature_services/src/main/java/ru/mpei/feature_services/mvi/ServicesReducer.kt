package ru.mpei.feature_services.mvi

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result

typealias ServicesResult = Result<ServicesState, ServicesEffect, ServicesAction>

class ServicesReducer : BaseReducer<ServicesState, ServicesEvent,ServicesEffect, ServicesAction> {

    override fun reduce(event: ServicesEvent, state: ServicesState): ServicesResult {
        TODO("Not yet implemented")
    }
}