package com.basetechz.showbox.F_Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basetechz.showbox.I_Activity.MoviePage;
import com.basetechz.showbox.R;
import com.basetechz.showbox.G_models.parent_model;

import java.util.List;


public class Recycler_Movie_Parent_Adapter extends RecyclerView.Adapter<Recycler_Movie_Parent_Adapter.ViewHolder> {

    Context context;
    public List<parent_model> parentItemList;
    public Recycler_Movie_Parent_Adapter(){

    }
    public  Recycler_Movie_Parent_Adapter( Context context){
        this.context = context;
    }

    public void setParentItemList(List<parent_model> parentItemList){
        this.parentItemList = parentItemList;
    }

    public List<parent_model> getParentItemList() {
        return parentItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_rv_item_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        parent_model model = parentItemList.get(position);
        holder.parentTitle.setText(model.getParentTitle());

        holder.child_RV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.child_RV.setLayoutManager(layoutManager);

        Recycler_Movie_Child_Adapter childAdapter = new Recycler_Movie_Child_Adapter(context);
        childAdapter.setChildItemList(model.getChildItemList());
        holder.child_RV.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MoviePage.class);
                intent.putExtra("parentId",model.getParentId());
                intent.putExtra("parentTxt",model.getParentTitle());
                intent.putExtra("ref",model.getRef());
                intent.putExtra("acId",model.getAcId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(parentItemList != null){
            return parentItemList.size();
        }else {
            return  0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView child_RV;
        TextView parentTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            child_RV = itemView.findViewById(R.id.child_rv);
            parentTitle = itemView.findViewById(R.id.pop_mov_Txt);
        }

    }
}