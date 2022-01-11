package com.example.class3demo2;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.auth.FirebaseAuth;


public class RegristerFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
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
    ImageView avatar;
    Bitmap bitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_regrister, container, false);
        avatar = view.findViewById(R.id.main_avatar_imv);

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
                save();
            }
        });

        cancelBtn = view.findViewById(R.id.main_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        ImageButton cameraBtn = view.findViewById(R.id.main_camera_btn);
        cameraBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            bitmap = (Bitmap) bundle.get("data");
            avatar.setImageBitmap(bitmap);
        }
    }


    private void register() {
        progressbar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        String name = nameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String location = locationEt.getText().toString();
        String id = idEt.getText().toString();
        String email = emailEt.getText().toString();
        boolean flag = cb.isChecked();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("RegisterFragment", "signInWithEmail:success");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    insertUser(user.getUid(), user.getEmail(), name,location,flag);
                } else {
                    progressbar.setVisibility(View.GONE);
                    // If sign in fails, display a message to the user.
                    Log.w("RegisterFragment", "signInWithEmail:failure", task.getException());
                    if(getContext()!= null){
                    Toast.makeText(getContext(), "Authentication failed."+task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


//        Log.d("TAG","saved name:" + name + " id:" + id +" email:" + email+" password:" + password+"location"+location+  " flag:" + flag);
//        Teacher st = new Teacher(name,id,flag,email,password,location);
//        Model.instance.addTeacher(st,()->{
//            Navigation.findNavController(view).navigateUp();
//        });

    }

    private void insertUser(String userUid, String email, String name, String location, boolean cb) {
        Teacher teacher = new Teacher(name, userUid, cb, email, "", location);
        FirebaseFirestore.getInstance().collection("teachers").document(userUid).set(teacher.toJson())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressbar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("RegisterFragment", "insertUser: success");
                            if(getContext()!= null){
                                Toast.makeText(getContext(), "insertUser success.",
                                        Toast.LENGTH_SHORT).show();
                            }



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("RegisterFragment", "insertUser:failure", task.getException());
                            if(getContext()!= null){
                            Toast.makeText(getContext(), "insertUser failed.",
                                    Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }


    private void save() {
        progressbar.setVisibility(View.VISIBLE);
        registerBtn.setEnabled(false);
        cancelBtn.setEnabled(false);

        String name = nameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String location = locationEt.getText().toString();
        String id = idEt.getText().toString();
        String email = emailEt.getText().toString();
        boolean flag = cb.isChecked();
        Log.d("TAG", "saved name:" + name + " id:" + id + " email:" + email + " password:" + password + "location" + location + " flag:" + flag);
        Teacher st = new Teacher(name, id, flag, email, password, location);
        if (bitmap == null) {
            Model.instance.addTeacher(st, () -> {
                Navigation.findNavController(view).navigateUp();
            });
        } else {
            Model.instance.saveImage(bitmap, id, url -> {
                st.setAvatarUtl(url);
                Model.instance.addTeacher(st, () -> {
                    Navigation.findNavController(view).navigateUp();
                });
            });
        }
//        Model.instance.addTeacher(st, () -> {
//            Navigation.findNavController(view).navigateUp();
//        });
    }
}