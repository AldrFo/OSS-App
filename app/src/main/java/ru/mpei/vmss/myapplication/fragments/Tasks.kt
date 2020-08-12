package ru.mpei.vmss.myapplication.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.mpei.vmss.myapplication.R
import ru.mpei.vmss.myapplication.adapters.TasksAdapter
import ru.mpei.vmss.myapplication.pojo.Task
import java.util.*

class Tasks : Fragment() {
    private var adapter: TasksAdapter? = null
    private val dataList: MutableList<Task> = ArrayList()

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tasks, container, false)
        val list = rootView.findViewById<ExpandableListView>(R.id.tasksList)
        adapter = TasksAdapter(requireContext(), dataList)
        list.setAdapter(adapter)
        list.setOnGroupExpandListener { groupPosition: Int ->
            for (g in 0 until adapter!!.groupCount) {
                if (g != groupPosition) {
                    list.collapseGroup(g)
                }
            }
        }
        updateList()
        val refresher: SwipeRefreshLayout = rootView.findViewById(R.id.tasksRefresher)
        refresher.setColorSchemeColors(requireContext().getColor(R.color.bgBottomNavigation))
        refresher.setOnRefreshListener {
            updateList()
            refresher.isRefreshing = false
        }
        return rootView
    }

    fun updateList() {
        dataList.clear()
        val body = JSONObject()
        try {
            if (User.isAuth) {
                body.put("id", User.userId)
            } else {
                body.put("id", 0)
            }
            body.put("type", "available")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val request = JsonArrayRequest(Request.Method.POST,
                requireContext().getString(R.string.getTasksUrl), body, Response.Listener { response: JSONArray ->
            for (i in 0 until response.length()) {
                var obj: JSONObject
                try {
                    obj = response.getJSONObject(i)
                    dataList.add(Task(obj.getString("description_short"),
                            obj.getString("description_full"),
                            obj.getString("place"),
                            obj.getString("reward"),
                            obj.getString("start"),
                            obj.getString("end"),
                            obj.getString("refuse_before"),
                            obj.getString("id")))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            adapter!!.notifyDataSetChanged()
        },
                Response.ErrorListener { error: VolleyError? -> Toast.makeText(requireContext(), "Ошибка соединения", Toast.LENGTH_LONG).show() })
        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(request)
    }
}