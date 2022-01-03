package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Teacher;

public class TeacherDetailsFragment extends Fragment {
    Teacher teacher;
    TextView nameTv;
    TextView emailTv;
    TextView passwordTv;
    TextView idTv;
    ProgressBar progressBar;
    Button machIt;
    Button cancel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_details, container, false);


        nameTv = view.findViewById(R.id.studentdetails_name_tv);
        passwordTv = view.findViewById(R.id.studentdetails_password_tv);
        emailTv = view.findViewById(R.id.studentdetails_email_tv);
        idTv = view.findViewById(R.id.studentdetails_id_tv);
        progressBar = view.findViewById(R.id.studentdetails_progressbar);
        progressBar.setVisibility(View.VISIBLE);
        machIt=view.findViewById(R.id.matchIt_btn);
        cancel=view.findViewById(R.id.cancel_btn);

        String studentId = TeacherDetailsFragmentArgs.fromBundle(getArguments()).getStudentId();
        Model.instance.getTeacherById(studentId, (student)->{
            updateDisplay(student);
        });

        if (teacher != null){
            updateDisplay(teacher);
        }


        machIt.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_teacherDetailsFragment_to_contactInfoFragment));
        Button cancel=view.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();

            }
        });
        return view;
    }

    private void updateDisplay(Teacher teacher) {
        this.teacher = teacher;
        nameTv.setText(teacher.getName());
        emailTv.setText(teacher.getEmail());
        passwordTv.setText(teacher.getPassword());
        idTv.setText(teacher.getId());
        progressBar.setVisibility(View.GONE);
    }
}