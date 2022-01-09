package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Teacher;


public class ContactInfoFragment extends Fragment {


    Teacher teacher;
    TextView nameTv;
    TextView idTv;
    TextView emailTv;
    TextView locationTv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact_info, container, false);
        Button cancel=view.findViewById(R.id.cancel_contact_btn);
        nameTv=view.findViewById(R.id._name_tv);
        idTv=view.findViewById(R.id._id_tv);
        emailTv=view.findViewById(R.id._email_tv);
        locationTv=view.findViewById(R.id._location_tv);



        String teacherId = EditFragmentArgs.fromBundle(getArguments()).getTeacherId();
        Model.instance.getTeacherById(teacherId, (teacher) -> {
            this.teacher = teacher;
            updateDisplay();
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();

            }
        });
        return view;
    }


    private void updateDisplay() {
        nameTv.setText(teacher.getName());
        emailTv.setText(teacher.getEmail());
        idTv.setText(teacher.getId());
        locationTv.setText(teacher.getLocation());

    }
}