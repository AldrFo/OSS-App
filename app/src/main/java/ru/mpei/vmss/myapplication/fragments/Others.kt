package ru.mpei.vmss.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_others.*
import ru.mpei.vmss.myapplication.R
import java.util.*

class Others : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val items = initList()
        val adapter = ArrayAdapter(activity!!.applicationContext, android.R.layout.simple_list_item_1, items)
        othersList!!.adapter = adapter
        return inflater.inflate(R.layout.fragment_others, container, false)
    }

    private fun initList(): List<String> {
        val res: MutableList<String> = ArrayList()
        res.add("Расписание")
        res.add("Документы")
        res.add("О приложении")
        return res
    }
}