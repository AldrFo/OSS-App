package ru.mpei.feature_tasks

/**
 * Андрей Турлюк
 * А-08-17
 */
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.ActivityResultListener
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.feature_tasks.databinding.FragmentTasksBinding
import ru.mpei.feature_tasks.items.TasksAdapterItem
import ru.mpei.feature_tasks.mvi.*
import ru.mpei.feature_tasks.mvi.TasksEvent.Wish

// Фрагмент спика заданий
class TasksFragment : BaseFragment<TasksEvent, TasksEffect, TasksState, TasksFeature>(), ActivityResultListener {

    // Объекты для создания фичи, перключения между страницами,
    // доступа к сохраненным данным и связывания кода и разметки
    private val tasksFeatureFactory: TasksFeatureFactory by inject()
    private val router: Router by inject()
    private val mSettings: SharedPreferences by inject()
    private val binding by viewBinding(FragmentTasksBinding::bind)

    // Отслеживание возвращения из фрагмента
    private var fromFragment: Boolean = false

    // Создание фичи
    override fun createFeature(): TasksFeature = tasksFeatureFactory.create()

    // Намерение при инициализации фрагмента
    override val initEvent: TasksEvent
        get() = Wish.System.Init(mSettings.getString(APP_PREFERENCES_ID, "0").toString())

    override var layoutId: Int = R.layout.fragment_tasks

    // Ажаптер списка заданий
    private val tasksAdapter: BaseAdapter by fastLazy { createAdapter() }

    // После внутреннего создания отобажения
    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            // Привязывание адаптер к списку
            tasksList.adapter = tasksAdapter
            tasksList.layoutManager = LinearLayoutManager(requireContext())

            // Логика обновления списка заданий
            swipeRefresh.setColorSchemeResources(R.color.mpei_blue)
            swipeRefresh.setOnRefreshListener {
                feature.accept(initEvent)
            }
        }
    }

    // Метод, вызываемый при обновлении состяния фичи
    override fun render(state: TasksState) {
        // Если мы вернулись из другого фрагмента,
        // то обновляем список для поддержания актуальной информации
        if (fromFragment) {
            fromFragment = false
            feature.accept(initEvent)
        }

        // Обновляем список доступных заданий
        tasksAdapter.update(state.ListOfTasks)
        if (state.ListOfTasks.isEmpty()) {
            binding.availTasksLabel.visibility = View.VISIBLE
        } else {
            binding.availTasksLabel.visibility = View.GONE
        }
    }

    // Обработка эффектов
    override fun handleEffect(effect: TasksEffect) = when (effect) {
        // Эффект загрузки заданий
        is TasksEffect.TasksLoaded -> {
            binding.swipeRefresh.isRefreshing = false
        }

        // Эффект отображения ошибки
        is TasksEffect.ShowError -> Unit
        else -> Unit
    }

    // Создание адаптера списка
    private fun createAdapter() = BaseAdapter(
        TasksAdapterItem {
            val bundle = Bundle()
            bundle.putSerializable("availableTaskData", it)
            val fragment = AvailableTaskFragment()
            fragment.arguments = bundle
            fromFragment = true
            router.executeCommand(AddScreenForward { fragment })
        }
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 404) {
            feature.accept(Wish.System.Init(mSettings.getString(APP_PREFERENCES_ID, "0").toString()))
        }
    }

    companion object {
        const val APP_PREFERENCES_FLAG = "isAuth"
        const val APP_PREFERENCES_PASS = "userPass"
        const val APP_PREFERENCES_ID = "userId"

        const val AUTHORIZATION_ERROR = 0
        const val AUTHENTICATION_ERROR = 1
    }

}