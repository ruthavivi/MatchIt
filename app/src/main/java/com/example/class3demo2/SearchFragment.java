package com.example.class3demo2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


public class SearchFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment
        Button cancel=view.findViewById(R.id.cancel_search_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).popBackStack();

            }
        });

        Button search = view.findViewById(R.id.search_btn);
        search.setOnClickListener(Navigation.createNavigateOnClickListener(SearchFragmentDirections.actionGlobalTeachersListFragment()));






        return view;
    }
}