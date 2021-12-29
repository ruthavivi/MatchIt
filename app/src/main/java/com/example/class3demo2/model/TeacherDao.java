package com.example.class3demo2.model;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface TeacherDao {
    @Query("select * from Teacher")
    List<Teacher> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Teacher... teachers);

    @Delete
    void delete(Teacher teacher);

    @Query("SELECT * FROM Teacher WHERE id=:id ")
    Teacher getTeacherById(String id);
}
