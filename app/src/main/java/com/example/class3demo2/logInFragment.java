package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class logInFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_log_in, container, false);
        // Inflate the layout for this fragment
//
//        Button logIn = view.findViewById(R.id.login);
//        logIn.setOnClickListener(Navigation.createNavigateOnClickListener(logInFragmentDirections.actionGlobalEditFragment()));

        Button register = view.findViewById(R.id.register);
        register.setOnClickListener(Navigation.createNavigateOnClickListener(logInFragmentDirections.actionGlobalAddTeacherFragment()));

        Button cancel=view.findViewById(R.id.search_bt);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();

            }
        });






        return view;
    }
}