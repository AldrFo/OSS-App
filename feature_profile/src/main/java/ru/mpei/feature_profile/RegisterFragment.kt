package ru.mpei.feature_profile

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.popup_reset_password.view.*
import okhttp3.ResponseBody
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mpei.domain_profile.ProfileApi

class RegisterFragment: Fragment() {

    private val router: Router by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerButton.setOnClickListener {
            if (validateFields()){

                val gender = if (radioMale.isChecked) "male" else "female"

                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("http://cy37212.tmweb.ru/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(ProfileApi::class.java)

                val call = service.register(
                    email = regMail.text.toString(),
                    name = regName.text.toString(),
                    surname = regSurname.text.toString(),
                    gender = gender,
                    group = regGroup.text.toString(),
                    password = regPassword.text.toString()
                )

                call.enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        when (response.code()) {
                            200 -> {
                                Toast.makeText(context, "Сообщение с подтверждением регистрации отправлено на указанную почту. Чтобы завершить регистрацию, пройдите по вложенной ссылке.", Toast.LENGTH_LONG).show()
                                router.executeCommand(ClearBackStack())
                            }
                            400 -> {
                                Toast.makeText(context, "Пользователь с таким email уже зарегистрирован.", Toast.LENGTH_LONG).show()
                            }
                            403 -> {
                                Toast.makeText(context, "Запрос на регистрацию пользователя с таким email уже существует. Проверьте почтовый ящик, в том числе спам", Toast.LENGTH_LONG).show()
                            }
                            else -> {
                                Toast.makeText(context, "Проблема на сервере - попробуйте еще раз позже", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(context, "Проблема на сервере - попробуйте еще раз позже", Toast.LENGTH_LONG).show()
                    }

                })
            }
        }

        enterLine.setOnClickListener {
            router.executeCommand(ClearBackStack())
        }
    }

    private fun validateFields(): Boolean{

        val isEmailValid = regMail.text.toString().matches(Regex("""[a-zA-Z]+@mpei.ru"""))
        val isNameValid = regName.text.toString().isNameValid()
        val isSurnameValid = regSurname.text.toString().isNameValid()
        val isGroupValid = regGroup.text.toString().matches(Regex("""[А-Я]{1,2}+-+[0-9]{1,2}+-+[0-9]{2}"""))
        val isPasswordValid = regPassword.text.length > 7
        val isRepeatPasswordValid = regRepeatPassword.text.toString().length > 7 && regPassword.text.toString() == regRepeatPassword.text.toString()
        val isMaleChosen = radioMale.isChecked || radioFemale.isChecked

        if (!isNameValid) {
            regNameTextInputLayout.isErrorEnabled = true
            regNameTextInputLayout.error = "Проверьте правильность написания имени"
        } else {
            regNameTextInputLayout.isErrorEnabled = false
        }

        if (!isSurnameValid) {
            regSurnameInputLayout.isErrorEnabled = true
            regSurnameInputLayout.error = "Проверьте правильность написания фамилии"
        } else {
            regSurnameInputLayout.isErrorEnabled = false
        }

        if (!isEmailValid) {
            regEmailInputLayout.isErrorEnabled = true
            regEmailInputLayout.error = "Проверьте правильность написания почты"
        } else {
            regEmailInputLayout.isErrorEnabled = false
        }

        if (!isGroupValid) {
            regGroupInputLayout.isErrorEnabled = true
            regGroupInputLayout.error = "Проверьте правильность написания группы"
        } else {
            regGroupInputLayout.isErrorEnabled = false
        }

        if (!isPasswordValid) {
            regPasswordInputLayout.isErrorEnabled = true
            regPasswordInputLayout.error = "В пароле должно быть не менее 8 символов"
        } else {
            regPasswordInputLayout.isErrorEnabled = false
        }

        if (!isRepeatPasswordValid) {
            regRepeatPasswordInputLayout.isErrorEnabled = true
            regRepeatPasswordInputLayout.error = "Пароли не совпадают"
        } else {
            regRepeatPasswordInputLayout.isErrorEnabled = false
        }

        if (!isMaleChosen) {
            radioFemale.error = "Необходимо выбрать хотя бы одно значение"
        } else {
            radioFemale.error = null
        }

        return isEmailValid && isGroupValid && isNameValid && isPasswordValid && isRepeatPasswordValid && isSurnameValid && isMaleChosen
    }

    private fun String.isEmailValid(): Boolean {
        return this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun String.isNameValid(): Boolean {
        return this.isNotEmpty() && this[0].isUpperCase()
    }

}