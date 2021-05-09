package ru.mpei.feature_profile.mvi

import io.reactivex.Observable
import kekmech.ru.common_mvi.Actor
import ru.mpei.domain_profile.ProfileRepository

class ProfileActor(
        private val profileRepository: ProfileRepository,
) : Actor<ProfileAction, ProfileEvent>{
    override fun execute(action: ProfileAction): Observable<ProfileEvent> = when (action){

        is ProfileAction.Authorize -> profileRepository.authorize(action.id, action.pass)
            .mapEvents(ProfileEvent.News::Authorized, ProfileEvent.News::AuthorizationFailed)

        is ProfileAction.Authenticate -> profileRepository.authenticate(action.email, action.pass)
            .mapEvents(ProfileEvent.News::Authenticated, ProfileEvent.News::AuthenticationFailed)

        is ProfileAction.LoadTasks -> profileRepository.loadTasks(action.type, action.id)
            .mapEvents(ProfileEvent.News::TasksLoaded, ProfileEvent.News::TasksLoadFailed)

        is ProfileAction.ConfirmTask -> profileRepository.confirmTask(action.body)
            .mapEvents(ProfileEvent.News::TaskConfirmed, ProfileEvent.News::TaskConfirmError)

        is ProfileAction.SendReport -> profileRepository.sendReport(action.body, action.imageBody)
            .mapEvents(ProfileEvent.News::ReportSent, ProfileEvent.News::ReportSendError)

        is ProfileAction.RefuseTask -> profileRepository.refuseTask(action.body)
            .mapEvents(ProfileEvent.News::TaskRefused, ProfileEvent.News::TaskRefuseError)

        is ProfileAction.LoadAllProducts -> profileRepository.loadAllProducts()
            .mapEvents(ProfileEvent.News::AllProductsLoaded, ProfileEvent.News::AllProductsLoadError)

        is ProfileAction.LoadPopularProducts -> profileRepository.loadPopularProducts()
            .mapEvents(ProfileEvent.News::PopularProductsLoaded, ProfileEvent.News::PopularProductsLoadError)
    }
}