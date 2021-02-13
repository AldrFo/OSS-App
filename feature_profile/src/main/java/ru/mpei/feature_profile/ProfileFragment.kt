package ru.mpei.feature_profile

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.AddScreenForward
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ParamsItem
import ru.mpei.domain_profile.dto.ProfileItem
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.Wish

@Suppress("IMPLICIT_CAST_TO_ANY")
class  ProfileFragment: BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>(){

    private val APP_PREFERENCES_FLAG = "isAuth"
    private val APP_PREFERENCES_PASS = "userPass"
    private val APP_PREFERENCES_ID = "userId"

    private val mSettings: SharedPreferences by inject()
    private val router: Router by inject()

    override val initEvent: ProfileEvent get() = when ( mSettings.getBoolean(APP_PREFERENCES_FLAG, false) ) {
        false -> {
            Wish.System.InitLogin
        }
        true -> {
            Wish.Authorization(mSettings.getString(APP_PREFERENCES_ID, "0")!!, mSettings.getString(APP_PREFERENCES_PASS, "")!!)
        }
    }

    private  val profileFeatureFactory: ProfileFeatureFactory by inject()

    override fun createFeature(): ProfileFeature = profileFeatureFactory.create()

    override var layoutId: Int = R.layout.fragment_profile

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
    }

    override fun render(state: ProfileState) {

        if (state.isAuthorized){
            showProfile(state.profileData)
        } else {
            showLogin()
        }

    }

    override fun handleEffect(effect: ProfileEffect) = when(effect) {

        is ProfileEffect.SaveParams -> {
            saveParams(effect.paramsItem)
        }

        is ProfileEffect.AuthorizationError -> {
            showError(0)
        }

        is ProfileEffect.AuthenticationError -> {
            showError(1)
        }

        is ProfileEffect.ClearParams -> {
            mSettings.edit().clear().apply()
            loginEmail.text.clear()
            loginPassword.text.clear()
            mailInputLayout.isErrorEnabled = false
            passwordInputLayout.isErrorEnabled = false
        }

        is ProfileEffect.Validate -> {

            validate(effect.email, effect.pass)
        }
    }

    private fun showLogin(){
        ScrollViewProfile2.visibility = View.GONE
        ScrollViewProfile1.visibility = View.VISIBLE
        enterButton.setOnClickListener {
            feature.accept(Wish.ValidateFields(loginEmail.text.toString(), loginPassword.text.toString()))
        }
        forgotten_password_text.setOnClickListener {
            val dialog = ProfileDialogFragment()
            dialog.show(requireFragmentManager(), "profileDialog")
        }
        registerLink.setOnClickListener {
            val fragment = RegisterFragment()
            router.executeCommand( AddScreenForward{ fragment })
        }
    }

    private fun showProfile(profileData: ProfileItem){
        ScrollViewProfile1.visibility = View.GONE
        ScrollViewProfile2.visibility = View.VISIBLE
        initials.text = "${profileData.name[0]}${profileData.surname[0]}"
        name.text = "${profileData.name}  ${profileData.surname}"
        capital.text = profileData.capital.toString()
        exitButton.setOnClickListener {
            feature.accept(Wish.Exit)
        }
    }

    private fun saveParams(params: ParamsItem){
        mSettings.edit().putString(APP_PREFERENCES_ID, params.id).apply()
        mSettings.edit().putString(APP_PREFERENCES_PASS, params.pass).apply()
        mSettings.edit().putBoolean(APP_PREFERENCES_FLAG, true).apply()
    }

    private fun validate(email: String, pass: String){
        val isEmailValid = email.isEmailValid()
        val isPassValid = pass.length > 6

        if (!isEmailValid){
            mailInputLayout.isErrorEnabled = true
            mailInputLayout.error = "Почтовый адрес введен неверно"
        }

        if (!isPassValid){
            passwordInputLayout.isErrorEnabled = !isPassValid
            passwordInputLayout.error = "Введите больше 6 символов"
        }

        if (isEmailValid && isPassValid) {
            feature.accept(Wish.Authentication)
        } else {
            feature.accept(Wish.ValidationFailed)
        }
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun showError(reason: Int) {
        val error: String = when (reason) {
            0 -> {
                "Ошибка авторизации - необходимо войти повторно."
            }
            1 -> {
                "Неверно введен логин или пароль."
            }
            else -> {
                ""
            }
        }

        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }
}