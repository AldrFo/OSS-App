package ru.mpe.feature_tasks

import android.os.Bundle
import android.view.View
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_tasks.*
import org.koin.android.ext.android.inject
import ru.mpe.feature_tasks.items.TasksAdapterItem
import ru.mpe.feature_tasks.mvi.*
import ru.mpe.feature_tasks.mvi.TasksEvent.*

class TasksFragment() : BaseFragment<TasksEvent, TasksEffect, TasksState, TasksFeature>(){
    override val initEvent: TasksEvent get() = Wish.System.Init

    private val tasksFeatureFactory: TasksFeatureFactory by inject()
    override fun createFeature(): TasksFeature = tasksFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_tasks

    private val tasksAdapter: BaseAdapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        tasksRecyclerView.adapter = tasksAdapter
    }

    override fun render(state: TasksState) {
        tasksAdapter.update(state.ListOfTasks)
    }

    override fun handleEffect(effect: TasksEffect) = when(effect) {
        is TasksEffect.ShowError -> Unit
    }

    private fun createAdapter() = BaseAdapter(
            TasksAdapterItem()
    )
}