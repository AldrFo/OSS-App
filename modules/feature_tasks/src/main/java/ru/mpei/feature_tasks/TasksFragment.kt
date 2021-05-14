package ru.mpei.feature_tasks

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.annotation.BoolRes
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

class TasksFragment : BaseFragment<TasksEvent, TasksEffect, TasksState, TasksFeature>(), ActivityResultListener {

    private val tasksFeatureFactory: TasksFeatureFactory by inject()
    private val router: Router by inject()
    private val mSettings: SharedPreferences by inject()
    private val binding by viewBinding(FragmentTasksBinding::bind)

    private var fromFragment: Boolean = false

    override fun createFeature(): TasksFeature = tasksFeatureFactory.create()

    override val initEvent: TasksEvent get() = Wish.System.Init(mSettings.getString(APP_PREFERENCES_ID,"0").toString())

    override var layoutId: Int = R.layout.fragment_tasks

    private val tasksAdapter: BaseAdapter by fastLazy { createAdapter() }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            tasksList.adapter = tasksAdapter
            tasksList.layoutManager = LinearLayoutManager(requireContext())

            swipeRefresh.setColorSchemeResources(R.color.mpei_blue)
            swipeRefresh.setOnRefreshListener {
                feature.accept(initEvent)
            }
        }
    }

    override fun render(state: TasksState) {

        if (fromFragment) {
            fromFragment = false
            feature.accept(initEvent)
        }

        tasksAdapter.update(state.ListOfTasks)
        if (state.ListOfTasks.isEmpty()) {
            binding.availTasksLabel.visibility = View.VISIBLE
        } else {
            binding.availTasksLabel.visibility = View.GONE
        }
    }

    override fun handleEffect(effect: TasksEffect) = when(effect) {
        is TasksEffect.TasksLoaded -> {
            binding.swipeRefresh.isRefreshing = false
        }

        is TasksEffect.ShowError -> Unit
        else -> Unit
    }

    private fun createAdapter() = BaseAdapter(
        TasksAdapterItem{
            val bundle = Bundle()
            bundle.putSerializable("availableTaskData", it)
            val fragment = AvailableTaskFragment()
            fragment.arguments = bundle
            fromFragment = true
            router.executeCommand(AddScreenForward { fragment/*.withResultFor(this, 12892)*/ })
        }
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 404){
            feature.accept(Wish.System.Init(mSettings.getString(APP_PREFERENCES_ID,"0").toString()))
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