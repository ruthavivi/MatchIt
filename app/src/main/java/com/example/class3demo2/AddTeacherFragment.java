package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Teacher;


public class AddTeacherFragment extends Fragment {

    EditText nameEt;
    EditText passwordEt;
    EditText idEt;
    EditText emailEt;
    CheckBox cb;
    View view;
    ProgressBar progressbar;
    Button saveBtn;
    Button cancelBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_teacher, container, false);

        nameEt = view.findViewById(R.id.main_name_et);
        passwordEt = view.findViewById(R.id.main_password_et);
        idEt = view.findViewById(R.id.main_id_et);
        emailEt = view.findViewById(R.id.main_email_et);
        cb = view.findViewById(R.id.main_cb);
        progressbar = view.findViewById(R.id.main_progressbar);
        progressbar.setVisibility(View.GONE);

        saveBtn = view.findViewById(R.id.main_save_btn);
        cancelBtn = view.findViewById(R.id.main_cancel_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        return view;

    }

    private void save() {
        progressbar.setVisibility(View.VISIBLE);
        saveBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        String name = nameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String id = idEt.getText().toString();
        String email = emailEt.getText().toString();
        boolean flag = cb.isChecked();
        Log.d("TAG","saved name:" + name + " id:" + id +" email:" + email+" password:" + password+  " flag:" + flag);
        Teacher st = new Teacher(name,id,flag,email,password);
        Model.instance.addTeacher(st,()->{
            Navigation.findNavController(view).navigateUp();
        });
    }
}