package ru.mpei.feature_profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import kekmech.ru.common_android.close
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.TaskItem
import ru.mpei.feature_profile.mvi.ProfileEvent.*
import ru.mpei.feature_profile.databinding.FragmentTaskReportBinding
import ru.mpei.feature_profile.mvi.*

class EditReportFragment(private val profileData: TaskItem, private val taskId: String) : BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>() {

    private val binding by viewBinding(FragmentTaskReportBinding::bind)
    private val router: Router by inject()

    override var layoutId: Int = R.layout.fragment_task_report

    private  val profileFeatureFactory: ProfileFeatureFactory by inject()
    override fun createFeature(): ProfileFeature = profileFeatureFactory.create()

    override val initEvent: ProfileEvent = Wish.System.InitReport

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        binding.btnSendNoReport.setOnClickListener {
            feature.accept(Wish.ConfirmTask(taskId, profileData.id))
        }
        binding.btnSendReport.setOnClickListener {
            feature.accept(Wish.SendReport(binding.fragmentTaskReportComment.text.toString(), profileData.id, taskId, ""))
        }
    }

    override fun render(state: ProfileState) {

    }

    override fun handleEffect(effect: ProfileEffect) {
        when(effect){
            is ProfileEffect.ConfirmSuccess -> {
                Toast.makeText(context, "Task confirmed", Toast.LENGTH_SHORT).show()
                router.executeCommand( ClearBackStack() )
            }
            is ProfileEffect.ReportSendSuccess -> {
                Toast.makeText(context, "Report sent", Toast.LENGTH_SHORT).show()
                router.executeCommand( ClearBackStack() )
            }
            is ProfileEffect.ReportSendError -> {
                Toast.makeText(context, "Report send error", Toast.LENGTH_SHORT).show()
            }
            is ProfileEffect.ConfirmError -> {
                Toast.makeText(context, "Confirm error", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
}