package ru.acediat.feature_timetable

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentStatePagerAdapter
import kekmech.ru.common_android.viewbinding.viewBinding
import ru.acediat.feature_timetable.databinding.FragmentTimetableBinding

class TimetableFragment : Fragment() {

    private val binding by viewBinding(FragmentTimetableBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timetable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.daysViewPager.adapter = DaysAdapter(requireContext())
        binding.daysTabLayout.setupWithViewPager(binding.daysViewPager)
    }

    companion object {
        @JvmStatic
        fun newInstance() {}
    }
}