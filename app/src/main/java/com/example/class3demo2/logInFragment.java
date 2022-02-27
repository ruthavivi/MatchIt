package com.example.class3demo2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;


public class logInFragment extends Fragment {
    Button logInBTN;
    Button cancelBtn;
    EditText emailET;
    EditText passwordET;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_log_in, container, false);

        emailET = view.findViewById(R.id.main_email_et);
        passwordET = view.findViewById(R.id.main_password_et);
        mAuth = FirebaseAuth.getInstance();


        logInBTN = view.findViewById(R.id.login);
        logInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });

        Button register = view.findViewById(R.id.register);
        register.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_logInFragment_to_regristerFragment));

        cancelBtn=view.findViewById(R.id.cancel_bt);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack(); }
        });


        return view;
    }

    private void login(View view)
    {
        //progressbar.setVisibility(View.VISIBLE);
        logInBTN.setEnabled(false);

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        if (password.length()==0 || email.length()==0)
        {
            logInBTN.setEnabled(true);
            Toast.makeText(getContext(), "Authentication failed. Please fill all the fields.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d("loginFragment", "signInWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if(getContext()!= null){
                                Toast.makeText(getContext(), "signIn User success.",
                                        Toast.LENGTH_SHORT).show();

                                logInFragmentDirections.ActionLogInFragmentToEditFragment action = logInFragmentDirections.actionLogInFragmentToEditFragment(currentUser.getUid());
                                Navigation.findNavController(view).navigate(action);
                            }

                        }else {
                            Log.w("loginFragment", "signInWithEmail:failure", task.getException());
                            if(getContext()!= null){
                                Toast.makeText(getContext(), "Authentication failed."+task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                logInBTN.setEnabled(true);
                            }
                        }
                    }
                });

    }

}
