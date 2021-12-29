package com.example.class3demo2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Teacher;

import java.util.List;

public class TeachersListFragmentViewModel extends ViewModel {
    LiveData<List<Teacher>> data = Model.instance.getAll();

    public LiveData<List<Teacher>> getData() {
        return data;
    }
}
