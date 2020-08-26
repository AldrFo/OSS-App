package ru.mpei.feature_store.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor

class StoreActor : Actor<StoreAction, StoreEvent>{
    override fun execute(action: StoreAction): Observable<StoreEvent> {
        TODO("Not yet implemented")
    }
}