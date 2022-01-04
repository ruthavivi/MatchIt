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

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Teacher;


public class EditFragment extends Fragment {
    EditText nameEt;
    EditText idEt;
    EditText passwordEt;
    EditText emailEt;
    CheckBox cb;
    View view;
    Button saveBtn;
    Button cancelBtn;
    Button deleteBtn;
    Teacher teacher;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_edit, container, false);
        saveBtn = view.findViewById(R.id.save_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        deleteBtn = view.findViewById(R.id.delete_btn);
        nameEt=view.findViewById(R.id.edit_name_et);
        idEt=view.findViewById(R.id.edit_id_et);
        passwordEt=view.findViewById(R.id.edit_password_et);
        emailEt=view.findViewById(R.id.edit_email_et);

        String TeacherId = EditFragmentArgs.fromBundle(getArguments()).getTeacherId();
        Model.instance.getTeacherById(TeacherId, (teacher)->{
            updateDisplay(teacher);
        });






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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance.deleteTeacher(teacher,()->{
                    //Navigation.findNavController(view).navigateUp();
                    Navigation.findNavController(view).navigate(R.id.action_editFragment_pop);

                });

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

    private void updateDisplay(Teacher teacher) {
        this.teacher = teacher;


    }








}