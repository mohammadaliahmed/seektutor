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

public class EducationListAdapter extends RecyclerView.Adapter<EducationListAdapter.ViewHolder> {
    Context context;
    ArrayList<EducationModel> itemList;
    EducationListCallbacks callbacks;

    boolean dntGoForward;

    public EducationListAdapter(Context context, ArrayList<EducationModel> itemList, EducationListCallbacks callbacks) {
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
        EducationListAdapter.ViewHolder viewHolder = new EducationListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final EducationModel educationModel = itemList.get(position);
//
        holder.subject.setText( educationModel.getEducationName());
        holder.description.setText("Description: " + educationModel.getDescription());
        holder.experience.setText( "Year: "+educationModel.getYear());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dntGoForward) {
                    Intent i = new Intent(context, AddEducation.class);
                    i.putExtra("id", educationModel.getId());
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

    public interface EducationListCallbacks {
        public void onSelected();
    }
}
