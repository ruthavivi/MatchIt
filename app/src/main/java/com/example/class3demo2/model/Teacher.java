package com.example.class3demo2.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.class3demo2.MyApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Teacher {
    final static String ID = "id";
    public final static String LAST_UPDATED = "LAST_UPDATED";

    @PrimaryKey
    @NonNull
    String id = "";
    String location = "";
    String email = "";
    String name = "";
    String password = "";
    boolean flag = false;
    Long lastUpdated = new Long(0);

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public Teacher(){}
    public Teacher(String name, String id, boolean flag,String email,String password,String location) {
        this.name = name;
        this.password=password;
        this.id = id;
        this.flag = flag;
        this.email=email;
        this.location = location;
    }

    public void updateTeacher(String name, boolean flag,String email,String password,String location){
        this.name = name;
        this.location = location;
        this.password=password;
        this.flag = flag;
        this.email=email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public boolean isFlag() {
        return flag;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put("name", getName());
        json.put("password", getPassword());
        json.put("email", getEmail());
        json.put("location", getLocation());
        json.put("flag", isFlag());
        json.put(LAST_UPDATED, FieldValue.serverTimestamp());
        return json;
    }

    static Teacher fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        if (id == null){
            return null;
        }
        String name = (String)json.get("name");
        String password = (String)json.get("password");
        String location = (String)json.get("location");
        String email = (String)json.get("email");
        boolean flag = (Boolean) json.get("flag");
        Teacher teacher = new Teacher(name,id,flag,email,password,location);
        Timestamp ts = (Timestamp)json.get(LAST_UPDATED);
        teacher.setLastUpdated(new Long(ts.getSeconds()));
        return teacher;
    }

    static Long getLocalLastUpdated(){
        Long localLastUpdate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("STUDENTS_LAST_UPDATE",0);
        return localLastUpdate;
    }

    static void setLocalLastUpdated(Long date){
        SharedPreferences.Editor editor = MyApplication.getContext()
                .getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        editor.putLong("STUDENTS_LAST_UPDATE",date);
        editor.commit();
        Log.d("TAG", "new lud " + date);
    }
}
