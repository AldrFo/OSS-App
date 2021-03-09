package ru.mpei.feature_tasks

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import kekmech.ru.common_android.close
import kekmech.ru.common_android.closeWithSuccess
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.BackButtonListener
import kotlinx.android.synthetic.main.fragment_task_available.*
import org.koin.android.ext.android.inject
import ru.mpei.domain_tasks.dto.TakeTaskItem
import ru.mpei.domain_tasks.dto.TasksItem
import ru.mpei.feature_tasks.mvi.*
import ru.mpei.feature_tasks.mvi.TasksEvent.Wish

class AvailableTaskFragment : BaseFragment<TasksEvent, TasksEffect, TasksState, TasksFeature>(), BackButtonListener {

    private lateinit var item: TasksItem
    private val mSettings: SharedPreferences by inject()
    private val tasksFeatureFactory: TasksFeatureFactory by inject()

    override val initEvent: TasksEvent get() = Wish.System.InitTask

    override fun createFeature(): TasksFeature = tasksFeatureFactory.createTask()

    override var layoutId: Int = R.layout.fragment_task_available

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedInternal(view, savedInstanceState)
        item = arguments?.get("availableTaskData") as TasksItem

        fragment_task_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        fragment_task_toolbar.setNavigationOnClickListener { close() }

        fragment_task_toolbar_text.text = item.taskName

        task_name_avail.text = item.taskName
        balance_avail.text = item.price
        task_description_avail.text = if (item.taskDescription.isEmpty()) getString(R.string.no_description) else item.taskDescription
        place_avail.text = Html.fromHtml(getString(R.string.place, item.location))
        begin_date_avail.text = Html.fromHtml(getString(R.string.begin_date, item.endDate.substring(0, item.endDate.length - 3)))
        end_date_avail.text = Html.fromHtml(getString(R.string.end_date, item.endDate.substring(0, item.endDate.length - 3)))
        refuse_date_avail.text = Html.fromHtml(getString(R.string.refuse_date, item.refuseInfo.substring(0, item.refuseInfo.length - 3)))

        btn_take_task_avail.setOnClickListener {
            feature.accept(Wish.TakeTask(TakeTaskItem(mSettings.getString("userId", "").toString(), item.id.toString())))
        }
    }

    override fun render(state: TasksState) {
        if (state.isLoading){
            btn_take_task_avail.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.mpei_blue_dark))
        }
    }

    override fun handleEffect(effect: TasksEffect) {
        when (effect){
            is TasksEffect.TaskTaken -> {
                successfulTake()
            }
            is TasksEffect.ShowError -> {
                Toast.makeText(context, "Ошибка на сервере: " + effect.throwable, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun successfulTake(){
        btn_take_task_avail.visibility = View.GONE
        successful_take.visibility = View.VISIBLE
    }

    override fun onBackPressed(): Boolean {
        closeWithSuccess()
        return true
    }

}