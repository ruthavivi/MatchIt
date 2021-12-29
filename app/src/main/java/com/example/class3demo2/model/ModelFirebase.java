package com.example.class3demo2.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ModelFirebase(){
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public void getAllTeachers(Long since, Model.GetAllTeachersListener listener) {
        db.collection("students")
                .whereGreaterThanOrEqualTo(Teacher.LAST_UPDATED,new Timestamp(since, 0))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                LinkedList<Teacher> teachersList = new LinkedList<Teacher>();
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot doc: task.getResult()){
                        Teacher s = Teacher.fromJson(doc.getData());
                        if (s != null) {
                            teachersList.add(s);
                        }
                    }
                }else{

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
        db.collection("students")
                .document(teacher.getId()).set(teacher.toJson())
                .addOnSuccessListener((successListener)-> {
                    listener.onComplete();
                })
                .addOnFailureListener((e)-> {
                    Log.d("TAG", e.getMessage());
                });
    }

    public void getTeacherById(String teacherId, Model.GetTeacherByIdListener listener) {
        DocumentReference docRef = db.collection("students").document(teacherId);
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
}
