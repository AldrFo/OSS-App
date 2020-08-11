package ru.mpei.vmss.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.mpei.vmss.myapplication.R;

public class User extends Fragment {

    private static FragmentManager fragmentManager;
    public static Boolean isAuth = false;
    public static String hashPass;
    public static String userId;


    public User() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.container, new Login()).commit();
        updateLayout();

        return rootView;
    }

    static void isAuth(boolean is, String pass, String id){
        isAuth = is;
        hashPass = pass;
        userId = id;
        fragmentManager.beginTransaction().replace(R.id.container, new Profile(hashPass, userId)).commit();
    }

    public static void updateLayout(){
        if (isAuth){
            fragmentManager.beginTransaction().replace(R.id.container, new Profile(hashPass, userId)).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.container, new Login()).commit();
        }
    }

}
