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
import android.widget.Toast;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Teacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class EditFragment extends Fragment {
    EditText nameEt;
    EditText passwordEt;
    EditText emailEt;
    EditText locationEt;
    CheckBox cb;
    View view;
    Button saveBtn;
    Button cancelBtn;
    Button deleteBtn;
    Button logoutBtn;
    Teacher teacher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit, container, false);
        saveBtn = view.findViewById(R.id.save_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        deleteBtn = view.findViewById(R.id.delete_btn);
        logoutBtn = view.findViewById(R.id.logout_editF);
        nameEt = view.findViewById(R.id.edit_name_et);
        passwordEt = view.findViewById(R.id.edit_password_et);
        locationEt = view.findViewById(R.id.edit_location_et);
        emailEt = view.findViewById(R.id.edit_email_et);
        cb = view.findViewById(R.id.main_cb);


        String teacherId = EditFragmentArgs.fromBundle(getArguments()).getTeacherId();
        Model.instance.getTeacherById(teacherId, (teacher) -> {
            this.teacher = teacher;
            updateDisplay();
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(view).navigate(R.id.action_editFragment_pop);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                Navigation.findNavController(view).navigate(R.id.action_editFragment_pop);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.instance.deleteTeacher(teacher, () -> {
                    //Navigation.findNavController(view).navigateUp();
                    Navigation.findNavController(view).navigate(R.id.action_editFragment_pop);

                });

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });


        return view;

    }



    private void save() {
        teacher.updateTeacher(
                nameEt.getText().toString(),
                cb.isChecked(),
                emailEt.getText().toString(),
                passwordEt.getText().toString(),
                locationEt.getText().toString());


        Model.instance.updateTeacher(teacher, new Model.UpdateTeacherListener() {
            @Override
            public void onComplete() {
                Navigation.createNavigateOnClickListener(EditFragmentDirections.actionGlobalTeachersListFragment());
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Failed to update teacher: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateDisplay() {
        nameEt.setText(teacher.getName());
        emailEt.setText(teacher.getEmail());
        passwordEt.setText(teacher.getPassword());
        locationEt.setText(teacher.getLocation());
        cb.setChecked(teacher.isFlag());
    }


}