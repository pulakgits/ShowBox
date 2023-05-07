package com.basetechz.showbox;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.basetechz.showbox.F_Adapter.Recycler_Movie_Child_Adapter;
import com.basetechz.showbox.L_ItemDecoration.GridSpacingItemDecoration;
import com.basetechz.showbox.databinding.FragmentActivitiesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import com.basetechz.showbox.G_models.child_model;

public class ActivitiesFragment extends Fragment {

    public ActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentActivitiesBinding binding;
    DatabaseReference databaseReference;
    ArrayList<child_model> child_modelArrayList = new ArrayList<>();
    Recycler_Movie_Child_Adapter childAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentActivitiesBinding.inflate(inflater,container,false);

        GridLayoutManager layoutManager1 = new GridLayoutManager(getContext(), 3);
        binding.ActivitiesRecycler.addItemDecoration(new GridSpacingItemDecoration(3,10,true));
        binding.ActivitiesRecycler.setLayoutManager(layoutManager1);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Your Activity").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                child_modelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    child_model model = dataSnapshot.getValue(child_model.class);
                    child_modelArrayList.add(model);
                }
                childAdapter = new Recycler_Movie_Child_Adapter(getContext(),child_modelArrayList);
                binding.ActivitiesRecycler.setAdapter(childAdapter);
                childAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error Found !!! ", Toast.LENGTH_SHORT).show();
            }
        });







        return binding.getRoot();
    }
}