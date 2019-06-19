package com.tutor.seektutor.Students;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tutor.seektutor.Models.Student;
import com.tutor.seektutor.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OnlyStudentListAdapter extends RecyclerView.Adapter<OnlyStudentListAdapter.ViewHolder> {
    Context context;
    ArrayList<Student> itemList;

    public OnlyStudentListAdapter(Context context, ArrayList<Student> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.tutor_list_item_layout, viewGroup, false);
        OnlyStudentListAdapter.ViewHolder viewHolder = new OnlyStudentListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Student student = itemList.get(position);
        if (student.getPicUrl() != null && !student.getPicUrl().equalsIgnoreCase("")) {
            Glide.with(context).load(student.getPicUrl()).into(holder.image);
        }
        holder.name.setText(student.getName());
        holder.details.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewStudent.class);
                i.putExtra("studentId", student.getUsername());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name;
        TextView details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            details = itemView.findViewById(R.id.details);
        }
    }

}
