package ru.mpei.vmss.myapplication.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import ru.mpei.vmss.myapplication.R
import ru.mpei.vmss.myapplication.activities.MainActivity
import ru.mpei.vmss.myapplication.activities.MainActivity.Companion.hideKeyboard
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*

class Login : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        enterButton.setOnClickListener {
            if (Objects.requireNonNull(loginEmail.text).toString().trim { it <= ' ' }.isEmpty() || Objects.requireNonNull(loginPassword.text).toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(context, "Проверьте заполнение полей", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            try {
                val body = JSONObject()
                body.put("email", loginEmail.text.toString())
                body.put("password", loginPassword.text.toString())
                val request = JsonObjectRequest(Request.Method.POST, requireContext().getString(R.string.authUrl), body,
                        Response.Listener { response: JSONObject ->
                            val result = response.optBoolean("error")
                            if (!result) {
                                MainActivity.MyAdapter.tasksFragment.updateList()
                                User.isAuth(true, response.optString("pass"), response.optString("id"))
                                hideKeyboard(activity!!)
                            } else {
                                Toast.makeText(context, response.optString("key"), Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener { Toast.makeText(context, "Возникла проблема, попробуйте позже", Toast.LENGTH_SHORT).show() })
                val requestQueue = Volley.newRequestQueue(context)
                requestQueue.add(request)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        registerButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(requireContext().getString(R.string.regUrl)))
            startActivity(browserIntent)
        }
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
}