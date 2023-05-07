package com.basetechz.showbox.D_LibraryFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basetechz.showbox.F_Adapter.Recycler_Movie_Child_Adapter;
import com.basetechz.showbox.L_ItemDecoration.GridSpacingItemDecoration;
import com.basetechz.showbox.databinding.FragmentLibraryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.basetechz.showbox.G_models.child_model;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LibraryFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    FragmentLibraryBinding binding;
    DatabaseReference df;
    ArrayList<child_model> child_modelArrayList = new ArrayList<>();
    Recycler_Movie_Child_Adapter childAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(inflater,container,false);

        // continue to watch
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.continueWatch.addItemDecoration(new GridSpacingItemDecoration(3,7,true));
        binding.continueWatch.setLayoutManager(layoutManager1);
        df = FirebaseDatabase.getInstance().getReference("Your Activity");

        df.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                child_modelArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    child_model model = dataSnapshot.getValue(child_model.class);
                    child_modelArrayList.add(model);
                }
                binding.continueWatch.setVisibility(View.VISIBLE);
                childAdapter = new Recycler_Movie_Child_Adapter(getContext(),child_modelArrayList);
                binding.continueWatch.setAdapter(childAdapter);
                childAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return  binding.getRoot();
    }
}