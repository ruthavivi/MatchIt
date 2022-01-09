package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class logInFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_log_in, container, false);

        final EditText email = view.findViewById(R.id.main_email_et);
        final EditText password = view.findViewById(R.id.main_password_et);
        // Inflate the layout for this fragment
//
        Button logIn = view.findViewById(R.id.login);
//        logIn.setOnClickListener(Navigation.createNavigateOnClickListener(logInFragmentDirections.actionGlobalEditFragment()));

        logIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String loginEmail = email.getText().toString();
                String loginPassword = password.getText().toString();

                if (loginEmail.isEmpty() || loginPassword.isEmpty())
                {
                    Toast.makeText(view.getContext(),"please enter your email and password", Toast.LENGTH_SHORT).show();
                }
                else
                    {


                    }

            }
        });

        Button register = view.findViewById(R.id.register);
        register.setOnClickListener(Navigation.createNavigateOnClickListener(logInFragmentDirections.actionGlobalAddTeacherFragment()));


        Button cancel=view.findViewById(R.id.cancel_bt);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();

            }
        });






        return view;
    }
}