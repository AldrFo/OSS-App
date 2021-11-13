package ru.mpei.feature_services

/**
 * Андрей Турлюк
 * А-08-17
 */

import android.os.Bundle
import android.view.View
import kekmech.ru.common_mvi.ui.BaseFragment
import org.koin.android.ext.android.inject
import ru.mpei.feature_services.mvi.*
import ru.mpei.feature_services.mvi.ServicesEvent.Wish

// Фрагмент сервисов - не реализован потому что заказчик не определился с наполнением
class  ServicesFragment: BaseFragment<ServicesEvent, ServicesEffect, ServicesState, ServicesFeature>(){
    override val initEvent: ServicesEvent get() = Wish.System.Init

    private  val servicesFeatureFactory: ServicesFeatureFactory by inject()

    override fun createFeature(): ServicesFeature = servicesFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_services

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        //
    }

    override fun render(state:ServicesState) {
        //
    }

    override fun handleEffect(effect: ServicesEffect) = when(effect) {
        is ServicesEffect.ShowError -> Unit
    }
}