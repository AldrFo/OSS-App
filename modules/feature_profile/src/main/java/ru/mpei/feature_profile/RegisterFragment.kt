package ru.mpei.feature_profile

/**
 * Андрей Турлюк
 * А-08-17
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_navigation.ClearBackStack
import kekmech.ru.common_navigation.Router
import okhttp3.ResponseBody
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mpei.domain_profile.ProfileApi
import ru.mpei.feature_profile.databinding.FragmentRegistrationBinding

class RegisterFragment : Fragment() {

    // Обекты для перехода между страницами и связывания разметки и кода
    private val router: Router by inject()
    private val binding by viewBinding(FragmentRegistrationBinding::bind)

    // При создании отображения связываем разметку и код
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    // После создания ротображения свзяываем поля и данные
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Вешаем действия при нажатии на кнопку регистрации
        binding.registerButton.setOnClickListener {
            if (validateFields()) {

                // Выбираем пол
                val gender = if (binding.radioMale.isChecked) "male" else "female"

                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl("http://cy37212.tmweb.ru/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(ProfileApi::class.java)

                /// Создаем запрос к серверу на регстрацию
                val call = with(binding) {
                    service.register(
                        email = regMail.text.toString(),
                        name = regName.text.toString(),
                        surname = regSurname.text.toString(),
                        gender = gender,
                        group = regGroup.text.toString(),
                        password = regPassword.text.toString()
                    )
                }

                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        // В заивисимости от кода овтета Выводим одно из сообщений
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

                    // При неудачно запросе выводим сообщение об ошибке
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(context, "Проблема на сервере - попробуйте еще раз позже", Toast.LENGTH_LONG).show()
                    }

                })
            }
        }

        // Действия при нажатии кнопки возврата на страницу авторизации
        binding.enterLine.setOnClickListener {
            router.executeCommand(ClearBackStack())
        }
    }

    // Метод валидации введенных данных
    private fun validateFields(): Boolean {
        with(binding) {
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
            return isEmailValid && isGroupValid
                && isNameValid && isPasswordValid
                && isRepeatPasswordValid
                && isSurnameValid && isMaleChosen
        }
    }

    // Метод валидации почтового адреса
    private fun String.isEmailValid(): Boolean {
        return this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    // Метод валидации имени
    private fun String.isNameValid(): Boolean {
        return this.isNotEmpty() && this[0].isUpperCase()
    }

}