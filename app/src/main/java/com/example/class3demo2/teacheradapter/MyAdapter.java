package com.example.class3demo2.teacheradapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.class3demo2.R;
import com.example.class3demo2.TeachersListFragment;
import com.example.class3demo2.TeachersListFragmentViewModel;
import com.example.class3demo2.model.Teacher;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private List<Teacher> list = new ArrayList<>();

    public interface OnItemClickListener{
        void onItemClick(int position, View v);
    }

    OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void updateListTeachers(List<Teacher> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_list_row,parent,false);
        MyViewHolder holder = new MyViewHolder(view,listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Teacher teacher = list.get(position);
        holder.nameTv.setText(teacher.getName());
        holder.idTv.setText(teacher.getId());
        holder.cb.setChecked(teacher.isFlag());
        String url = teacher.getAvatarUtl();
        if (url != null && !url.equals("")){

            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.avatar)
                    .into(holder.avatarImg);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nameTv;
        ImageView avatarImg;

        TextView idTv;
        CheckBox cb;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.listrow_name_tv);
            avatarImg = itemView.findViewById(R.id.listrow_avatar_img);

            idTv = itemView.findViewById(R.id.listrow_id_tv);
            cb = itemView.findViewById(R.id.listrow_cb);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(pos,v);
                    }
                }
            });
        }



        public void bind(Teacher teacher){
            nameTv.setText(teacher.getName());
            idTv.setText(teacher.getId());
            cb.setChecked(teacher.isFlag());
            String url = teacher.getAvatarUtl();
            if (url != null && !url.equals("")){
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.avatar)
                        .into(avatarImg);
            }
        }
    }


}
