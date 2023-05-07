package com.basetechz.showbox.A_HomeFragment.Main_Activity.Inner_Fragments.B_IndianFlim;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.basetechz.showbox.F_Adapter.Recycler_Movie_Parent_Adapter;
import com.basetechz.showbox.H_MVVMPaterns.ViewModel.FirebaseViewModel;
import com.basetechz.showbox.databinding.FragmentPanIndiaBinding;
import com.basetechz.showbox.G_models.parent_model;
import com.google.firebase.database.DatabaseError;

import java.util.List;

public class IndianFilm extends Fragment {


    public IndianFilm() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentPanIndiaBinding binding;
    private FirebaseViewModel firebaseViewModel;
    private Recycler_Movie_Parent_Adapter parentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPanIndiaBinding.inflate(getLayoutInflater());

        binding.panIndiaRecycler.setHasFixedSize(true);
        binding.panIndiaRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        parentAdapter = new Recycler_Movie_Parent_Adapter(getContext());
        binding.panIndiaRecycler.setAdapter(parentAdapter);
        firebaseViewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
        firebaseViewModel.setReference("IndianFilm");
        firebaseViewModel.getAllData();

        firebaseViewModel.getParentModelMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<parent_model>>() {
            @Override
            public void onChanged(List<parent_model> parentModelList) {
                parentAdapter.setParentItemList(parentModelList);
                parentAdapter.notifyDataSetChanged();
            }
        });
        firebaseViewModel.getDatabaseErrorMutableLiveData().observe(getViewLifecycleOwner(), new Observer<DatabaseError>() {
            @Override
            public void onChanged(DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });








        return binding.getRoot();
    }
}