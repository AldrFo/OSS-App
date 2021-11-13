package ru.mpei.feature_profile

/**
 * Андрей Турлюк
 * А-08-17
 */

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.PopUntil
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ConfirmRefuseItem
import ru.mpei.domain_profile.dto.TaskItem
import ru.mpei.feature_profile.databinding.FragmentTaskBinding
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.Wish

// Класс фрагмента страницы информации о задании
class TaskFragment : BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>() {

    // Обекты для создания фичи, доступа к вкладкам, сохраненным данным, разметке
    private val profileFeatureFactory: ProfileFeatureFactory by inject()
    private val router: Router by inject()
    private val mSettings: SharedPreferences by inject()
    private val binding by viewBinding(FragmentTaskBinding::bind)
    override var layoutId: Int = R.layout.fragment_task

    // Метод при инициализации фрагмента
    override val initEvent: ProfileEvent = Wish.System.InitTask

    override fun createFeature(): ProfileFeature = profileFeatureFactory.create()

    // Буфферный объект для хранения данных о задании
    private lateinit var task: TaskItem

    // При внутреннем создании отображения
    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        // Получаем данные о задании
        task = arguments?.get("taskData") as TaskItem
        with(binding) {
            // Отселживаем тип выбранного задания
            when (task.status) {
                "ongoing" -> {
                    // Выбор разметки для выполняемого задания
                    processTaskLayout.visibility = View.VISIBLE
                    checkTaskLayout.visibility = View.GONE
                    endedTaskLayout.visibility = View.GONE
                }
                "onchecking", "penalization" -> {
                    // Выбор разметки для заданий на проверке
                    processTaskLayout.visibility = View.GONE
                    checkTaskLayout.visibility = View.VISIBLE
                    endedTaskLayout.visibility = View.GONE
                }
                else -> {
                    // Выбор разметки для выполненных и отклоненных заданий
                    processTaskLayout.visibility = View.GONE
                    checkTaskLayout.visibility = View.GONE
                    endedTaskLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    // Метод, выхываемый при изменении состояния вкладки профиля
    override fun render(state: ProfileState) {
        // Создаем навигацияю возвращения и реализуем возвращение
        binding.fragmentTaskToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentTaskToolbar.setNavigationOnClickListener { router.executeCommand(PopUntil(TasksListFragment::class)) }

        // Выводим имя задания в заголовке
        binding.fragmentTaskToolbarText.text = task.taskName
        // Взависимости от типа задания выбираем выполянемы метод
        if (task.status == "ongoing") {
            initProcessTask()
        } else if (task.status == "onchecking" || task.status == "penilization") {
            initOnCheckTask()
        } else {
            initEndedTask()
        }
    }

    // Обработка эффектов
    override fun handleEffect(effect: ProfileEffect) = when (effect) {
        // Эффект успешного отказа от задания
        is ProfileEffect.RefuseSuccess -> {
            router.executeCommand(PopUntil(TasksListFragment::class))
        }
        // Эффект неудачи при отказе от задания
        is ProfileEffect.RefuseError -> {
            Toast.makeText(context, "Не удалось отказаться от задания - попробуйте еще раз позднее", Toast.LENGTH_SHORT).show()
        }
        else -> {
        }
    }

    // Метод при инициализации выполняемого задания
    private fun initProcessTask() {
        with(binding) {
            // Связываем поля и отображаемые данные
            taskNameProcess.text = task.taskName
            balanceProcess.text = task.price
            taskDescriptionProcess.text = if (task.taskDescription.isEmpty()) getString(R.string.no_description) else task.taskDescription
            placeProcess.text = Html.fromHtml(getString(R.string.place, task.location))
            beginDateProcess.text = Html.fromHtml(getString(R.string.begin_date, task.startDate.substring(0, task.startDate.length - 3)))
            endDateProcess.text = Html.fromHtml(getString(R.string.end_date, task.endDate.substring(0, task.endDate.length - 3)))
            refuseDateProcess.text = Html.fromHtml(getString(R.string.refuse_date, task.refuseInfo.substring(0, task.refuseInfo.length - 3)))
            // Вешаем действия при отправке задания на проверку
            btnSendForCheckProcess.setOnClickListener {
                val fragment = EditReportFragment(task.id, task.taskName, ReportType.NEW)
                router.executeCommand(AddScreenForward { fragment })
            }
            // Вешаем действия при отправке задания без отчета
            btnRefuseTaskProcess.setOnClickListener {
                val body = ConfirmRefuseItem(task_id = task.id, user_id = mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!)
                feature.accept(Wish.RefuseTask(body))
            }
        }
    }

    private fun initOnCheckTask() {
        with(binding) {
            // Связывание полей и отображаемых данных
            taskNameCheck.text = task.taskName
            balanceCheck.text = task.price
            taskDescriptionCheck.text = if (task.taskDescription.isEmpty()) getString(R.string.no_description) else task.taskDescription
            placeCheck.text = Html.fromHtml(getString(R.string.place, task.location))
            beginDateCheck.text = Html.fromHtml(getString(R.string.begin_date, task.startDate.substring(0, task.startDate.length - 3)))
            endDateCheck.text = Html.fromHtml(getString(R.string.end_date, task.endDate.substring(0, task.endDate.length - 3)))
            taskCommentCheck.text = if (task.comment == null || task.comment.isEmpty()) getString(R.string.no_comment) else task.comment
            // Вешаем действия при нажатии на кнопку редактирования отчета
            btnEditCheck.setOnClickListener {
                val fragment = EditReportFragment(task.id, task.taskName, ReportType.EDIT)
                router.executeCommand(AddScreenForward { fragment })
            }
        }
    }

    private fun initEndedTask() {
        with(binding) {
            // Связываем поля и отображаемые данные
            taskNameEnded.text = task.taskName
            balanceEnded.text = task.price
            taskDescriptionEnded.text = if (task.taskDescription.isEmpty()) getString(R.string.no_description) else task.taskDescription
            placeEnded.text = Html.fromHtml(getString(R.string.place, task.location))
            beginDateEnded.text = Html.fromHtml(getString(R.string.begin_date, task.startDate.substring(0, task.startDate.length - 3)))
            endDateEnded.text = Html.fromHtml(getString(R.string.end_date, task.endDate.substring(0, task.endDate.length - 3)))
            taskCommentEnded.text = if (task.comment == null || task.comment.isEmpty()) getString(R.string.no_comment) else task.comment
        }
    }
}