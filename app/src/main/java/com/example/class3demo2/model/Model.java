package com.example.class3demo2.model;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.class3demo2.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    ModelFirebase modelFirebase = new ModelFirebase();

    private Model() {
        reloadTeachersList();
    }

    public interface GetAllTeachersListener {
        void onComplete(List<Teacher> data);
    }

    public interface GetTeachersByLocationListener {
        void onComplete(List<Teacher> data);
    }


    MutableLiveData<List<Teacher>> teachersListLd = new MutableLiveData<List<Teacher>>();

    public void reloadTeachersList() {
        //1. get local last update
        Long localLastUpdate = Teacher.getLocalLastUpdated();
        Log.d("TAG", "localLastUpdate: " + localLastUpdate);
        //2. get all students record since local last update from firebase
        modelFirebase.getAllTeachers(localLastUpdate, (list) -> {
            MyApplication.executorService.execute(() -> {
                //3. update local last update date
                //4. add new records to the local db

                if (list != null && list.size() > 0) {
                    Log.d("TAG", "FB returned " + list.size());
                    Long lLastUpdate = 0L;
                    for (Teacher s : list) {
                        AppLocalDB.db.teacherDao().insertAll(s);
                        if (s.getLastUpdated() > lLastUpdate) {
                            lLastUpdate = s.getLastUpdated();
                        }
                    }
                    Teacher.setLocalLastUpdated(lLastUpdate);

                    //5. return all records to the caller
                    List<Teacher> stList = AppLocalDB.db.teacherDao().getAllActiveTeachers();
                    teachersListLd.postValue(stList);
                }

                else {
                    teachersListLd.postValue(new ArrayList<>());
                }


            });
        });
    }

    public LiveData<List<Teacher>> getAll() {
        return teachersListLd;
    }


    public interface DeleteTeacherListener {
        void onComplete();
    }

    public void deleteTeacher(Teacher teacher, DeleteTeacherListener listener) {
        modelFirebase.deleteTeacher(teacher, () -> {
            MyApplication.executorService.execute(() -> {
                AppLocalDB.db.teacherDao().updateTeacher(teacher);
                MyApplication.mainHandler.post(() -> {
                    reloadTeachersList();
                    listener.onComplete();
                });
            });
        });

    }

    public interface GetTeacherByIdListener {
        void onComplete(Teacher teacher);
    }

    public void getTeacherById(String teacherId, GetTeacherByIdListener listener) {
        MyApplication.executorService.execute(() -> {
            Teacher teacher = AppLocalDB.db.teacherDao().getTeacherById(teacherId);
            MyApplication.mainHandler.post(() -> {
                if (teacher != null) {
                    listener.onComplete(teacher);
                    return;
                }
                modelFirebase.getTeacherById(teacherId, listener);
            });
        });
    }

    public interface AddTeacherListener {
        void onComplete();
    }

    public void addTeacher(Teacher teacher, AddTeacherListener listener) {
        modelFirebase.addTeacher(teacher, () -> {
            reloadTeachersList();
            listener.onComplete();
        });

    }

    public interface UpdateTeacherListener {
        void onComplete();

        void onError(Exception e);
    }

    public void updateTeacher(Teacher teacher, UpdateTeacherListener listener) {

        modelFirebase.updateTeacher(teacher, new UpdateTeacherListener() {
            @Override
            public void onComplete() {
                MyApplication.executorService.execute(() -> {
                    AppLocalDB.db.teacherDao().updateTeacher(teacher);
                    MyApplication.mainHandler.post(() -> {
                        reloadTeachersList();
                        listener.onComplete();
                    });
                });
            }

            @Override
            public void onError(Exception e) {
                listener.onError(e);
            }
        });


    }


    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap bitmap, String name, SaveImageListener listener) {
        modelFirebase.saveImage(bitmap, name, listener);
    }
}
