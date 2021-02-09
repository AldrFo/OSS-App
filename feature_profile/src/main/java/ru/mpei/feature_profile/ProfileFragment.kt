package ru.mpei.feature_profile

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.Router
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ParamsItem
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.Wish
import java.util.*

class  ProfileFragment: BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>(){

    private val APP_PREFERENCES_FLAG = "isAuth"
    private val APP_PREFERENCES_PASS = "hashCode"
    private val APP_PREFERENCES_ID = "userId"

    private val mSettings: SharedPreferences by inject()
    private val router: Router by inject()

    override val initEvent: ProfileEvent get() = when( mSettings.getBoolean(APP_PREFERENCES_FLAG, false)){
        true -> {
            Wish.System.InitProfile(mSettings.getString(APP_PREFERENCES_ID, "0")!!, mSettings.getString(APP_PREFERENCES_PASS, "")!!)
        }
        false -> Wish.System.InitLogin
    }

    private  val profileFeatureFactory: ProfileFeatureFactory by inject()

    override fun createFeature(): ProfileFeature = profileFeatureFactory.create()

    override var layoutId: Int = when(mSettings.getBoolean(APP_PREFERENCES_FLAG, false)) {
        true -> R.layout.fragment_profile
        false -> R.layout.fragment_login
    }

    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        when(mSettings.getBoolean(APP_PREFERENCES_FLAG, false)) {
            false -> {
                enterButton.setOnClickListener {
                    if (Objects.requireNonNull(loginEmail.text).toString().trim { it <= ' ' }.isEmpty() || Objects.requireNonNull(loginPassword.text).toString().trim { it <= ' ' }.isEmpty()) {
                        Toast.makeText(context, "Проверьте заполнение полей", Toast.LENGTH_LONG).show()
                    } else {
                        feature.accept(Wish.LogIn(loginEmail.text.toString(), loginPassword.text.toString()))
                    }
                }
            }
            true -> {

            }
        }
    }

    override fun render(state: ProfileState) {
        if (mSettings.getBoolean(APP_PREFERENCES_FLAG, false)){
            initials.text = "${state.profileData.name[0]}${state.profileData.surname[0]}"
            name.text = "${state.profileData.name}  ${state.profileData.surname}"
            capital.text = state.profileData.capital.toString()
        }
    }

    override fun handleEffect(effect: ProfileEffect) = when(effect) {
        is ProfileEffect.ShowError ->
            if (effect.throwable.message!!.contains("401")){
                Toast.makeText(context, "Неверный логин или пароль", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Ошибка авторизации, попробуйте еще раз позже", Toast.LENGTH_LONG).show()
            }

        is ProfileEffect.LogIn -> {
            saveParams(effect.params)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveParams(params: ParamsItem){
        mSettings.edit().putString(APP_PREFERENCES_ID, params.id).apply()
        mSettings.edit().putString(APP_PREFERENCES_PASS, params.pass).apply()
        mSettings.edit().putBoolean(APP_PREFERENCES_FLAG, true).apply()
    }
}