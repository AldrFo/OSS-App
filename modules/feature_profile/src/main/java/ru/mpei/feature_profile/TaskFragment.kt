package ru.mpei.feature_profile

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.PopUntil
import kekmech.ru.common_navigation.Router
import kekmech.ru.common_navigation.addScreenForward
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.TaskItem
import ru.mpei.feature_profile.databinding.FragmentTaskEndedBinding
import ru.mpei.feature_profile.databinding.FragmentTaskInCheckBinding
import ru.mpei.feature_profile.databinding.FragmentTaskInProcessBinding
import ru.mpei.feature_profile.databinding.FragmentTaskReportBinding

class TaskFragment: Fragment() {

    private val mSettings: SharedPreferences by inject()
    private val router: Router by inject()

    private lateinit var it: TaskItem
    private val fragment_task_toolbar: Toolbar by fastLazy { requireView().findViewById(R.id.fragment_task_toolbar) }
    private val fragment_task_toolbar_text: TextView by fastLazy { requireView().findViewById(R.id.fragment_task_toolbar_text) }

    private val task_name_process: TextView by fastLazy { requireView().findViewById(R.id.task_name_process) }
    private val balance_process: TextView by fastLazy { requireView().findViewById(R.id.balance_process) }
    private val task_description_process: TextView by fastLazy { requireView().findViewById(R.id.task_description_process) }
    private val place_process: TextView by fastLazy { requireView().findViewById(R.id.place_process) }
    private val begin_date_process: TextView by fastLazy { requireView().findViewById(R.id.begin_date_process) }
    private val end_date_process: TextView by fastLazy { requireView().findViewById(R.id.end_date_process) }
    private val refuse_date_process: TextView by fastLazy { requireView().findViewById(R.id.refuse_date_process) }
    private val btn_send_for_check_process: Button by fastLazy { requireView().findViewById(R.id.btn_send_for_check_process) }

    private val task_name_check: TextView by fastLazy { requireView().findViewById(R.id.task_name_check) }
    private val balance_check: TextView by fastLazy { requireView().findViewById(R.id.balance_check) }
    private val task_description_check: TextView by fastLazy { requireView().findViewById(R.id.task_description_check) }
    private val place_check: TextView by fastLazy { requireView().findViewById(R.id.place_check) }
    private val begin_date_check: TextView by fastLazy { requireView().findViewById(R.id.begin_date_check) }
    private val end_date_check: TextView by fastLazy { requireView().findViewById(R.id.end_date_check) }
    private val task_comment_check: TextView by fastLazy { requireView().findViewById(R.id.task_comment_check) }

    private val task_name_ended: TextView by fastLazy { requireView().findViewById(R.id.task_name_ended) }
    private val balance_ended: TextView by fastLazy { requireView().findViewById(R.id.balance_ended) }
    private val task_description_ended: TextView by fastLazy { requireView().findViewById(R.id.task_description_ended) }
    private val place_ended: TextView by fastLazy { requireView().findViewById(R.id.place_ended) }
    private val begin_date_ended: TextView by fastLazy { requireView().findViewById(R.id.begin_date_ended) }
    private val end_date_ended: TextView by fastLazy { requireView().findViewById(R.id.end_date_ended) }
    private val task_comment_ended: TextView by fastLazy { requireView().findViewById(R.id.task_comment_ended) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        it = arguments?.get("taskData") as TaskItem
        return when (it.status){
            "ongoing" -> inflater.inflate(R.layout.fragment_task_in_process, container, false)
            "onchecking", "penalization" -> inflater.inflate(R.layout.fragment_task_in_check, container, false)
            else  -> inflater.inflate(R.layout.fragment_task_ended, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_task_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        fragment_task_toolbar.setNavigationOnClickListener { router.executeCommand(PopUntil(TasksListFragment::class)) }

        fragment_task_toolbar_text.text = it.taskName

        when (it.status){
            "ongoing" -> initProcessTask()
            "onchecking", "penalization" -> initOnCheckTask()
            else  -> initEndedTask()
        }

    }

    private fun initProcessTask(){
        task_name_process.text = it.taskName
        balance_process.text = it.price
        task_description_process.text = if (it.taskDescription.isEmpty()) getString(R.string.no_description) else it.taskDescription
        place_process.text = Html.fromHtml(getString(R.string.place, it.location))
        begin_date_process.text = Html.fromHtml(getString(R.string.begin_date, it.startDate.substring(0, it.startDate.length-3)))
        end_date_process.text = Html.fromHtml(getString(R.string.end_date, it.endDate.substring(0, it.endDate.length-3)))
        refuse_date_process.text = Html.fromHtml(getString(R.string.refuse_date, it.refuseInfo.substring(0, it.refuseInfo.length-3)))
        btn_send_for_check_process.setOnClickListener {
            val fragment = EditReportFragment(this.it, mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!)
            router.executeCommand( AddScreenForward { fragment } )
        }
    }

    private fun initOnCheckTask(){
        task_name_check.text = it.taskName
        balance_check.text = it.price
        task_description_check.text = if (it.taskDescription.isEmpty()) getString(R.string.no_description) else it.taskDescription
        place_check.text = Html.fromHtml(getString(R.string.place, it.location))
        begin_date_check.text = Html.fromHtml(getString(R.string.begin_date, it.startDate.substring(0, it.startDate.length-3)))
        end_date_check.text = Html.fromHtml(getString(R.string.end_date, it.endDate.substring(0, it.endDate.length-3)))
        task_comment_check.text = if (it.comment.isEmpty()) getString(R.string.no_comment) else it.comment
    }

    private fun initEndedTask(){
        task_name_ended.text = it.taskName
        balance_ended.text = it.price
        task_description_ended.text = if (it.taskDescription.isEmpty()) getString(R.string.no_description) else it.taskDescription
        place_ended.text = Html.fromHtml(getString(R.string.place, it.location))
        begin_date_ended.text = Html.fromHtml(getString(R.string.begin_date, it.startDate.substring(0, it.startDate.length-3)))
        end_date_ended.text = Html.fromHtml(getString(R.string.end_date, it.endDate.substring(0, it.endDate.length-3)))
        task_comment_ended.text = if (it.comment.isEmpty()) getString(R.string.no_comment) else it.comment
    }
}