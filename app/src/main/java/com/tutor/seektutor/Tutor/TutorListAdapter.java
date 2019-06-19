package com.tutor.seektutor.Tutor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tutor.seektutor.Models.Tutor;
import com.tutor.seektutor.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorListAdapter extends RecyclerView.Adapter<TutorListAdapter.ViewHolder> {
    Context context;
    ArrayList<Tutor> itemList;
    TutorAdapterCallbacks callbacks;

    public TutorListAdapter(Context context, ArrayList<Tutor> itemList, TutorAdapterCallbacks callbacks) {
        this.context = context;
        this.itemList = itemList;
        this.callbacks = callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(context).inflate(R.layout.tutor_list_item_layout,viewGroup,false);
        TutorListAdapter.ViewHolder viewHolder=new TutorListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Tutor tutor=itemList.get(position);
        if(tutor.getPicUrl()!=null && !tutor.getPicUrl().equalsIgnoreCase("")){
            Glide.with(context).load(tutor.getPicUrl()).into(holder.image);
        }
        holder.name.setText(tutor.getName());
        holder.details.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,ViewTutor.class);
                i.putExtra("tutorId",tutor.getUsername());
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
        TextView name,details;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            details=itemView.findViewById(R.id.details);
            image=itemView.findViewById(R.id.image);
        }
    }
    public interface TutorAdapterCallbacks{
        public void onSelected();
    }
}
