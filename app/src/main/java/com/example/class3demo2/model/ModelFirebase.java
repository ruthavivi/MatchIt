package com.example.class3demo2.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;

public class ModelFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void getAllTeachers(Long since, Model.GetAllTeachersListener listener) {
        db.collection("teachers")
                //.whereEqualTo("is_deleted",false)
                .whereGreaterThanOrEqualTo(Teacher.LAST_UPDATED, new Timestamp(since, 0))

                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinkedList<Teacher> teachersList = new LinkedList<Teacher>();
                if (task.isSuccessful() && task.getResult() != null) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Teacher s = Teacher.fromJson(doc.getData());
                        if (s != null) {
                            teachersList.add(s);
                        }
                    }
                } else {

                }
                listener.onComplete(teachersList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(null);
            }
        });

    }

    public void addTeacher(Teacher teacher, Model.AddTeacherListener listener) {
        db.collection("teachers")
                .document(teacher.getId()).set(teacher.toJson())
                .addOnSuccessListener((successListener) -> {
                    listener.onComplete();
                })
                .addOnFailureListener((e) -> {
                    Log.d("TAG", e.getMessage());
                });
    }

    public void deleteTeacher(Teacher teacher, Model.DeleteTeacherListener listener) {
        db.collection("teachers")
                .document(teacher.getId())
                .set(new HashMap<String,Boolean>(){{ put("is_deleted",true); }}, SetOptions.merge())
                .addOnSuccessListener((successListener) -> {
                    listener.onComplete();
                })
                .addOnFailureListener((e) -> {
                    Log.d("TAG", e.getMessage());
                });
    }

    public void getTeacherById(String teacherId, Model.GetTeacherByIdListener listener) {
        DocumentReference docRef = db.collection("teachers").document(teacherId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Teacher s = Teacher.fromJson(document.getData());
                        listener.onComplete(s);
                    } else {
                        listener.onComplete(null);
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                    listener.onComplete(null);
                }
            }
        });
    }

    public void updateTeacher(Teacher teacher, Model.UpdateTeacherListener listener) {
        DocumentReference docRef = db.collection("teachers").document(teacher.getId());
        docRef.update(teacher.toJson())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        listener.onComplete();
                    }else{
                        listener.onError(task.getException());
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                });



    }

//    public void getTeachersByLocation(String location,Long since, Model.GetTeachersByLocationListener listener) {
//        db.collection("teachers")
//                .whereGreaterThanOrEqualTo(Teacher.LAST_UPDATED, new Timestamp(since, 0))
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                LinkedList<Teacher> teachersList = new LinkedList<Teacher>();
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        Teacher s = Teacher.fromJson(doc.getData());
//                        if (s.location == location) {
//                            teachersList.add(s);
//                        }
//                    }
//                } else {
//
//                }
//                listener.onComplete(teachersList);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                listener.onComplete(null);
//            }
//        });
//
//    }

    public void saveImage(Bitmap bitmap, String name, Model.SaveImageListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("avatar/" + name + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            Uri downloadUrl = uri;
                            listener.onComplete(downloadUrl.toString());
                        }));
    }

}
