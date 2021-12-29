package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;


public class EditFragment extends Fragment {
    EditText nameEt;
    EditText idEt;
    CheckBox cb;
    View view;
    ProgressBar progressbar;
    Button saveBtn;
    Button cancelBtn;
    Button deleteBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_edit, container, false);
        saveBtn = view.findViewById(R.id.save_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        deleteBtn = view.findViewById(R.id.delete_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });





        return view;

    }

    private void cancel() {
        Navigation.findNavController(view).popBackStack();
    }

    private void save() {


        Navigation.createNavigateOnClickListener(EditFragmentDirections.actionGlobalSearchFragment());

    }








}