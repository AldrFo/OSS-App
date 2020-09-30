package ru.mpei.feature_tasks.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import kotlinx.android.extensions.LayoutContainer
import ru.mpe.feature_tasks.R
import ru.mpei.domain_tasks.dto.TasksItem

interface TasksViewHolder{
    fun setName(name: String);
    fun setPrice(price: String);
    fun setLocation(location: String);
}

class TasksViewHolderImpl(
        override val containerView: View
): RecyclerView.ViewHolder(containerView), TasksViewHolder, LayoutContainer {
    override fun setName(name: String) {
        containerView.findViewById<TextView>(R.id.taskNameText).text = name
    }

    override fun setPrice(price: String) {
        containerView.findViewById<TextView>(R.id.taskPriceText).text = price
    }

    override fun setLocation(location: String) {
        containerView.findViewById<TextView>(R.id.taskLocationText).text = location
    }
}

class TasksItemBinder() : BaseItemBinder<TasksViewHolder, TasksItem>(){
    override fun bind(vh: TasksViewHolder, model: TasksItem, position: Int) {
        vh.setName(model.taskName)
        vh.setPrice(model.price)
        vh.setLocation(model.location)
    }
}

class TasksAdapterItem(): AdapterItem<TasksViewHolder, TasksItem>(
        isType = {it is TasksItem},
        layoutRes = R.layout.item_tasks,
        itemBinder = TasksItemBinder(),
        viewHolderGenerator = ::TasksViewHolderImpl
)