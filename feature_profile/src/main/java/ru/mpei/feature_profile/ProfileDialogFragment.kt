package ru.mpei.feature_profile

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.popup_reset_password.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mpei.domain_profile.ProfileApi


class ProfileDialogFragment : DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = layoutInflater.inflate(R.layout.popup_reset_password, container)
        rootView.btn_send_request.setOnClickListener {
            rootView.email_input.error = "Почтовый адрес введен неверно"
            if (!rootView.resetPasswordEmail.text.toString().isEmailValid()) {
                rootView.email_input.isErrorEnabled = true
            } else {
                rootView.email_input.isErrorEnabled = false

                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("http://cy37212.tmweb.ru/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(ProfileApi::class.java)

                val call = service.restorePass(rootView.resetPasswordEmail.text.toString())

                call.enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        when {
                            response.code() == 200 -> {
                                rootView.email_send_frame.visibility = View.GONE
                                rootView.ok_frame.visibility = View.VISIBLE
                                rootView.popup_emailSent_message.text = getString(R.string.reset_password_text_blank).replace("example@mail.ru", rootView.resetPasswordEmail.text.toString(), true)
                            }
                            response.code() == 401 -> {
                                rootView.email_input.error = "Неизвестный почтовый адрес!"
                                rootView.email_input.isErrorEnabled = true
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        rootView.email_input.error = "Проблема на сервере, попробуйте еще раз позднее"
                        rootView.email_input.isErrorEnabled = true
                    }
                })

            }
        }
        rootView.btn_cancel.setOnClickListener { dismiss() }
        return rootView
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

}