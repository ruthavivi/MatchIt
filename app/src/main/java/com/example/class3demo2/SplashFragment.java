package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashFragment extends Fragment {

//מפה המהסך אמור להמשיך אם הוא מחובר לדף הבית ובמידה ולא אז ממשיך להרשמה בעצם בודק עם המשתמש מחובר כבר או לא


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        checkUser();
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    private void checkUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            //go to your home view
        }else{
            //go to register
        }
    }
}