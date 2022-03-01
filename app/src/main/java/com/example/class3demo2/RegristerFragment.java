package com.example.class3demo2;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    EditText phoneEt;
    View view;
    ProgressBar progressbar;
    Button registerBtn;
    TextView loginNowBtn;
    Button cancelBtn;
    ImageView avatar;
    Bitmap bitmap;
    Boolean flag=true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_regrister, container, false);
        avatar = view.findViewById(R.id.teacherEdit_avatar_img);

        nameEt = view.findViewById(R.id.main_name_et);
        passwordEt = view.findViewById(R.id.main_password_et);
        idEt = view.findViewById(R.id.main_id_et);
        emailEt = view.findViewById(R.id.main_email_et);
        locationEt = view.findViewById(R.id.main_location_et);
        phoneEt=view.findViewById(R.id.main_phone_et);
        progressbar = view.findViewById(R.id.main_progressbar);
        progressbar.setVisibility(View.GONE);



        registerBtn = view.findViewById(R.id.main_register2_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register();
                //save();
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
        String phone = phoneEt.getText().toString();

        if (password.length()==0 || email.length()==0||name.length()==0||location.length()==0||phone.length()==0)
        {
            registerBtn.setEnabled(true);
            Toast.makeText(getContext(), "Authentication failed. Please fill all the fields.",
                    Toast.LENGTH_SHORT).show();
            progressbar.setVisibility(View.GONE);
            return;

        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // registration success, update UI with the signed-in user's information
                    Log.d("RegisterFragment", "createUserWithEmail:success");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                    insertUser(user.getUid(), user.getEmail(), name,location,password,phone);
                } else {
                    progressbar.setVisibility(View.GONE);
                    // If registration fails, display a message to the user.
                    Log.w("RegisterFragment", "createUserWithEmail:failure", task.getException());
                    if(getContext()!= null){
                    Toast.makeText(getContext(), "Authentication failed."+task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                        registerBtn.setEnabled(true);
                    }
                }

            }
        });




    }

    private void insertUser(String userUid, String email, String name, String location,String password,String phone) {
        Teacher teacher;
        teacher= new Teacher(name, userUid, email, password, location,phone,false);


         if (bitmap == null) {
            bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.avatar);
            avatar.setImageBitmap(bitmap);

            Model.instance.saveImage(bitmap, userUid, url -> {
                teacher.setAvatarUtl(url);
                Model.instance.addTeacher(teacher, () -> {
                    Navigation.findNavController(view).navigateUp();
                });
            });



        } else {
            //flag=true;

            Model.instance.saveImage(bitmap, userUid, url -> {
                teacher.setAvatarUtl(url);
                Model.instance.addTeacher(teacher, () -> {
                    Navigation.findNavController(view).navigateUp();
                });
            });
        }

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
                                registerBtn.setEnabled(true);
                            }
                        }

                    }
                });




    }



}