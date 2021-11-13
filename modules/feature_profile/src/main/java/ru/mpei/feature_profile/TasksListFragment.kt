package ru.mpei.feature_profile

/**
 * Андрей Турлюк
 * А-08-17
 */

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ProfileItem
import ru.mpei.feature_profile.databinding.FragmentTasksListBinding
import ru.mpei.feature_profile.items.TaskAdapterItem
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.*

class TasksListFragment(val type: TasksType, private val profileData: ProfileItem) : BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>() {

    // Объект для создания фичи, переключеия между страницами, адаптера для списка и связывания кода и разметки
    private val profileFeatureFactory: ProfileFeatureFactory by inject()
    private val router: Router by inject()
    private val adapter: BaseAdapter by fastLazy { createAdapter() }
    private val binding by viewBinding(FragmentTasksListBinding::bind)

    // переменная для отлеживания вернулись мы из другого фрагмента или нет
    private var fromFragment: Boolean = false

    // Определние имени списка по типу отображаемых заданий
    private val name = when (type) {
        TasksType.PROCESS -> "Выполняемые"
        TasksType.CHECK -> "На проверке"
        TasksType.FINISHED -> "Завершенные"
        TasksType.REFUSED -> "Отклоненные"
    }

    // Намерение, реализуемое при инициализации фрагмента
    override val initEvent: ProfileEvent = Wish.LoadTasks(
        when (type) {
            TasksType.PROCESS -> "taken"
            TasksType.CHECK -> "inCheck"
            TasksType.FINISHED -> "accepted"
            TasksType.REFUSED -> "refused"
        }
    )

    override fun createFeature(): ProfileFeature = profileFeatureFactory.createWithData(profileData)

    override var layoutId: Int = R.layout.fragment_tasks_list

    // При внутреннем создании отображения
    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedInternal(view, savedInstanceState)

        with(binding) {
            // Задаем имя списка
            fragmentTasksToolbarText.text = name

            // Создаем логику навигации назад и задаем ей логику
            fragmentTasksToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            fragmentTasksToolbar.setNavigationOnClickListener { router.executeCommand(ClearBackStack()) }

            // Определяем списку адаптер
            tasksList.adapter = adapter
            tasksList.layoutManager = LinearLayoutManager(requireContext())

            // Определяем логику обновленися страницы
            swipeRefresh.setColorSchemeColors(resources.getColor(R.color.mpei_blue))
            swipeRefresh.setOnRefreshListener {
                feature.accept(Wish.LoadTasks(
                    when (type) {
                        TasksType.PROCESS -> "taken"
                        TasksType.CHECK -> "inCheck"
                        TasksType.FINISHED -> "accepted"
                        TasksType.REFUSED -> "refused"
                    }
                ))
            }
        }
    }

    // Метод, вызываемы при изменения состояния фрагмента
    override fun render(state: ProfileState) {
        // Если мы вернулись из другого фрагмента, то обновляем список для поддержания актульаной информации в списке
        if (fromFragment) {
            fromFragment = false
            feature.accept(Wish.LoadTasks(
                when (type) {
                    TasksType.PROCESS -> "taken"
                    TasksType.CHECK -> "inCheck"
                    TasksType.FINISHED -> "accepted"
                    TasksType.REFUSED -> "refused"
                }
            ))
        }

        // Обновляем список заданий
        adapter.update(state.tasksList)

        // ЛОгика отображения иформации о пустом списке
        if (state.tasksList.isEmpty()) {
            val tType = when (type) {
                TasksType.PROCESS -> "выполняемых"
                TasksType.REFUSED -> "отклоненных"
                TasksType.FINISHED -> "выполненных"
                TasksType.CHECK -> "проверяемых"
            }
            binding.emptyListLabel.text = getString(R.string.empty_tasks_list).format(tType)
            binding.emptyListLabel.visibility = View.VISIBLE
        } else {
            binding.emptyListLabel.visibility = View.GONE
        }

    }

    // Обработка приходящих эффектов
    override fun handleEffect(effect: ProfileEffect) = when (effect) {

        // Эффект после загрузки списка заданий
        is ProfileEffect.TasksLoaded -> {
            binding.swipeRefresh.isRefreshing = false
        }

        // Эффект при ошибки во вемя загрузки списка заданий
        is ProfileEffect.TasksLoadError -> {
            Toast.makeText(context, "Возникла ошибка - попробуйте еще раз позже", Toast.LENGTH_SHORT).show()
        }

        else -> {
        }
    }

    // Создание адаптера для списка
    private fun createAdapter() = BaseAdapter(
        TaskAdapterItem {
            val bundle = Bundle()
            bundle.putSerializable("taskData", it)
            val fragment = TaskFragment()
            fragment.arguments = bundle
            fromFragment = true
            router.executeCommand(AddScreenForward { fragment })
        }
    )

}

