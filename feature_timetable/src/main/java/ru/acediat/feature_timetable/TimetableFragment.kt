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
import org.koin.android.ext.android.inject
import ru.acediat.domain_timetable.TimetableRepository
import ru.acediat.feature_timetable.adapters.DaysAdapter
import ru.acediat.feature_timetable.databinding.FragmentTimetableBinding
import ru.acediat.feature_timetable.models.TimetableViewModel

class TimetableFragment : Fragment() {

    private val binding by viewBinding(FragmentTimetableBinding::bind)
    private val repository : TimetableRepository by inject()
    private val preferences : SharedPreferences by inject()

    private lateinit var model : TimetableViewModel
    private lateinit var daysAdapter: DaysAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_timetable, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = TimetableViewModel(activity, repository, preferences)

        daysAdapter = DaysAdapter(requireContext(), preferences)
        binding.timetableSettingsButton.setOnClickListener(this::onSettingsClick)
        binding.daysViewPager.adapter = daysAdapter
        binding.daysTabLayout.setupWithViewPager(binding.daysViewPager)

        model.week.observe(viewLifecycleOwner){
            daysAdapter.setWeek(it)
        }

        model.group.observe(viewLifecycleOwner){
            preferences.edit().putString("timetableGroup", it).commit()
            model.getTimetable()
        }

        model.getGroup()
    }

    private fun onSettingsClick(view : View?) = model.goToSettings()
}