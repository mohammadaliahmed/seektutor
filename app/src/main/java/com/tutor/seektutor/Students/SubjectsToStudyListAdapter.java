package com.tutor.seektutor.Students;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tutor.seektutor.R;

import java.util.ArrayList;

public class SubjectsToStudyListAdapter extends RecyclerView.Adapter<SubjectsToStudyListAdapter.ViewHolder> {
    Context context;
    ArrayList<SubjectToStudyModel> itemList;

    boolean dntGoForward;

    public SubjectsToStudyListAdapter(Context context, ArrayList<SubjectToStudyModel> itemList) {
        this.context = context;
        this.itemList = itemList;

    }

    public void setDntGoForward(boolean dntGoForward) {
        this.dntGoForward = dntGoForward;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.subject_study_list_item_layout, viewGroup, false);
        SubjectsToStudyListAdapter.ViewHolder viewHolder = new SubjectsToStudyListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SubjectToStudyModel subjectToStudyModel = itemList.get(position);
//
        holder.subject.setText(subjectToStudyModel.getSubject());
        holder.description.setText("Description: " + subjectToStudyModel.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dntGoForward) {
                    Intent i = new Intent(context, AddSubjectsToStudy.class);
                    i.putExtra("id", subjectToStudyModel.getId());
                    context.startActivity(i);
                } else {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description, subject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            subject = itemView.findViewById(R.id.subject);
        }
    }


}
