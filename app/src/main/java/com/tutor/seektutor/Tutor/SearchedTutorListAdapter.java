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
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchedTutorListAdapter extends RecyclerView.Adapter<SearchedTutorListAdapter.ViewHolder> {
    Context context;
    ArrayList<SubjectToTeachModel> itemList;
    SearchedTutorAdapterCallbacks callbacks;

    public SearchedTutorListAdapter(Context context, ArrayList<SubjectToTeachModel> itemList, SearchedTutorAdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.tutor_list_item_layout, viewGroup, false);
        SearchedTutorListAdapter.ViewHolder viewHolder = new SearchedTutorListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SubjectToTeachModel model = itemList.get(position);
        if (model.getTutorPic() != null && !model.getTutorPic().equalsIgnoreCase("")) {
            Glide.with(context).load(model.getTutorPic()).into(holder.image);
        }
        holder.name.setText(model.getTutorName());
        holder.details.setText("Subject: " + model.getSubject() );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewTutor.class);
                i.putExtra("tutorId", model.getTutorId());
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
        TextView name, details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            details = itemView.findViewById(R.id.details);
            image = itemView.findViewById(R.id.image);
        }
    }

    public interface SearchedTutorAdapterCallbacks {
        public void onSelected();
    }
}
