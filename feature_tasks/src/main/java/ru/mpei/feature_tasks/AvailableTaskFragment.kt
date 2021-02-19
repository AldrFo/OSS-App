package ru.mpei.feature_tasks

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_task_available.*
import org.koin.android.ext.android.inject
import ru.mpei.domain_tasks.dto.TasksItem

class AvailableTaskFragment: Fragment() {

    private val router: Router by inject()
    private lateinit var it: TasksItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        it = arguments?.get("availableTaskData") as TasksItem
        return inflater.inflate(R.layout.fragment_task_available, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_task_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        fragment_task_toolbar.setNavigationOnClickListener { router.executeCommand( ClearBackStack() ) }

        fragment_task_toolbar_text.text = it.taskName

        task_name_avail.text = it.taskName
        balance_avail.text = it.price
        task_description_avail.text = if (it.taskDescription.isEmpty()) getString(R.string.no_description) else it.taskDescription
        place_avail.text = Html.fromHtml(getString(R.string.place, it.location))
        begin_date_avail.text = Html.fromHtml(getString(R.string.begin_date, it.endDate.substring(0, it.endDate.length-3)))
        end_date_avail.text = Html.fromHtml(getString(R.string.end_date, it.endDate.substring(0, it.endDate.length-3)))
        refuse_date_avail.text = Html.fromHtml(getString(R.string.refuse_date, it.refuseInfo.substring(0, it.refuseInfo.length-3)))
    }

}