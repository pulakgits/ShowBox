package com.basetechz.showbox.A_HomeFragment.Main_Activity.Inner_Fragments.A_ForYou;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.basetechz.showbox.F_Adapter.Recycler_Movie_Child_Adapter;
import com.basetechz.showbox.F_Adapter.Recycler_Movie_Parent_Adapter;
import com.basetechz.showbox.H_MVVMPaterns.ViewModel.FirebaseViewModel;
import com.basetechz.showbox.G_models.child_model;
import com.basetechz.showbox.F_Adapter.ViewPager.SlideImgViewPagerAdapter;
import com.basetechz.showbox.databinding.FragmentForYouBinding;
import com.basetechz.showbox.G_models.parent_model;
import com.basetechz.showbox.G_models.slideImgModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ForYou extends Fragment{

    public ForYou() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentForYouBinding binding;
    FirebaseFirestore database;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference df;

    ArrayList<slideImgModel> slideImgList = new ArrayList<>();
    private  Recycler_Movie_Parent_Adapter parentAdapter;

    // image slider variable
    SlideImgViewPagerAdapter adapter;
    private ImageSlider imageSlider;
    private ArrayList<String> idList= new ArrayList<>();
    Recycler_Movie_Parent_Adapter parent_adapter;
    private FirebaseViewModel firebaseViewModel;


    Recycler_Movie_Child_Adapter adapterChild;
    Recycler_Movie_Child_Adapter childAdapter;

    ArrayList<child_model> child_modelArrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForYouBinding.inflate(inflater,container,false);

        binding.shimmer.startShimmer();
        ViewPager viewPager = binding.viewPagers.viewPager;
        database = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // data retrieving from firebase realtime database for movie slider
        databaseReference.child("SlideMovie").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                slideImgList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    slideImgModel slideImgModel = dataSnapshot.getValue(com.basetechz.showbox.G_models.slideImgModel.class);
                    slideImgList.add(slideImgModel);
                }

                binding.viewPagers.viewPager.setVisibility(View.VISIBLE);
                binding.viewPagers.wormDotsIndicator.setVisibility(View.VISIBLE);
                binding.parentRv.setVisibility(View.VISIBLE);
                binding.shimmer.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adapter =  new SlideImgViewPagerAdapter(getContext(),slideImgList);
        binding.viewPagers.viewPager.setAdapter(adapter);
        binding.viewPagers.wormDotsIndicator.attachTo(binding.viewPagers.viewPager);
        // image slider
        // Create an instance of ImageSlider and pass it the ViewPager
        imageSlider = new ImageSlider(viewPager);
        // Start the automatic sliding timer
        imageSlider.startSlideTimer();



        // nested recycler view
        binding.parentRv.setHasFixedSize(true);
        binding.parentRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        parent_adapter = new Recycler_Movie_Parent_Adapter(getContext());
        binding.parentRv.setAdapter(parent_adapter);

        // Fragment data access from ViewModel
        firebaseViewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
        firebaseViewModel.setReference("ForYou");
        firebaseViewModel.getAllData();

        firebaseViewModel.getParentModelMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<parent_model>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<parent_model> parentModelList) {
                parent_adapter.setParentItemList(parentModelList);
                parent_adapter.notifyDataSetChanged();
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
    @SuppressLint("NotifyDataSetChanged")

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        imageSlider.startSlideTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop the automatic sliding timer when the Activity is paused
        imageSlider.stopSlideTimer();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}