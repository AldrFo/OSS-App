package ru.acediat.feature_timetable

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kekmech.ru.common_android.viewbinding.viewBinding
import org.koin.android.ext.android.inject
import ru.acediat.domain_timetable.TimetableRepository
import ru.acediat.feature_timetable.databinding.FragmentTimetableBinding

class TimetableFragment : Fragment() {

    private val binding by viewBinding(FragmentTimetableBinding::bind)
    private val repository : TimetableRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timetable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.daysViewPager.adapter = DaysAdapter(requireContext(), repository.getWeekTimetable("А-07-20")!!)
        binding.daysTabLayout.setupWithViewPager(binding.daysViewPager)
    }

    companion object {
        @JvmStatic
        fun newInstance() {}
    }
}