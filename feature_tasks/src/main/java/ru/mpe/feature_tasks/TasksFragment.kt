package ru.mpe.feature_tasks

import android.os.Bundle
import android.view.View
import kekmech.ru.common_mvi.ui.BaseFragment
import ru.mpe.feature_tasks.mvi.TasksEffect
import ru.mpe.feature_tasks.mvi.TasksEvent
import ru.mpe.feature_tasks.mvi.TasksEvent.*
import ru.mpe.feature_tasks.mvi.TasksFeature
import ru.mpe.feature_tasks.mvi.TasksState

class TasksFragment() : BaseFragment<TasksEvent, TasksEffect, TasksState, TasksFeature>(){
    override val initEvent: TasksEvent get() = Wish.System.Init

    override fun createFeature(): TasksFeature {
        TODO("Not yet implemented")
    }

    override var layoutId: Int = R.layout.fragment_tasks

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        //
    }

    override fun render(state: TasksState) {
        //
    }

    override fun handleEffect(effect: TasksEffect) = when(effect) {
        is TasksEffect.ShowError -> Unit
    }
}