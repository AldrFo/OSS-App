package ru.mpei.feature_tasks

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_tasks.*
import org.koin.android.ext.android.inject
import ru.mpei.feature_tasks.items.TasksAdapterItem
import ru.mpei.feature_tasks.mvi.*
import ru.mpei.feature_tasks.mvi.TasksEvent.Wish

class TasksFragment : BaseFragment<TasksEvent, TasksEffect, TasksState, TasksFeature>(){
    override val initEvent: TasksEvent get() = Wish.System.Init

    private val tasksFeatureFactory: TasksFeatureFactory by inject()
    private val router: Router by inject()

    override fun createFeature(): TasksFeature = tasksFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_tasks

    private val tasksAdapter: BaseAdapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        tasks_list.adapter = tasksAdapter
        tasks_list.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun render(state: TasksState) {
        tasksAdapter.update(state.ListOfTasks)
        if (state.ListOfTasks.isEmpty()) {
            avail_tasks_label.visibility = View.VISIBLE
        } else {
            avail_tasks_label.visibility = View.GONE
        }
    }

    override fun handleEffect(effect: TasksEffect) = when(effect) {
        is TasksEffect.ShowError -> Unit
    }

    private fun createAdapter() = BaseAdapter(
        TasksAdapterItem{
            val bundle = Bundle()
            bundle.putSerializable("availableTaskData", it)
            val fragment = AvailableTaskFragment()
            fragment.arguments = bundle
            router.executeCommand(AddScreenForward { fragment })
        }
    )
}