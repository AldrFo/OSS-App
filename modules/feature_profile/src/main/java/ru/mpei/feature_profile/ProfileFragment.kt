package ru.mpei.feature_profile

import android.content.SharedPreferences
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

class  ProfileFragment: BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>(){

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
            showError(AUTHORIZATION_ERROR)
        }

        is ProfileEffect.AuthenticationError -> {
            showError(AUTHENTICATION_ERROR)
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

        else -> {}
    }

    private fun showLogin(){
        ScrollViewProfile2.visibility = View.GONE
        ScrollViewProfile1.visibility = View.VISIBLE
        enterButton.setOnClickListener {
            feature.accept(Wish.ValidateFields(loginEmail.text.toString(), loginPassword.text.toString()))
        }
        forgotten_password_text.setOnClickListener {
            val dialog = ProfileDialogFragment()
            dialog.show(parentFragmentManager, "profileDialog")
        }
        registerLink.setOnClickListener {
            val fragment = RegisterFragment()
            router.executeCommand( AddScreenForward{ fragment } )
        }
    }

    private fun showProfile(profileData: ProfileItem){

        ScrollViewProfile1.visibility = View.GONE
        ScrollViewProfile2.visibility = View.VISIBLE

        initials.text = getString(R.string.initials).format(profileData.name[0], profileData.surname[0])
        name.text = getString(R.string.name_blank).format(profileData.name, profileData.surname)
        capital.text = profileData.capital.toString()

        exitButton.setOnClickListener {
            feature.accept(Wish.Exit)
        }

        in_progress_btn.setOnClickListener {
            val fragment = TasksListFragment(TasksType.PROCESS, profileData)
            router.executeCommand( AddScreenForward {fragment} )
        }

        on_check_btn.setOnClickListener {
            val fragment = TasksListFragment(TasksType.CHECK, profileData)
            router.executeCommand( AddScreenForward {fragment} )
        }

        finished_btn.setOnClickListener {
            val fragment = TasksListFragment(TasksType.FINISHED, profileData)
            router.executeCommand( AddScreenForward {fragment} )
        }

        refused_btn.setOnClickListener {
            val fragment = TasksListFragment(TasksType.REFUSED, profileData)
            router.executeCommand( AddScreenForward {fragment} )
        }

    }

    private fun saveParams(params: ParamsItem){
        mSettings.edit().putString(APP_PREFERENCES_ID, params.id).apply()
        mSettings.edit().putString(APP_PREFERENCES_PASS, params.pass).apply()
        mSettings.edit().putBoolean(APP_PREFERENCES_FLAG, true).apply()
    }

    private fun validate(email: String, pass: String){
        val isEmailValid = email.isEmailValid()
        val isPassValid = pass.isNotEmpty()

        if (!isEmailValid){
            mailInputLayout.isErrorEnabled = true
            mailInputLayout.error = "Почтовый адрес введен неверно"
        }

        if (!isPassValid){
            passwordInputLayout.isErrorEnabled = !isPassValid
            passwordInputLayout.error = "Введите пароль"
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
        when (reason) {
            0 -> {
                Toast.makeText(context, "Ошибка авторизации - необходимо войти повторно.", Toast.LENGTH_LONG).show()
            }
            1 -> {
                Toast.makeText(context, "Неверно введен логин или пароль.", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    companion object {
        const val APP_PREFERENCES_FLAG = "isAuth"
        const val APP_PREFERENCES_PASS = "userPass"
        const val APP_PREFERENCES_ID = "userId"

        const val AUTHORIZATION_ERROR = 0
        const val AUTHENTICATION_ERROR = 1
    }
}