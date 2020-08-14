package ru.mpei.vmss.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.mpei.vmss.myapplication.R

class User : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Companion.fragmentManager = activity!!.supportFragmentManager
        fragmentManager!!.beginTransaction().add(R.id.container, Login()).commit()
        updateLayout()
    }

    companion object {
        private var fragmentManager: FragmentManager? = null
        var isAuth = false
        var hashPass: String? = null
        var userId: String? = null
        fun isAuth(`is`: Boolean, pass: String?, id: String?) {
            isAuth = `is`
            hashPass = pass
            userId = id
            fragmentManager!!.beginTransaction().replace(R.id.container, Profile(hashPass, userId)).commit()
        }

        fun updateLayout() {
            if (isAuth) {
                fragmentManager!!.beginTransaction().replace(R.id.container, Profile(hashPass, userId)).commit()
            } else {
                fragmentManager!!.beginTransaction().replace(R.id.container, Login()).commit()
            }
        }
    }
}