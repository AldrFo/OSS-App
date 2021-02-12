package ru.mpei.feature_profile

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.popup_reset_password.view.*

class ProfileDialogFragment : DialogFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = layoutInflater.inflate(R.layout.popup_reset_password, container)
        rootView.btn_send_request.setOnClickListener {
            rootView.email_input.error = "Почтовый адрес введен неверно"
            if (!rootView.resetPasswordEmail.text.toString().isEmailValid()) {
                rootView.email_input.isErrorEnabled = true
            } else {
                rootView.email_input.isErrorEnabled = false
                rootView.email_send_frame.visibility = View.GONE
                rootView.ok_frame.visibility = View.VISIBLE
                rootView.popup_emailSent_message.text = getString(R.string.reset_password_text_blank).replace("example@mail.ru", rootView.resetPasswordEmail.text.toString(), true)
            }
        }
        rootView.btn_cancel.setOnClickListener { dismiss() }
        return rootView
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

}