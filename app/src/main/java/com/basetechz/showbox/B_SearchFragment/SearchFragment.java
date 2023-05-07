package com.basetechz.showbox.B_SearchFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.basetechz.showbox.F_Adapter.ChildAdapter;
import com.basetechz.showbox.F_Adapter.Recycler_Movie_Parent_Adapter;
import com.basetechz.showbox.L_ItemDecoration.GridSpacingItemDecoration;
import com.basetechz.showbox.R;
import com.basetechz.showbox.H_MVVMPaterns.ViewModel.FirebaseViewModel;
import com.basetechz.showbox.databinding.FragmentSearchBinding;
import com.basetechz.showbox.G_models.child_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentSearchBinding binding;
    private FirebaseViewModel firebaseViewModel;
    private Recycler_Movie_Parent_Adapter parent_adapter;
    private ChildAdapter child_adapter;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    ArrayList<child_model> childModelList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater,container,false);


        int searchTextViewId = binding.searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = binding.searchView.findViewById(searchTextViewId);
        searchEditText.setTextColor(getResources().getColor(R.color.black));



        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        binding.searchRecycler.addItemDecoration(new GridSpacingItemDecoration(3,20,true));
        binding.searchRecycler.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        databaseReference.child("SearchMovie").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childModelList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    child_model model = dataSnapshot.getValue(child_model.class);
                    childModelList.add(model);
                }
                child_adapter=new ChildAdapter(getContext(),childModelList);
                binding.searchRecycler.setAdapter(child_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // search view operation
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList(newText);
                return true;
            }
        });




        return binding.getRoot();
    }

    private void filteredList(String text) {
        ArrayList<child_model> filteredList = new ArrayList<>();
       for(child_model item : childModelList){
           if(item.getMovieTxt().toLowerCase().contains(text.toLowerCase())){
               filteredList.add(item);
           }
       }
       if(filteredList.isEmpty()){
           Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
       }else{
           child_adapter.setData(filteredList);
       }
    }
}