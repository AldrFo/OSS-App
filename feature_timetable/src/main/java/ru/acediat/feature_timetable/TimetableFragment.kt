package ru.acediat.feature_timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kekmech.ru.common_android.viewbinding.viewBinding
import ru.acediat.feature_timetable.databinding.FragmentTimetableBinding

class TimetableFragment : Fragment() {

    private val binding by viewBinding(FragmentTimetableBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timetable, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() {}
    }
}