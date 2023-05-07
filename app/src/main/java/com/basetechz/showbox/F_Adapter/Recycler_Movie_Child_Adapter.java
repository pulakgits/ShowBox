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

import com.basetechz.showbox.G_models.child_model;
import com.basetechz.showbox.R;
import com.basetechz.showbox.I_Activity.VideoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class Recycler_Movie_Child_Adapter extends RecyclerView.Adapter<Recycler_Movie_Child_Adapter.ViewHolder> {

    Context context;
    private List<child_model> childItemList;
    public Recycler_Movie_Child_Adapter(List<child_model> childItemList){
        this.childItemList=childItemList;
    }
    public Recycler_Movie_Child_Adapter(){
    }



    public Recycler_Movie_Child_Adapter(Context context){
        this.context =context;
    }
    public  Recycler_Movie_Child_Adapter(Context context, List<child_model> childItemList ){
        this.context=context;
        this.childItemList=childItemList;
    }
    public void setChildItemList(List<child_model> childItemList) {
        this.childItemList = childItemList;
        this.childItemList.removeAll(Collections.singleton(null));
    }

    public void setFilteredList(List<child_model> childModelList1) {
        this.childItemList=childModelList1;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        child_model models = childItemList.get(position);

        Picasso.get().load(models.getMovieImage()).into(holder.movieImage);
        holder.textView.setText(models.getMovieTxt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("itemId", models.getMovieId());
                intent.putExtra("itemTxt", models.getMovieTxt());
                intent.putExtra("videoUrl", models.getMovieUrl());

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Your Activity");

                String movieId = models.getMovieTxt();
                databaseReference.child(movieId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Item already exists in database
                            context.startActivity(intent);
                        } else {
                            // Item doesn't exist in database, add it
                            databaseReference.child(movieId).setValue(models);
                            context.startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        if (childItemList != null){
            return childItemList.size();
        }else{
            return  0;
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{


        ImageView movieImage;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movieImage);
            textView = itemView.findViewById(R.id.movieText);
        }
    }
}