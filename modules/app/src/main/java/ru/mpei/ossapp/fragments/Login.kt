package ru.mpei.ossapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kekmech.ru.common_android.viewbinding.viewBinding
import org.json.JSONException
import org.json.JSONObject
import ru.mpei.ossapp.R
import ru.mpei.ossapp.databinding.FragmentLogin2Binding
import java.util.*

class Login : Fragment() {

    private val binding by viewBinding(FragmentLogin2Binding::bind)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_login_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.enterButton.setOnClickListener {
            if (Objects.requireNonNull(binding.loginEmail.text).toString().trim { it <= ' ' }.isEmpty() || Objects.requireNonNull(binding.loginPassword.text).toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(context, "Проверьте заполнение полей", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            try {
                val body = JSONObject()
                body.put("email", binding.loginEmail.text.toString())
                body.put("password", binding.loginPassword.text.toString())
                /*val request = JsonObjectRequest(Request.Method.POST, requireContext().getString(R.string.authUrl), body,
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
                requestQueue.add(request)*/
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        binding.registerButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(requireContext().getString(R.string.regUrl)))
            startActivity(browserIntent)
        }
    }
}