package ru.mpei.feature_profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.PopUntil
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ConfirmRefuseItem
import ru.mpei.domain_profile.dto.ReportItem
import ru.mpei.feature_profile.databinding.FragmentTaskReportBinding
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.Wish

class EditReportFragment(private val taskId: String, private val taskName: String, private val type: ReportType) : BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>() {

    private val binding by viewBinding(FragmentTaskReportBinding::bind)
    private val mSettings: SharedPreferences by inject()
    private val router: Router by inject()

    override var layoutId: Int = R.layout.fragment_task_report

    private val profileFeatureFactory: ProfileFeatureFactory by inject()
    override fun createFeature(): ProfileFeature = profileFeatureFactory.create()

    override val initEvent: ProfileEvent = Wish.System.InitReport

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        when (type) {
            ReportType.NEW -> {
                with (binding) {
                    btnSendNoReport.visibility = View.VISIBLE
                    btnSendWithReport.visibility = View.VISIBLE
                    btnSendReport.visibility = View.GONE

                    btnSendNoReport.setOnClickListener {
                        val body = ConfirmRefuseItem(task_id = taskId, user_id = mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!)
                        feature.accept(Wish.ConfirmTask(body))
                    }
                    btnSendWithReport.setOnClickListener {
                        val body = ReportItem(comment = fragmentTaskReportComment.text.toString(), user_id = mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!, task_id = taskId)
                        feature.accept(Wish.SendReport(body))
                    }
                }
            }

            ReportType.EDIT -> {
                with(binding){
                    btnSendReport.visibility = View.VISIBLE
                    btnSendWithReport.visibility = View.GONE
                    btnSendNoReport.visibility = View.GONE

                    btnSendReport.setOnClickListener {
                        val body = ReportItem(comment = fragmentTaskReportComment.text.toString(), user_id = mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!, task_id = taskId)
                        feature.accept(Wish.SendReport(body))
                    }
                }
            }
        }

        binding.fragmentTaskReportToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentTaskReportToolbar.setNavigationOnClickListener { router.executeCommand(PopUntil(TaskFragment::class)) }

        binding.fragmentTaskReportToolbarText.text = taskName

        binding.btnAddPhotoImage.setOnClickListener {
        }
    }

    override fun render(state: ProfileState) {

    }

    override fun handleEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.ConfirmSuccess -> {
                Toast.makeText(context, "Task confirmed", Toast.LENGTH_SHORT).show()
                router.executeCommand(PopUntil(TasksListFragment::class))
            }
            is ProfileEffect.ReportSendSuccess -> {
                Toast.makeText(context, "Report sent", Toast.LENGTH_SHORT).show()

                router.executeCommand(PopUntil(TasksListFragment::class))
            }
            is ProfileEffect.ReportSendError -> {
                Toast.makeText(context, "Report send error", Toast.LENGTH_SHORT).show()
            }
            is ProfileEffect.ConfirmError -> {
                Toast.makeText(context, "Confirm error", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
    }
}