package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class FirstPageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_first_page, container, false);
        // Inflate the layout for this fragment
        Button sub_teacher=view.findViewById(R.id.subTeacher);
        sub_teacher.setOnClickListener(Navigation.createNavigateOnClickListener(FirstPageFragmentDirections.actionFirstPageFragmentToLogInFragment()));

        Button teacherList_btn = view.findViewById(R.id.teacherList_bt);
        teacherList_btn.setOnClickListener(Navigation.createNavigateOnClickListener(FirstPageFragmentDirections.actionGlobalTeachersListFragment()));

        return view;
    }
}