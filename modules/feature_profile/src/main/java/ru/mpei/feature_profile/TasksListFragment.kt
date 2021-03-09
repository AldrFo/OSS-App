package ru.mpei.feature_profile

import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.BaseAdapter
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.fastLazy
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ProfileItem
import ru.mpei.feature_profile.databinding.FragmentTasksListBinding
import ru.mpei.feature_profile.items.TaskAdapterItem
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.*

class TasksListFragment(val type: TasksType, private val profileData: ProfileItem) : BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>() {

    private  val profileFeatureFactory: ProfileFeatureFactory by inject()
    private val router: Router by inject()
    private val adapter: BaseAdapter by fastLazy { createAdapter() }
    private val binding by viewBinding(FragmentTasksListBinding::bind)

    private val name = when(type) {
        TasksType.PROCESS -> "Выполняемые"
        TasksType.CHECK -> "На проверке"
        TasksType.FINISHED -> "Завершенные"
        TasksType.REFUSED -> "Отклоненные"
    }

    override val initEvent: ProfileEvent = Wish.LoadTasks(
        when(type){
            TasksType.PROCESS -> "taken"
            TasksType.CHECK -> "inCheck"
            TasksType.FINISHED -> "accepted"
            TasksType.REFUSED -> "refused"
        }
    )

    override fun createFeature(): ProfileFeature = profileFeatureFactory.createTasksList(profileData)

    override var layoutId: Int = R.layout.fragment_tasks_list

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        super.onViewCreatedInternal(view, savedInstanceState)

        with(binding) {
            fragmentTasksToolbarText.text = name

            fragmentTasksToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            fragmentTasksToolbar.setNavigationOnClickListener { router.executeCommand(ClearBackStack()) }

            tasksList.adapter = adapter
            tasksList.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun render(state: ProfileState) {
        adapter.update(state.tasksList)

        if (state.tasksList.isEmpty()) {
            val tType = when (type) {
                TasksType.PROCESS -> "выполняемых"
                TasksType.REFUSED -> "отклоненных"
                TasksType.FINISHED -> "выполненных"
                TasksType.CHECK -> "проверяемых"
            }
            binding.emptyListLabel.text = getString(R.string.empty_tasks_list).format(tType)
            binding.emptyListLabel.visibility = View.VISIBLE
        } else {
            binding.emptyListLabel.visibility = View.GONE
        }

    }

    override fun handleEffect(effect: ProfileEffect) = when(effect) {


        is ProfileEffect.TasksLoadError -> {
            Toast.makeText(context, "Возникла ошибка - попробуйте еще раз позже", Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }

    private fun createAdapter() = BaseAdapter(
        TaskAdapterItem {
            val bundle = Bundle()
            bundle.putSerializable("taskData", it)
            val fragment = TaskFragment()
            fragment.arguments = bundle
            router.executeCommand(AddScreenForward { fragment })
        }
    )

}

