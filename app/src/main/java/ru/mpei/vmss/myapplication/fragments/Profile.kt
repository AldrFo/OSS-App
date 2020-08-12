package ru.mpei.vmss.myapplication.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import ru.mpei.vmss.myapplication.R
import ru.mpei.vmss.myapplication.activities.MainActivity
import ru.mpei.vmss.myapplication.activities.MainActivity.Companion.deleteData
import ru.mpei.vmss.myapplication.activities.TasksActivity
import java.util.*

class Profile : Fragment {
    private var hashPass: String? = null
    private var userId: String? = null
    private var userName: String? = null
    private var userSurname: String? = null
    private var userCapital: String? = null

    constructor() {}
    constructor(pass: String?, id: String?) {
        hashPass = pass
        userId = id
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        val name = rootView.findViewById<TextView>(R.id.profileName)
        val capital = rootView.findViewById<TextView>(R.id.profileCoins)
        getUserData(name, capital)
        rootView.findViewById<View>(R.id.profileExitButton).setOnClickListener { v: View? ->
            MainActivity.MyAdapter.tasksFragment.updateList()
            deleteData()
            User.updateLayout()
        }
        rootView.findViewById<View>(R.id.profileShopButton).setOnClickListener { v: View? ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(requireContext().getString(R.string.shopUrl)))
            startActivity(browserIntent)
        }
        val list = rootView.findViewById<ListView>(R.id.profileTaskTypeList)
        list.onItemClickListener = OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            val intent = Intent(context, TasksActivity::class.java)
            intent.putExtra("type", id)
            startActivity(intent)
        }
        return rootView
    }

    private fun getUserData(name: TextView, capital: TextView) {
        try {
            val body = JSONObject()
            body.put("id", userId)
            body.put("pass", hashPass)
            val request = JsonObjectRequest(Request.Method.POST, context!!.getString(R.string.lkUrl), body,
                    Response.Listener { response: JSONObject ->
                        val result = response.optBoolean("error")
                        if (!result) {
                            userName = response.optString("name")
                            userSurname = response.optString("surname")
                            userCapital = response.optString("capital")
                            updateInfo(name, capital)
                        } else {
                            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener { error: VolleyError? -> Toast.makeText(getContext(), "Возникла проблема, попробуйте позже", Toast.LENGTH_SHORT).show() })
            val requestQueue = Volley.newRequestQueue(context)
            requestQueue.add(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateInfo(name: TextView, capital: TextView) {
        name.text = "$userName $userSurname"
        capital.text = "Ваш баланс: $userCapital р."
    }
}