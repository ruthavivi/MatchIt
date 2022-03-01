package com.example.class3demo2;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class EditFragment extends Fragment {
    String photoUrl;
    EditText nameEt;
    EditText passwordEt;
    EditText emailEt;
    EditText locationEt;
    EditText phoneEt;
    View view;
    Button saveBtn;
    Button cancelBtn;
    Button deleteBtn;
    Button logoutBtn;
    Teacher teacher;
    Bitmap bitmap;
    ImageView avatarImg;
    private static final int REQUEST_IMAGE_CAPTURE = 1;



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
        phoneEt = view.findViewById(R.id.edit_phone_et);
        emailEt = view.findViewById(R.id.edit_email_et);
        avatarImg = view.findViewById(R.id.teacherEdit_avatar_img);



        String teacherId = EditFragmentArgs.fromBundle(getArguments()).getTeacherId();
        Model.instance.getTeacherById(teacherId, (teacher) -> {
            this.teacher = teacher;
            updateDisplay();
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
                teacher.setDeleted(true);
                Model.instance.deleteTeacher(teacher, () -> {
                    FirebaseAuth.getInstance().signOut();

                    Navigation.findNavController(view).navigate(R.id.action_editFragment_pop);
                });

            }
        });



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(view).navigate(R.id.action_editFragment_pop);
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
            avatarImg.setImageBitmap(bitmap);
        }
    }






    private void save() {
        teacher.updateTeacher(
                nameEt.getText().toString(),
                emailEt.getText().toString(),
                passwordEt.getText().toString(),
                locationEt.getText().toString(),
                phoneEt.getText().toString()

        );





        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (bitmap == null) {
            Model.instance.addTeacher(teacher, () -> {

            });
        } else {
            Model.instance.saveImage(bitmap, user.getUid(), url -> {
                teacher.setAvatarUtl(url);
                Model.instance.addTeacher(teacher, () -> {

                });

            });
        }









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

        user.updateEmail(teacher.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });
        user.updatePassword(teacher.getPassword())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });


















    }

    private void updateDisplay() {
        nameEt.setText(teacher.getName());
        emailEt.setText(teacher.getEmail());
        passwordEt.setText(teacher.getPassword());
        locationEt.setText(teacher.getLocation());
        phoneEt.setText(teacher.getPhone());
        if (teacher.getAvatarUtl() != null) {
            Picasso.get()
                    .load(teacher.getAvatarUtl())
                    .placeholder(R.drawable.avatar)
                    .into(avatarImg);
        }
    }


}