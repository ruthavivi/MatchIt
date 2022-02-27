package com.example.class3demo2.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.class3demo2.MyApplication;

@Database(entities = {Teacher.class}, version = 33)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract TeacherDao teacherDao();
}

public class AppLocalDB {
    static public final AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
    private AppLocalDB(){}
}

