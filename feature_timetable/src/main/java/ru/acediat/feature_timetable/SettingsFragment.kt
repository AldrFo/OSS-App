package ru.acediat.feature_timetable

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kekmech.ru.common_android.KeyboardUtils
import kekmech.ru.common_android.viewbinding.viewBinding
import org.koin.android.ext.android.inject
import ru.acediat.domain_timetable.TimetableRepository
import ru.acediat.feature_timetable.databinding.FragmentSettingsBinding
import ru.acediat.feature_timetable.models.SettingsViewModel

class SettingsFragment : Fragment() {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val preferences : SharedPreferences by inject()
    private val repository : TimetableRepository by inject()

    private val model = SettingsViewModel(preferences, repository)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.groupEditText.setText(model.loadGroup())
        binding.backButton.setOnClickListener(this::onBackClick)
        binding.saveButton.setOnClickListener(this::onSaveClick)
    }

    private fun onBackClick(view : View?) = activity?.onBackPressed()

    private fun onSaveClick(view : View?) {
        with(binding.groupEditText.text.toString()) {
            model.isGroupValid(requireContext(),this)
            if (model.isGroupValid) {
                model.saveGroup(this)
                KeyboardUtils.hideSoftInput(binding.groupEditText)
                onBackClick(null)
            } else if (!model.wasError){
                model.showToast(requireContext(),"Неккоректный ввод группы!")
            }else{}
        }
    }
}