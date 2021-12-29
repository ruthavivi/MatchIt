package com.example.class3demo2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
    public interface DatePickerFragmentListener{
        public void onDateSet(int year, int month, int dayOfMonth);
    }

    DatePickerFragmentListener listener;
    public void setOnDatePickerFragmentListener(DatePickerFragmentListener listener){
        this.listener = listener;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (listener != null) {
                    listener.onDateSet(year, month, dayOfMonth);
                }
            }
        }, 2021, 10, 29);

        return dialog;
    }
}
