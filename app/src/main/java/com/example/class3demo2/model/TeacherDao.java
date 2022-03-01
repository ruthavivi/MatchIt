package com.example.class3demo2.model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TeacherDao {
    @Query("select * from Teacher")
    List<Teacher> getAll();

    @Query("select * from Teacher where `isDeleted` = 0")
    List<Teacher> getAllActiveTeachers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Teacher... teachers);

    @Delete
    void delete(Teacher teacher);

    @Update
    void updateTeacher(Teacher teacher);

    @Query("SELECT * FROM Teacher WHERE id=:id ")
    Teacher getTeacherById(String id);


//    @Query("SELECT * FROM Teacher")
//    Teacher getTeachersByLocation(String location);









}
