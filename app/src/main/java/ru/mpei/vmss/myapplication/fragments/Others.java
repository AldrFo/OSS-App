package ru.mpei.vmss.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.mpei.vmss.myapplication.R;

public class Others extends Fragment {

    public Others() {
        // Required empty public constructor
    }

    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_others, container, false);

        list = (ListView) rootView.findViewById(R.id.othersList);

        List<String> items = initList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, items);

        list.setAdapter(adapter);
        return rootView;
    }

    private List<String> initList(){
        List<String> res = new ArrayList<String>();

        res.add("Расписание");
        res.add("Документы");
        res.add("О приложении");

        return res;
    }
}
