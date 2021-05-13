package ru.mpei.feature_profile

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kekmech.ru.common_android.viewbinding.viewBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mpei.domain_profile.ProfileApi
import ru.mpei.feature_profile.databinding.PopupResetPasswordBinding

class ProfileDialogFragment : DialogFragment() {

    private lateinit var binding: PopupResetPasswordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.popup_reset_password, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PopupResetPasswordBinding.bind(view)
        with(binding) {
            btnSendRequest.setOnClickListener {
                emailInput.error = "Почтовый адрес введен неверно"
                if (!resetPasswordEmail.text.toString().isEmailValid()) {
                    emailInput.isErrorEnabled = true
                } else {
                    emailInput.isErrorEnabled = false

                    val retrofit: Retrofit = Retrofit.Builder()
                        .baseUrl("http://cy37212.tmweb.ru/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val service = retrofit.create(ProfileApi::class.java)

                    val call = service.restorePass(resetPasswordEmail.text.toString())

                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            when {
                                response.code() == 200 -> {
                                    emailSendFrame.visibility = View.GONE
                                    okFrame.visibility = View.VISIBLE
                                    popupEmailSentMessage.text = getString(R.string.reset_password_text_blank).format(resetPasswordEmail.text.toString())
                                }
                                response.code() == 401 -> {
                                    emailInput.error = "Неизвестный почтовый адрес!"
                                    emailInput.isErrorEnabled = true
                                }
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            emailInput.error = "Проблема на сервере, попробуйте еще раз позднее"
                            emailInput.isErrorEnabled = true
                        }
                    })
                }
            }
            btnCancel.setOnClickListener { dismiss() }
        }
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

}