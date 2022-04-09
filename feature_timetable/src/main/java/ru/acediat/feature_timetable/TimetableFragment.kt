package ru.acediat.feature_timetable

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_kotlin.OSS_TAG
import kekmech.ru.common_navigation.AddScreenForward
import org.koin.android.ext.android.inject
import ru.acediat.domain_timetable.TimetableRepository
import ru.acediat.domain_timetable.entities.Week
import ru.acediat.feature_timetable.adapters.DaysAdapter
import ru.acediat.feature_timetable.databinding.FragmentTimetableBinding

class TimetableFragment : Fragment() {

    private val binding by viewBinding(FragmentTimetableBinding::bind)
    private val repository : TimetableRepository by inject()
    private val preferences : SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_timetable, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timetableSettingsButton.setOnClickListener(this::onSettingsClick)
        binding.daysViewPager.adapter = DaysAdapter(
            requireContext(),
            getTimetable()!!
        )
        binding.daysTabLayout.setupWithViewPager(binding.daysViewPager)
    }

    private fun onSettingsClick(view : View?) {
        activity?.supportFragmentManager?.let { AddScreenForward { SettingsFragment() }.apply(it) }
    }

    private fun getTimetable() : Week? {
        val group = preferences.getString("timetableGroup", "А-07-20") ?: "А-07-20"
        Log.d(OSS_TAG, group)
        return repository.getWeekTimetable(group)
    }
}