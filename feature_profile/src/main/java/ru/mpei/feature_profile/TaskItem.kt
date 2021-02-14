package ru.mpei.feature_profile

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kotlinx.android.extensions.LayoutContainer
import ru.mpei.domain_profile.dto.TaskItem
import kekmech.ru.common_adapter.BaseItemBinder


interface TaskViewHolder {
    fun setTaskName(name: String)
    fun setTaskPrice(price: String)
    fun setTaskDates(start: String, end: String)
    fun setOnClickListener(onClick: () -> Unit)
}

class TasksViewHolderImpl(
    override val containerView: View
): RecyclerView.ViewHolder(containerView), TaskViewHolder, LayoutContainer {
    override fun setTaskName(name: String) {
        containerView.findViewById<TextView>(R.id.task_name).text = name
    }

    override fun setTaskPrice(price: String) {
        containerView.findViewById<TextView>(R.id.task_price).text = price
    }

    override fun setTaskDates(start: String, end: String) {
        containerView.findViewById<TextView>(R.id.task_dates).text = start+" - "+end
    }

    override fun setOnClickListener(onClick: () -> Unit) {
        containerView.setOnClickListener { onClick() }
    }

}

class TaskItemBinder(
    private val onClick: (TaskItem) -> Unit
) : BaseItemBinder<TaskViewHolder, TaskItem>() {

    override fun bind(vh: TaskViewHolder, model: TaskItem, position: Int) {
        vh.setTaskName(model.taskName)
        vh.setTaskPrice(model.price)
        vh.setTaskDates(model.startDate, model.endDate)
        vh.setOnClickListener { onClick(model) }
    }
}

class TaskAdapterItem(onClick: (TaskItem) -> Unit): AdapterItem<TaskViewHolder, TaskItem>(
    isType = {it is TaskItem},
    layoutRes = R.layout.item_task,
    itemBinder = TaskItemBinder(onClick),
    viewHolderGenerator = ::TasksViewHolderImpl
)