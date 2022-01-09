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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Teacher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.auth.FirebaseAuth;


public class RegristerFragment extends Fragment {

    //private FirebaseAuth mAuth;
    EditText nameEt;
    EditText passwordEt;
    EditText idEt;
    EditText emailEt;
    EditText locationEt;
    CheckBox cb;
    View view;
    ProgressBar progressbar;
    Button registerBtn;
    TextView loginNowBtn;
    Button cancelBtn;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://matchitapp-4d472-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_regrister, container, false);

        nameEt = view.findViewById(R.id.main_name_et);
        passwordEt = view.findViewById(R.id.main_password_et);
        idEt = view.findViewById(R.id.main_id_et);
        emailEt = view.findViewById(R.id.main_email_et);
        locationEt = view.findViewById(R.id.main_location_et);
        cb = view.findViewById(R.id.main_cb);
        progressbar = view.findViewById(R.id.main_progressbar);
        progressbar.setVisibility(View.GONE);


        registerBtn = view.findViewById(R.id.main_register2_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        cancelBtn = view.findViewById(R.id.main_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        return view;
    }


    private void register()
    {
        progressbar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        String name = nameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String location = locationEt.getText().toString();
        String id = idEt.getText().toString();
        String email = emailEt.getText().toString();
        boolean flag = cb.isChecked();

        if (name.isEmpty() || password.isEmpty() ||location.isEmpty()||id.isEmpty()||email.isEmpty())
        {
            Toast.makeText(view.getContext(),"Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //check if the id is not registered before
                    if (snapshot.hasChild(id)){
                        Toast.makeText(view.getContext(),"ID is already registered", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //sending data to firebase Realtime Database
                        //we are using id as unique identity of every user
                        databaseReference.child("users").child(id).child("name").setValue(name);
                        databaseReference.child("users").child(id).child("email").setValue(email);
                        databaseReference.child("users").child(id).child("password").setValue(password);
                        databaseReference.child("users").child(id).child("location").setValue(location);

                        Toast.makeText(view.getContext(),"User registered successfully.", Toast.LENGTH_SHORT).show();
                        //finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

//        Log.d("TAG","saved name:" + name + " id:" + id +" email:" + email+" password:" + password+"location"+location+  " flag:" + flag);
//        Teacher st = new Teacher(name,id,flag,email,password,location);
//        Model.instance.addTeacher(st,()->{
//            Navigation.findNavController(view).navigateUp();
//        });

    }


}