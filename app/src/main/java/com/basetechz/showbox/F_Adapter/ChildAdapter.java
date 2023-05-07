package com.basetechz.showbox.F_Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.basetechz.showbox.R;
import com.basetechz.showbox.I_Activity.VideoActivity;
import com.basetechz.showbox.G_models.child_model;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    DatabaseReference databaseReference;
    Context context;
    ArrayList<child_model> child_modelArrayList =new ArrayList<>();

    public ChildAdapter(Context context, ArrayList<child_model> child_modelArrayList) {
        this.context = context;
        this.child_modelArrayList = child_modelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        child_model childModel = child_modelArrayList.get(position);
        holder.movieTxt.setText(childModel.getMovieTxt());
        Picasso.get().load(childModel.getMovieImage()).into(holder.movieImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("itemId",childModel.getMovieId());
                intent.putExtra("itemTxt",childModel.getMovieTxt());
                intent.putExtra("videoUrl",childModel.getMovieUrl());

                databaseReference = FirebaseDatabase.getInstance().getReference("Click_Movie");
                String clickId = databaseReference.push().getKey();
                assert clickId != null;
                databaseReference.child(clickId).setValue(childModel);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(child_modelArrayList != null){
           return child_modelArrayList.size();
        }else{
            return  0;
        }
    }

    public void setData(ArrayList<child_model> filteredList) {
        this.child_modelArrayList=filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView movieImage;
        TextView movieTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movieImage);
            movieTxt = itemView.findViewById(R.id.movieText);
        }
    }
}
