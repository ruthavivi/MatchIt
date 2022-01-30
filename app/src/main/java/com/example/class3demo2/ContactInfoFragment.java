package com.example.class3demo2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.class3demo2.model.Model;
import com.example.class3demo2.model.Teacher;
import com.squareup.picasso.Picasso;


public class ContactInfoFragment extends Fragment {


    Teacher teacher;
    TextView nameTv;
    TextView phoneTv;
    TextView emailTv;
    TextView locationTv;
    ImageView avatarImg;
    Button call;
    Button email;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact_info, container, false);
        Button cancel=view.findViewById(R.id.cancel_contact_btn);
        nameTv=view.findViewById(R.id._name_tv);
        phoneTv=view.findViewById(R.id._phone_tv);
        emailTv=view.findViewById(R.id._email_tv);
        locationTv=view.findViewById(R.id._location_tv);
        avatarImg = view.findViewById(R.id.teacherEdit_avatar_img);
        call=view.findViewById(R.id.call);
        email=view.findViewById(R.id.email);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+teacher.getPhone()));
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",teacher.getEmail(), null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "substitute teacher");
                intent.putExtra(Intent.EXTRA_TEXT, "hey i am looking for a replacement teacher, are you available?");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });



        String teacherId = EditFragmentArgs.fromBundle(getArguments()).getTeacherId();
        Model.instance.getTeacherById(teacherId, (teacher) -> {
            this.teacher = teacher;
            updateDisplay();
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();

            }
        });
        return view;
    }


    private void updateDisplay() {
        nameTv.setText(teacher.getName());
        emailTv.setText(teacher.getEmail());
        phoneTv.setText(teacher.getPhone());
        locationTv.setText(teacher.getLocation());
        if (teacher.getAvatarUtl() != null) {
            Picasso.get()
                    .load(teacher.getAvatarUtl())
                    .placeholder(R.drawable.avatar)
                    .into(avatarImg);
        }


    }
}