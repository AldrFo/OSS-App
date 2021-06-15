package ru.mpei.feature_dashboard.mvi

/**
 * Андрей Турлюк
 * А-08-17
 */

import kekmech.ru.common_mvi.Feature
import ru.mpei.domain_news.dto.NewsItem

typealias DashboardFeature = Feature<DashboardState, DashboardEvent, DashboardEffect>

// Модель состояния
data class DashboardState(
    val isLoading: Boolean = false,
    val newsList: List<NewsItem> = emptyList(),
    val eventsList: List<NewsItem> = emptyList(),
    val selectedPage: Int = 0
)

// Класс намерений
sealed class DashboardEvent {

    sealed class Wish : DashboardEvent() {
        object System {
            // Инициализация
            object Init : Wish()
        }

        // Получение списка событий
        object GetEvents : Wish()
        // Получение списка новостей
        object GetNews : Wish()
        // Переключение списка
        data class OnPageChange(val position: Int): Wish()
    }

    sealed class News : DashboardEvent() {
        // Получили список новостей
        data class NewsListLoaded(val listOfItems: List<NewsItem>) : News()
        // Ошибка получения списка новостей
        data class NewsListLoadError(val throwable: Throwable) : News()

        // Получили список событий
        data class EventsListLoaded(val listOfItems: List<NewsItem>) : News()
        // Ошибка получения списка событий
        data class EventsListLoadError(val throwable: Throwable) : News()
    }
}

// Эффекты
sealed class DashboardEffect {
    // Эффект после загрузки списка новостей
    object NewsListLoaded: DashboardEffect()
    // Эффект после загрузки списка событий
    object EventsListLoaded: DashboardEffect()
    // Эффект отображения ошибки
    data class ShowError(val throwable: Throwable) : DashboardEffect()
}

// Действия
sealed class DashboardAction {
    // Действие получения списка новостей
    object LoadNewsList : DashboardAction()
    // Действие получения списка событий
    object LoadEventsList : DashboardAction()
}