package ru.mpei.feature_profile.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */
import kekmech.ru.common_mvi.Feature
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import ru.mpei.domain_profile.dto.*

typealias ProfileFeature = Feature<ProfileState, ProfileEvent, ProfileEffect>

enum class TasksType {
    PROCESS, CHECK, REFUSED, FINISHED
}

enum class ReportType {
    NEW, EDIT
}

// Состояние фичи
data class ProfileState(
    val isLoading: Boolean = false,
    val profileData: ProfileItem = ProfileItem(),
    val isAuthorized: Boolean = false,
    val paramsItem: ParamsItem = ParamsItem(),
    val tasksList: List<TaskItem> = emptyList(),
    val takePhoto: Boolean = false,
    val selectedShopPage: Int = 0,
    val shopPopularProductsList: List<ProductItem> = emptyList(),
    val shopAllProductsList: List<ProductItem> = emptyList()
)

sealed class ProfileEvent{

    sealed class Wish: ProfileEvent(){
        // Системные намерения
        object System{
            object InitLogin: Wish()
            object InitReport: Wish()
            object InitTask: Wish()
            object InitShop: Wish()
        }

        // Намерение авторизации
        data class Authorization(val id: String, val pass: String): Wish()

        // Намерение аутентификации
        object Authentication: Wish()

        // Намерение валидации полей
        data class ValidateFields(val email:String, val pass: String): Wish()
        // Намерение неудачной валидации полей
        object ValidationFailed: Wish()
        object Exit: Wish()

        // Намерение выхода
        data class LoadTasks(val type: String): Wish()

        // Намерение подтверждения задания
        data class ConfirmTask(val body: ConfirmRefuseItem): Wish()
        // Намерение отправки отчета
        data class SendReport(val body: ReportItem, val imageBody: MultipartBody.Part?): Wish()

        // Намерение отказа от задания
        data class RefuseTask(val body: ConfirmRefuseItem): Wish()

        // Намерение отправки фото
        object AddPhoto: Wish()

        // Намренеи открытия магазина
        object OpenShop: Wish()
        // Намернеи переключения списка внутри магазина
        data class OnShopPageChange(val position: Int): Wish()

        // Намерение загрузки всез товаров
        object LoadAllProducts: Wish()
        // Намерение загрузки популярных товаров
        object LoadPopularProducts: Wish()
    }

    sealed class News : ProfileEvent() {

        // Событие авторизации
        data class Authorized(val profile: ProfileItem): News()
        // СОбытие неудачной авторизации
        data class AuthorizationFailed(val throwable: Throwable): News()

        // Событие аутентификации
        data class Authenticated(val paramsItem: ParamsItem): News()
        // СОбытие неудачной аутентификации
        data class AuthenticationFailed(val throwable: Throwable): News()

        // Событие загрузки заданий
        data class TasksLoaded(val tasksList: List<TaskItem>): News()
        // Событие ошибки загрузки заданий
        data class TasksLoadFailed( val throwable: Throwable): News()

        // Событие подтвеждения задания
        data class TaskConfirmed(val obj: ResponseBody): News()
        // Событие ощибки подтверждения задания
        data class TaskConfirmError(val throwable: Throwable): News()

        // Событие отправки отчета
        data class ReportSent(val obj: ResponseBody): News()
        // Событие ошибки отправки отчета
        data class ReportSendError(val throwable: Throwable): News()

        // Событие отказа от задания
        data class TaskRefused(val obj: ResponseBody): News()
        // Событие ошибки при отказе от задания
        data class TaskRefuseError(val throwable: Throwable): News()

        // Событие загрузки списка всех товаров
        data class AllProductsLoaded(val allProducts: List<ProductItem>): News()
        // Событие ошибки при загрузке всех товаров
        data class AllProductsLoadError(val throwable: Throwable): News()

        // Событие загрузки списка популярных товаров
        data class PopularProductsLoaded(val popularProducts: List<ProductItem>): News()
        // Событие ошибки при загрузке списка популярных товаров
        data class PopularProductsLoadError(val throwable: Throwable): News()
    }

}

sealed class ProfileEffect{
    // Эффект ошибки при авториации
    data class AuthorizationError(val throwable: Throwable): ProfileEffect()
    // Эффек ошибки при аутентификации
    data class AuthenticationError(val throwable: Throwable): ProfileEffect()

    // Эффект сохранение данных пользователя
    data class SaveParams(val paramsItem: ParamsItem): ProfileEffect()
    // Эффект удаления данных пользователя
    object ClearParams: ProfileEffect()

    // Эффект валидации
    data class Validate(val email: String, val pass: String): ProfileEffect()

    // Эффект загрузки списка заданий
    object TasksLoaded: ProfileEffect()
    // Эффект ошибки при загрузке списка заданий
    data class TasksLoadError(val throwable: Throwable): ProfileEffect()

    // Эффект успешного подтвреждения задания
    object ConfirmSuccess: ProfileEffect()
    // Эффект неудачи при подтверждении задания
    data class ConfirmError(val throwable: Throwable): ProfileEffect()

    // Эффект при отправке отчета
    object ReportSendSuccess: ProfileEffect()
    // Эффект при ошибке при отправке отчета
    data class ReportSendError(val throwable: Throwable): ProfileEffect()

    // Эффект при отказе от задания
    object RefuseSuccess: ProfileEffect()
    // Эффект при ошибке при отказе от задания
    data class RefuseError(val throwable: Throwable): ProfileEffect()

    // Эффект добавления отчета
    object AddPhoto: ProfileEffect()

    // Эффект открытия магазина
    object OpenShop: ProfileEffect()

    // Эффект при загрузке списка всех заданий
    object AllProductsLoaded: ProfileEffect()
    // Эффект при загрузке списка популярных заданий
    object PopularProductsLoaded: ProfileEffect()
    // Эффект отображения ошибки
    data class ShowError(val throwable: Throwable): ProfileEffect()
}

sealed class ProfileAction {
    // Дейтвие при авторизации
    data class Authorize(val id: String, val pass: String): ProfileAction()
    // Дейтвие при аутентификации
    data class Authenticate(val email: String, val pass: String): ProfileAction()
    // Дейтвие при загрузке списка заданий
    data class LoadTasks(val type: String,  val id: String): ProfileAction()
    // Дейтвие при родтверждении задания
    data class ConfirmTask(val body: ConfirmRefuseItem): ProfileAction()
    // Дейтвие при отправке отчета
    data class SendReport(val body: ReportItem, val imageBody: MultipartBody.Part?): ProfileAction()
    // Дейтвие при отказе от задания
    data class RefuseTask(val body: ConfirmRefuseItem): ProfileAction()
    // Дейтвие при загрузке спика популярных заданий
    object LoadPopularProducts: ProfileAction()
    // Дейтвие при загрузке списка всез заданий
    object LoadAllProducts: ProfileAction()
}