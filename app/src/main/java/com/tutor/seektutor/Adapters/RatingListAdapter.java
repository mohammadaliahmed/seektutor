package com.tutor.seektutor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tutor.seektutor.Models.RatingModel;
import com.tutor.seektutor.R;
import com.tutor.seektutor.Utils.SharedPrefs;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingListAdapter extends RecyclerView.Adapter<RatingListAdapter.ViewHolder> {
    Context context;
    ArrayList<RatingModel> itemList;

    public RatingListAdapter(Context context, ArrayList<RatingModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rating_item_layout, viewGroup, false);
        RatingListAdapter.ViewHolder viewHolder = new RatingListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RatingModel model = itemList.get(position);
        if (model.getPicUrl() != null && !model.getPicUrl().equalsIgnoreCase("")) {
            Glide.with(context).load(model.getPicUrl()).into(holder.image);
        }
        if (SharedPrefs.getTutor() != null) {
            holder.name.setText(model.getRatingFrom() + " rated you ");

        } else {
            holder.name.setText("You rated " + model.getTutorId() + " ");

        }

        holder.rating.setRating(model.getRating());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView name;
        AppCompatRatingBar rating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            rating = itemView.findViewById(R.id.rating);
        }
    }

}
