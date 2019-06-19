package com.tutor.seektutor.Tutor;

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

public class SubjectsToTeachListAdapter extends RecyclerView.Adapter<SubjectsToTeachListAdapter.ViewHolder> {
    Context context;
    ArrayList<SubjectToTeachModel> itemList;
    SubjectsToTeachListCallbacks callbacks;

    boolean dntGoForward;

    public SubjectsToTeachListAdapter(Context context, ArrayList<SubjectToTeachModel> itemList, SubjectsToTeachListCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }

    public void setDntGoForward(boolean dntGoForward) {
        this.dntGoForward = dntGoForward;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.subject_teach_list_item_layout, viewGroup, false);
        SubjectsToTeachListAdapter.ViewHolder viewHolder = new SubjectsToTeachListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SubjectToTeachModel experienceModel = itemList.get(position);
//
        holder.subject.setText(experienceModel.getSubject());
        holder.description.setText("Description: " + experienceModel.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dntGoForward) {
                    Intent i = new Intent(context, AddSubjectsToTeach.class);
                    i.putExtra("id", experienceModel.getId());
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
        TextView description, subject, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            subject = itemView.findViewById(R.id.subject);
            price = itemView.findViewById(R.id.price);
        }
    }

    public interface SubjectsToTeachListCallbacks {
        public void onSelected();
    }
}
