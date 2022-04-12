package ru.mpei.feature_profile

/**
 * Андрей Турлюк
 * А-08-17
 */

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kekmech.ru.common_kotlin.OSS_TAG
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mpei.domain_profile.ProfileApi
import ru.mpei.feature_profile.databinding.PopupResetPasswordBinding

// Диалоговое окно сброса пароля
class ProfileDialogFragment : DialogFragment() {

    // Объект для свзяывания кода и разметки
    private lateinit var binding: PopupResetPasswordBinding

    // При создании отображения связываем разметку и код
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.popup_reset_password, container)
    }

    // Когда отображение создано
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PopupResetPasswordBinding.bind(view)
        with(binding) {

            // Вешаем действие на кнопку отправки запроса сброса пароля
            btnSendRequest.setOnClickListener {
                emailInput.error = "Почтовый адрес введен неверно"
                // Проверяем валиость почтового адреса
                if (!resetPasswordEmail.text.toString().isEmailValid()) {
                    // Если почтовый адрес не прошел валидацию, то отображаем ошибку
                    emailInput.isErrorEnabled = true
                } else {
                    // Иначе делаем запрос к серверу
                    emailInput.isErrorEnabled = false
                    val retrofit: Retrofit = Retrofit.Builder()
                        .baseUrl("https://oss.mpei.ru/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val service = retrofit.create(ProfileApi::class.java)

                    val call = service.restorePass(resetPasswordEmail.text.toString())

                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            when {
                                // Если запрос был успешно обработан, то меняем разметку и отображаем сообщение об отправка письмо со ссылкой на сброс пароля
                                response.isSuccessful -> {
                                    emailSendFrame.visibility = View.GONE
                                    okFrame.visibility = View.VISIBLE
                                    popupEmailSentMessage.text = getString(R.string.reset_password_text_blank).format(resetPasswordEmail.text.toString())
                                }
                                // Если запрос не был найден, то отображаем ошибку о неизвестном адресе электронной почты
                                response.code() == 401 -> {
                                    emailInput.error = "Неизвестный почтовый адрес!"
                                    emailInput.isErrorEnabled = true
                                }
                                else -> {
                                    // Если запрос не был успешным по какой-то другой причине, то выводим ошибку
                                    emailInput.error = "Произошла неизвестная ошибка, попробуйте позднее!"
                                    emailInput.isErrorEnabled = true
                                }
                            }
                        }

                        // Если запрос был неудачным, то выводим ошшибку
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            emailInput.error = "Проблема на сервере, попробуйте еще раз позднее"
                            emailInput.isErrorEnabled = true
                        }
                    })
                }
            }

            // Вешаем действие на кнопку возвращения
            btnCancel.setOnClickListener { dismiss() }
        }
    }

    // Метод проверки валидности адреса электронной почты
    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}