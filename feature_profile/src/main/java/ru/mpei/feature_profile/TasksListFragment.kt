package ru.mpei.feature_profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_tasks_list.*
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ProfileItem
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.*

class TasksListFragment(private val type: TasksType, private val profileData: ProfileItem) : BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>() {

    private  val profileFeatureFactory: ProfileFeatureFactory by inject()
    private val router: Router by inject()

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

        fragment_tasks_toolbar_text.text = name

        fragment_tasks_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        fragment_tasks_toolbar.setNavigationOnClickListener { router.executeCommand( ClearBackStack() ) }

    }

    override fun render(state: ProfileState) {
    }

    override fun handleEffect(effect: ProfileEffect) = when(effect) {

        is ProfileEffect.TasksLoaded -> {
            Toast.makeText(context, "Список загружен, длинна - " + effect.tasksList.size, Toast.LENGTH_SHORT).show()
        }

        is ProfileEffect.TasksLoadError -> {
            Toast.makeText(context, "Возникла ошибка - попробуйте еще раз позже", Toast.LENGTH_SHORT).show()
        }

        else -> {}
    }

}