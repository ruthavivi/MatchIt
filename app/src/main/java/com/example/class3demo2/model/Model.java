package com.example.class3demo2.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.class3demo2.MyApplication;

import java.util.List;

public class Model {
    public static final Model instance = new Model();

    ModelFirebase modelFirebase = new ModelFirebase();
    private Model(){
        reloadTeachersList();
    }

    public interface GetAllTeachersListener {
        void onComplete(List<Teacher> data);
    }
    public void getAllStudents(GetAllTeachersListener listener){
//        modelFirebase.getAllTeachers(localLastUpdate, listener);
//        MyApplication.executorService.execute(()->{
//            List<Teacher> data = AppLocalDB.db.teacherDao().getAll();
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete(data);
//            });
//        });
    }

    MutableLiveData<List<Teacher>> teachersListLd = new MutableLiveData<List<Teacher>>();
    private void reloadTeachersList() {
        //1. get local last update
        Long localLastUpdate = Teacher.getLocalLastUpdated();
        Log.d("TAG","localLastUpdate: " + localLastUpdate);
        //2. get all students record since local last update from firebase
        modelFirebase.getAllTeachers(localLastUpdate,(list)->{
            MyApplication.executorService.execute(()->{
                //3. update local last update date
                //4. add new records to the local db
                Long lLastUpdate = new Long(0);
                Log.d("TAG", "FB returned " + list.size());
                for(Teacher s : list){
                    AppLocalDB.db.teacherDao().insertAll(s);
                    if (s.getLastUpdated() > lLastUpdate){
                        lLastUpdate = s.getLastUpdated();
                    }
                }
                Teacher.setLocalLastUpdated(lLastUpdate);

                //5. return all records to the caller
                List<Teacher> stList = AppLocalDB.db.teacherDao().getAll();
                teachersListLd.postValue(stList);
            });
        });
    }

    public LiveData<List<Teacher>> getAll(){
        return teachersListLd;
    }

    public interface AddTeacherListener {
        void onComplete();
    }
    public void addTeacher(Teacher teacher, AddTeacherListener listener){
        modelFirebase.addTeacher(teacher,()->{
            reloadTeachersList();
            listener.onComplete();
        });
//        MyApplication.executorService.execute(()->{
//            AppLocalDB.db.teacherDao().insertAll(teacher);
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete();
//            });
//        });
    }

    public interface GetTeacherByIdListener {
        void onComplete(Teacher teacher);
    }
    public void getTeacherById(String teacherId, GetTeacherByIdListener listener) {
        modelFirebase.getTeacherById(teacherId, listener);
//        MyApplication.executorService.execute(()->{
//            Teacher student = AppLocalDB.db.teacherDao().getTeacherById(teacherId);
//            MyApplication.mainHandler.post(()->{
//                listener.onComplete(student);
//            });
//        });
    }
}
