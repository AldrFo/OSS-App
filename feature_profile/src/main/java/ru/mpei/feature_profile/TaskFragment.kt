package ru.mpei.feature_profile

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kekmech.ru.common_navigation.PopUntil
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_task_ended.*
import kotlinx.android.synthetic.main.fragment_task_ended.balance_ended
import kotlinx.android.synthetic.main.fragment_task_ended.begin_date_ended
import kotlinx.android.synthetic.main.fragment_task_ended.end_date_ended
import kotlinx.android.synthetic.main.fragment_task_ended.fragment_task_toolbar_text
import kotlinx.android.synthetic.main.fragment_task_ended.fragment_task_toolbar
import kotlinx.android.synthetic.main.fragment_task_ended.place_ended
import kotlinx.android.synthetic.main.fragment_task_ended.task_description_ended
import kotlinx.android.synthetic.main.fragment_task_ended.task_name_ended
import kotlinx.android.synthetic.main.fragment_task_in_check.*
import kotlinx.android.synthetic.main.fragment_task_in_process.*
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.TaskItem

class TaskFragment: Fragment() {

    private val router: Router by inject()

    private lateinit var it: TaskItem

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