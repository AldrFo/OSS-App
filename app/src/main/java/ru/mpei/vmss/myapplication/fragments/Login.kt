package ru.mpei.vmss.myapplication.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
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
import ru.mpei.vmss.myapplication.activities.MainActivity.Companion.hideKeyboard
import java.util.*

class Login : Fragment() {
    private lateinit var email: AppCompatEditText
    private lateinit var password: AppCompatEditText
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_login, container, false)
        email = rootView.findViewById(R.id.loginEmail)
        password = rootView.findViewById(R.id.loginPassword)
        rootView.findViewById<View>(R.id.enterButton).setOnClickListener { v: View? ->
            if (Objects.requireNonNull(email.getText()).toString().trim { it <= ' ' }.length == 0 || Objects.requireNonNull(password.getText()).toString().trim { it <= ' ' }.length == 0) {
                Toast.makeText(getContext(), "Проверьте заполнение полей", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            try {
                val body = JSONObject()
                body.put("email", email.getText().toString())
                body.put("password", password.getText().toString())
                val request = JsonObjectRequest(Request.Method.POST, requireContext().getString(R.string.authUrl), body,
                        Response.Listener { response: JSONObject ->
                            val result = response.optBoolean("error")
                            if (!result) {
                                MainActivity.MyAdapter.tasksFragment.updateList()
                                User.isAuth(true, response.optString("pass"), response.optString("id"))
                                hideKeyboard(activity!!)
                            } else {
                                Toast.makeText(getContext(), response.optString("key"), Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener { error: VolleyError? -> Toast.makeText(getContext(), "Возникла проблема, попробуйте позже", Toast.LENGTH_SHORT).show() })
                val requestQueue = Volley.newRequestQueue(context)
                requestQueue.add(request)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        rootView.findViewById<View>(R.id.registerButton).setOnClickListener { v: View? ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(requireContext().getString(R.string.regUrl)))
            startActivity(browserIntent)
        }
        return rootView
    }
}