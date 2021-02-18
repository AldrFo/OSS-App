package ru.mpei.feature_tasks.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kotlinx.android.extensions.LayoutContainer
import ru.mpei.domain_tasks.dto.TasksItem
import ru.mpei.feature_tasks.R

interface TasksViewHolder{
    fun setName(name: String)
    fun setPrice(price: String)
    fun setOnClickListener(onClick: () -> Unit)
}

class TasksViewHolderImpl(
        override val containerView: View
): RecyclerView.ViewHolder(containerView), TasksViewHolder, LayoutContainer {
    override fun setName(name: String) {
        containerView.findViewById<TextView>(R.id.task_name).text = name
    }

    override fun setPrice(price: String) {
        containerView.findViewById<TextView>(R.id.task_price).text = price
    }

    override fun setOnClickListener(onClick: () -> Unit) {
        containerView.setOnClickListener { onClick() }
    }
}

class TasksItemBinder(
    private val onClick: (TasksItem) -> Unit
) : BaseItemBinder<TasksViewHolder, TasksItem>(){
    override fun bind(vh: TasksViewHolder, model: TasksItem, position: Int) {
        vh.setName(model.taskName)
        vh.setPrice(model.price)
        vh.setOnClickListener{onClick(model)}
    }
}

class TasksAdapterItem(onClick: (TasksItem) -> Unit): AdapterItem<TasksViewHolder, TasksItem>(
        isType = {it is TasksItem},
        layoutRes = R.layout.item_tasks,
        itemBinder = TasksItemBinder(onClick),
        viewHolderGenerator = ::TasksViewHolderImpl
)