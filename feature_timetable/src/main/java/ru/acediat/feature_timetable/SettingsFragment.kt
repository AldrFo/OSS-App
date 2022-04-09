package ru.acediat.feature_timetable

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kekmech.ru.common_android.viewbinding.viewBinding
import org.koin.android.ext.android.inject
import ru.acediat.feature_timetable.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val preferences : SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.groupEditText.setText(loadGroup())
        binding.backButton.setOnClickListener(this::onBackClick)
        binding.saveButton.setOnClickListener(this::onSaveClick)
    }

    private fun isGroupValid() : Boolean = binding.groupEditText.text.toString().matches(
        Regex("""[А-Я]{1,2}+-+[0-9]{1,2}+-+[0-9]{2}""")
    )

    private fun loadGroup() : String = preferences.getString("timetableGroup", "") ?: ""

    private fun saveGroup() = preferences.edit()
            .putString("timetableGroup", binding.groupEditText.text.toString())
            .commit()

    private fun onBackClick(view : View?){
        activity?.onBackPressed()
    }

    private fun onSaveClick(view : View?) {
        if(isGroupValid())
            saveGroup()
        else
            Toast.makeText(context, "Некорректный ввод группы!", Toast.LENGTH_SHORT).show()
    }
}