package com.basetechz.showbox.I_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.basetechz.showbox.F_Adapter.Recycler_Movie_Child_Adapter;
import com.basetechz.showbox.L_ItemDecoration.GridSpacingItemDecoration;
import com.basetechz.showbox.G_models.child_model;
import com.basetechz.showbox.databinding.ActivitySifiMoviePageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MoviePage extends AppCompatActivity {
    String parentId;
    String acId;

    String referenceId;

    public MoviePage(){}

    ActivitySifiMoviePageBinding binding;
    FirebaseFirestore database;
    Recycler_Movie_Child_Adapter adapter;
    int spanCount = 3; // 3 columns
    int spacing = 10; // 50px
    boolean includeEdge = true;

    FirebaseDatabase fd;
    DatabaseReference databaseReference;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySifiMoviePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        // get intent from Recycler_Movie_Parent_Adapter
        Intent intent=getIntent();
        String movieTitle = intent.getStringExtra("parentTxt");
        String reference=intent.getStringExtra("ref");
        acId = intent.getStringExtra("acId");
        parentId = intent.getStringExtra("parentId");
        binding.movieTxt.setText(movieTitle);

        GridLayoutManager layoutManager = new GridLayoutManager(MoviePage.this,spanCount);
        binding.movieRecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount,spacing,includeEdge));
        binding.movieRecycler.setLayoutManager(layoutManager);



        fd=FirebaseDatabase.getInstance();
        fd.getReference(reference).child(acId).child(parentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<child_model> childModelList = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    child_model model = new child_model();
                    model.setMovieId(ds.child("movieId").getValue(String.class));
                    model.setMovieImage(ds.child("movieImage").getValue(String.class));
                    model.setMovieUrl(ds.child("movieUrl").getValue(String.class));
                    model.setMovieTxt(ds.child("movieTxt").getValue(String.class));
                    childModelList.add(model);
                }
                adapter = new Recycler_Movie_Child_Adapter(MoviePage.this,childModelList);
                binding.movieRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MoviePage.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }







    @Override
    protected void onResume() {
        super.onResume();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}