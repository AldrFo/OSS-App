package ru.mpei.feature_tasks.items

/**
 * Андрей Турлюк
 * А-08-17
 */

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kekmech.ru.common_adapter.AdapterItem
import kekmech.ru.common_adapter.BaseItemBinder
import ru.mpei.domain_tasks.dto.TasksItem
import ru.mpei.feature_tasks.R
import ru.mpei.feature_tasks.databinding.ItemTasksBinding

// Интерфейс элемента списка заданий
interface TasksViewHolder{
    fun setName(name: String)
    fun setPrice(price: String)
    fun setDates(startDate: String, endDate: String)
    fun setOnClickListener(onClick: () -> Unit)
}

/// Реализация этого интрефейса
class TasksViewHolderImpl(
    containerView: View
): RecyclerView.ViewHolder(containerView), TasksViewHolder {

    private val binding = ItemTasksBinding.bind(containerView)

    override fun setName(name: String) {
        binding.availName.text = name
    }

    override fun setPrice(price: String) {
        binding.availPrice.text = price
    }

    override fun setDates(startDate: String, endDate: String) {
        binding.availDates.text = "${startDate.substring(0, startDate.length-3)} - ${endDate.substring(0, endDate.length-3)}"
    }

    override fun setOnClickListener(onClick: () -> Unit) {
        binding.root.setOnClickListener { onClick() }
    }
}

// Связывание кода и разметки
class TasksItemBinder(
    private val onClick: (TasksItem) -> Unit
) : BaseItemBinder<TasksViewHolder, TasksItem>(){
    override fun bind(vh: TasksViewHolder, model: TasksItem, position: Int) {
        vh.setName(model.taskName)
        vh.setPrice(model.price)
        vh.setDates(model.startDate, model.endDate)
        vh.setOnClickListener{onClick(model)}
    }
}

// Адаптер списка заданий
class TasksAdapterItem(onClick: (TasksItem) -> Unit): AdapterItem<TasksViewHolder, TasksItem>(
        isType = {it is TasksItem},
        layoutRes = R.layout.item_tasks,
        itemBinder = TasksItemBinder(onClick),
        viewHolderGenerator = ::TasksViewHolderImpl
)