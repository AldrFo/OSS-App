package ru.mpei.feature_profile

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

class TaskFragment : BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>() {

    //    private val mSettings: SharedPreferences by inject()
    private val profileFeatureFactory: ProfileFeatureFactory by inject()
    private val router: Router by inject()
    private val mSettings: SharedPreferences by inject()
    private val binding by viewBinding(FragmentTaskBinding::bind)
    override var layoutId: Int = R.layout.fragment_task

    override val initEvent: ProfileEvent = Wish.System.InitTask

    override fun createFeature(): ProfileFeature = profileFeatureFactory.create()

    private lateinit var task: TaskItem

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        task = arguments?.get("taskData") as TaskItem
        with(binding) {
            when (task.status) {
                "ongoing" -> {
                    processTaskLayout.visibility = View.VISIBLE
                    checkTaskLayout.visibility = View.GONE
                    endedTaskLayout.visibility = View.GONE
                }
                "onchecking", "penalization" -> {
                    processTaskLayout.visibility = View.GONE
                    checkTaskLayout.visibility = View.VISIBLE
                    endedTaskLayout.visibility = View.GONE
                }
                else -> {
                    processTaskLayout.visibility = View.GONE
                    checkTaskLayout.visibility = View.GONE
                    endedTaskLayout.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun render(state: ProfileState) {
        binding.fragmentTaskToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentTaskToolbar.setNavigationOnClickListener { router.executeCommand(PopUntil(TasksListFragment::class)) }

        binding.fragmentTaskToolbarText.text = task.taskName
        if (task.status == "ongoing") {
            initProcessTask()
        } else if (task.status == "onchecking" || task.status == "penilization") {
            initOnCheckTask()
        } else {
            initEndedTask()
        }
    }

    override fun handleEffect(effect: ProfileEffect) = when (effect){
        is ProfileEffect.RefuseSuccess -> {
            router.executeCommand( PopUntil(TasksListFragment::class) )
        }
        is ProfileEffect.RefuseError -> {
            Toast.makeText(context, "Не удалось отказаться от задания - попробуйте еще раз позднее", Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    private fun initProcessTask() {
        with(binding) {
            taskNameProcess.text = task.taskName
            balanceProcess.text = task.price
            taskDescriptionProcess.text = if (task.taskDescription.isEmpty()) getString(R.string.no_description) else task.taskDescription
            placeProcess.text = Html.fromHtml(getString(R.string.place, task.location))
            beginDateProcess.text = Html.fromHtml(getString(R.string.begin_date, task.startDate.substring(0, task.startDate.length - 3)))
            endDateProcess.text = Html.fromHtml(getString(R.string.end_date, task.endDate.substring(0, task.endDate.length - 3)))
            refuseDateProcess.text = Html.fromHtml(getString(R.string.refuse_date, task.refuseInfo.substring(0, task.refuseInfo.length - 3)))
            btnSendForCheckProcess.setOnClickListener {
                val fragment = EditReportFragment(task.id, task.taskName, ReportType.NEW)
                router.executeCommand(AddScreenForward { fragment })
            }
            btnRefuseTaskProcess.setOnClickListener {
                val body = ConfirmRefuseItem(task_id = task.id, user_id = mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!)
                feature.accept(Wish.RefuseTask(body))
            }
        }
    }

    private fun initOnCheckTask() {
        with(binding) {
            taskNameCheck.text = task.taskName
            balanceCheck.text = task.price
            taskDescriptionCheck.text = if (task.taskDescription.isEmpty()) getString(R.string.no_description) else task.taskDescription
            placeCheck.text = Html.fromHtml(getString(R.string.place, task.location))
            beginDateCheck.text = Html.fromHtml(getString(R.string.begin_date, task.startDate.substring(0, task.startDate.length - 3)))
            endDateCheck.text = Html.fromHtml(getString(R.string.end_date, task.endDate.substring(0, task.endDate.length - 3)))
            taskCommentCheck.text = task.comment
            btnEditCheck.setOnClickListener {
                val fragment = EditReportFragment(task.id, task.taskName, ReportType.EDIT)
                router.executeCommand(AddScreenForward { fragment })
            }
        }
    }

    private fun initEndedTask() {
        with(binding) {
            taskNameEnded.text = task.taskName
            balanceEnded.text = task.price
            taskDescriptionEnded.text = if (task.taskDescription.isEmpty()) getString(R.string.no_description) else task.taskDescription
            placeEnded.text = Html.fromHtml(getString(R.string.place, task.location))
            beginDateEnded.text = Html.fromHtml(getString(R.string.begin_date, task.startDate.substring(0, task.startDate.length - 3)))
            endDateEnded.text = Html.fromHtml(getString(R.string.end_date, task.endDate.substring(0, task.endDate.length - 3)))
            taskCommentEnded.text = if (task.comment.isEmpty()) getString(R.string.no_comment) else task.comment
        }
    }
}