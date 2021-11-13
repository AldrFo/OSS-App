package ru.mpei.feature_profile

/**
 * Андрей Турлюк
 * А-08-17
 */

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ParamsItem
import ru.mpei.domain_profile.dto.ProfileItem
import ru.mpei.feature_profile.databinding.FragmentProfileBinding
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.Wish

// Фрагмент вкладка профиля
class ProfileFragment : BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>() {

    // Объекты для помощи доступа к элементам разметки, переходу между страницами и досутпа к сохраненным данным
    private val mSettings: SharedPreferences by inject()
    private val router: Router by inject()
    private val binding by viewBinding(FragmentProfileBinding::bind)

    // Ослеживание из какой вкладки мы пришли
    private var fromFragment: Boolean = false

    // Если пользователь авторизован, то зываем метод аутентификации
    // иначе инициируем вкладку авторизации
    override val initEvent: ProfileEvent
        get() = when (mSettings.getBoolean(APP_PREFERENCES_FLAG, false)) {
            false -> {
                Wish.System.InitLogin
            }
            true -> {
                Wish.Authorization(mSettings.getString(APP_PREFERENCES_ID, "0")!!, mSettings.getString(APP_PREFERENCES_PASS, "")!!)
            }
        }

    // Объект и метод создания фичи
    private val profileFeatureFactory: ProfileFeatureFactory by inject()
    override fun createFeature(): ProfileFeature = profileFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_profile

    // Метод, вызываемый при изменении состояния вклдаки
    override fun render(state: ProfileState) {

        // Если мы перешли в профиль из другой вкладки, то вызываем процесс повторной авторизации
        if (fromFragment) {
            fromFragment = false
            feature.accept(
                Wish.Authorization(
                    mSettings.getString(APP_PREFERENCES_ID, "0")!!,
                    mSettings.getString(APP_PREFERENCES_PASS, "")!!
                )
            )
        }

        // Выбираем разметку для отображеия в зависимости от авторизованости пользователя
        if (state.isAuthorized) {
            showProfile(state.profileData)
        } else {
            showLogin()
        }
    }

    // Обработка эффектов
    override fun handleEffect(effect: ProfileEffect) = when (effect) {

        // Эффект открытия магазина
        is ProfileEffect.OpenShop -> {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://oss-mpei.ru/shop.php"))
            startActivity(browserIntent)
        }

        // Эффект сохранения данных пользователя
        is ProfileEffect.SaveParams -> {
            saveParams(effect.paramsItem)
        }

        // Эффект ошибки аворизации
        is ProfileEffect.AuthorizationError -> {
            showError(AUTHORIZATION_ERROR)
        }

        // Эффект ошибки аутенификации
        is ProfileEffect.AuthenticationError -> {
            showError(AUTHENTICATION_ERROR)
        }

        // Эфект удаления данных о пользователе
        is ProfileEffect.ClearParams -> {
            mSettings.edit().clear().apply()
            with(binding) {
                loginEmail.text.clear()
                loginPassword.text.clear()
                mailInputLayout.isErrorEnabled = false
                passwordInputLayout.isErrorEnabled = false
            }
        }

        // Эффект валидации введенных данных
        is ProfileEffect.Validate -> {
            validate(effect.email, effect.pass)
        }

        else -> {
        }
    }

    // Отоьбражение вкладки авторизации
    private fun showLogin() {
        with(binding) {
            // Выбираем разметку для отображения
            ScrollViewProfile2.visibility = View.GONE
            ScrollViewProfile1.visibility = View.VISIBLE
            // Вешаем действия при нажатии на кнопку входа
            enterButton.setOnClickListener {
                feature.accept(Wish.ValidateFields(loginEmail.text.toString(), loginPassword.text.toString()))
                requireActivity().currentFocus?.let {
                    val inputMethodManager = getSystemService(requireContext(), InputMethodManager::class.java)!!
                    inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                }
            }
            // Вешаем действия на нажатие кнопки сброса пароля
            forgottenPasswordText.setOnClickListener {
                val dialog = ProfileDialogFragment()
                dialog.show(parentFragmentManager, "profileDialog")
            }
            // Вешаем действие на нажатие кнопки "зарегистрироваться"
            registerLink.setOnClickListener {
                val fragment = RegisterFragment()
                router.executeCommand(AddScreenForward { fragment })
            }
        }
    }

    // Отображение разметки профиля
    private fun showProfile(profileData: ProfileItem) {
        with(binding) {
            // Выбираем какую разметку отобразить
            ScrollViewProfile1.visibility = View.GONE
            ScrollViewProfile2.visibility = View.VISIBLE

            // СВязываем поля и данные для отображения
            initials.text = getString(R.string.initials).format(profileData.name[0], profileData.surname[0])
            name.text = getString(R.string.name_blank).format(profileData.name, profileData.surname)
            capital.text = profileData.capital.toString()

            // Вешаем действие на нажатие кнопки выхода
            exitButton.setOnClickListener {
                feature.accept(Wish.Exit)
            }

            // Вешаем действие на нажатие кнопки выполняемых заданий
            inProgressBtn.setOnClickListener {
                val fragment = TasksListFragment(TasksType.PROCESS, profileData)
                fromFragment = true
                router.executeCommand(AddScreenForward { fragment })
            }

            // Вешаем действия на нажатие кнопки заданий на проверке
            onCheckBtn.setOnClickListener {
                val fragment = TasksListFragment(TasksType.CHECK, profileData)
                fromFragment = true
                router.executeCommand(AddScreenForward { fragment })
            }

            // Вешаем действие на нажатие кнопки завершенныхь заданий
            finishedBtn.setOnClickListener {
                val fragment = TasksListFragment(TasksType.FINISHED, profileData)
                fromFragment = true
                router.executeCommand(AddScreenForward { fragment })
            }

            // Вешаем действие на нажатие кнопки отклоенных заданий
            refusedBtn.setOnClickListener {
                val fragment = TasksListFragment(TasksType.REFUSED, profileData)
                fromFragment = true
                router.executeCommand(AddScreenForward { fragment })
            }

            // Вешаем действия на нажатие кнопки открывания магазина
            btnOpenShop.setOnClickListener {
                val fragment = ShopFragment(profileData)
                fromFragment = true
                router.executeCommand(AddScreenForward { fragment })
            }
        }

    }

    // Метод сохранения данных пользователя
    private fun saveParams(params: ParamsItem) {
        mSettings.edit().putString(APP_PREFERENCES_ID, params.id).apply()
        mSettings.edit().putString(APP_PREFERENCES_PASS, params.pass).apply()
        mSettings.edit().putBoolean(APP_PREFERENCES_FLAG, true).apply()
    }

    // Метод валидации веденных данных
    private fun validate(email: String, pass: String) {
        val isEmailValid = email.isEmailValid()
        val isPassValid = pass.isNotEmpty()

        if (!isEmailValid) with(binding) {
            mailInputLayout.isErrorEnabled = true
            mailInputLayout.error = "Почтовый адрес введен неверно"
        }

        if (!isPassValid) with(binding) {
            passwordInputLayout.isErrorEnabled = !isPassValid
            passwordInputLayout.error = "Введите пароль"
        }

        if (isEmailValid && isPassValid) {
            feature.accept(Wish.Authentication)
        } else {
            feature.accept(Wish.ValidationFailed)
        }
    }

    // Метод валидации почтового ажреса
    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    // Метод отображения ошибки
    private fun showError(reason: Int) {
        when (reason) {
            0 -> {
                Toast.makeText(context, "Ошибка авторизации - необходимо войти повторно.", Toast.LENGTH_LONG).show()
            }
            1 -> {
                Toast.makeText(context, "Неверно введен логин или пароль.", Toast.LENGTH_LONG).show()
            }
            else -> {
            }
        }
    }

    // Вспомогательный объект с кностантами
    companion object {
        const val APP_PREFERENCES_FLAG = "isAuth"
        const val APP_PREFERENCES_PASS = "userPass"
        const val APP_PREFERENCES_ID = "userId"

        const val AUTHORIZATION_ERROR = 0
        const val AUTHENTICATION_ERROR = 1
    }
}