package ru.mpei.vmss.myapplication.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import ru.mpei.vmss.myapplication.R
import ru.mpei.vmss.myapplication.fragments.User
import ru.mpei.vmss.myapplication.pojo.Task

class TasksAdapter(private val context: Context, private val elements: MutableList<Task>) : BaseExpandableListAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    init{
    }

    override fun getGroupCount(): Int {
        return elements.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any {
        return elements[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return null
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.tasks_element, parent, false)
        }
        val taskNameText = view.findViewById<TextView>(R.id.taskNameText)
        taskNameText.text = getTasksElement(groupPosition).taskName
        val taskPriceText = view.findViewById<TextView>(R.id.taskPriceText)
        taskPriceText.text = getTasksElement(groupPosition).price + " б."
        val taskLocationText = view.findViewById<TextView>(R.id.taskLocationText)
        taskLocationText.text = "Место: " + getTasksElement(groupPosition).location
        return view
    }

    private fun getTasksElement(position: Int): Task {
        return getGroup(position) as Task
    }

    @SuppressLint("SetTextI18n")
    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.available_tasks_element_child, parent, false)
        }
        val availableDescription = view.findViewById<TextView>(R.id.availableDescriptionText)
        availableDescription.text = getTasksElement(groupPosition).taskDescription
        val availableBeginTime = view.findViewById<TextView>(R.id.availableBeginText)
        availableBeginTime.text = "Начало: " + getTasksElement(groupPosition).startDate
        val availableEndTime = view.findViewById<TextView>(R.id.availableEndText)
        availableEndTime.text = "Конец: " + getTasksElement(groupPosition).endDate
        val availableRefuseBefore = view.findViewById<TextView>(R.id.availableCommentText)
        availableRefuseBefore.text = "От задания можно отказаться не позднее, чем: " + getTasksElement(groupPosition).refuseInfo
        view.findViewById<View>(R.id.availableAgreeButton).setOnClickListener { v: View? ->
            if (User.isAuth) {
                try {
                    val body = JSONObject()
                    body.put("user_id", User.userId)
                    body.put("task_id", getTasksElement(groupPosition).id)
                    val request = JsonObjectRequest(Request.Method.POST, context.getString(R.string.takeTaskUrl), body,
                            Response.Listener { },
                            Response.ErrorListener { })
                    val requestQueue = Volley.newRequestQueue(context)
                    requestQueue.add(request)
                    elements.removeAt(groupPosition)
                    notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(context, "Необходимо авторизоваться", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }

}