package ru.mpei.feature_profile.items

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import ru.mpei.domain_profile.dto.TaskItem
import kekmech.ru.common_adapter.BaseItemBinder
import ru.mpei.feature_profile.R
import ru.mpei.feature_profile.databinding.ItemTaskBinding

interface TaskViewHolder {
    fun setTaskName(name: String)
    fun setTaskPrice(price: String)
    fun setTaskDates(start: String, end: String)
    fun setOnClickListener(onClick: () -> Unit)
}

class TasksViewHolderImpl(
    containerView: View
): RecyclerView.ViewHolder(containerView), TaskViewHolder {

    private val binding = ItemTaskBinding.bind(containerView)

    override fun setTaskName(name: String) {
        binding.taskName.text = name
    }

    override fun setTaskPrice(price: String) {
        binding.taskPrice.text = price
    }

    override fun setTaskDates(start: String, end: String) {
        binding.taskDates.text = "${start.substring(0, start.length - 3)} - ${end.substring(0, end.length - 3)}"
    }

    override fun setOnClickListener(onClick: () -> Unit) {
        binding.root.setOnClickListener { onClick() }
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