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

public class ExperienceListAdapter extends RecyclerView.Adapter<ExperienceListAdapter.ViewHolder> {
    Context context;
    ArrayList<ExperienceModel> itemList;
    ExperienceListCallbacks callbacks;

    boolean dntGoForward;

    public ExperienceListAdapter(Context context, ArrayList<ExperienceModel> itemList, ExperienceListCallbacks callbacks) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.experience_list_item_layout, viewGroup, false);
        ExperienceListAdapter.ViewHolder viewHolder = new ExperienceListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ExperienceModel experienceModel = itemList.get(position);
//
        holder.subject.setText( experienceModel.getSubject());
        holder.description.setText("Des: " + experienceModel.getDescription());
        holder.experience.setText( experienceModel.getExperience());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dntGoForward) {
                    Intent i = new Intent(context, AddExperience.class);
                    i.putExtra("id", experienceModel.getId());
                    context.startActivity(i);
                }else{

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description, subject, experience;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.description);
            subject = itemView.findViewById(R.id.subject);
            experience = itemView.findViewById(R.id.experience);
        }
    }

    public interface ExperienceListCallbacks {
        public void onSelected();
    }
}
