package com.basetechz.showbox.A_HomeFragment.Main_Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.Calendar;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.basetechz.showbox.A_HomeFragment.a_HomeFragment;
import com.basetechz.showbox.B_SearchFragment.SearchFragment;
import com.basetechz.showbox.D_LibraryFragment.LibraryFragment;
import com.basetechz.showbox.J_Profile.Profile;
import com.basetechz.showbox.K_Setting.SettingActivity;
import com.basetechz.showbox.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.basetechz.showbox.*;
public class a_MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FrameLayout content;

    int searchTextColor;
    ImageView closeButton;
    String greeting;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();

        // Time zone
        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance(timeZone);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 5 && hour < 12) {
            greeting = "Good morning";
        } else if (hour >= 12 && hour < 18) {
            greeting = "Good afternoon";
        } else {
            greeting = "Good Evening";
        }

        // user icon
        binding.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(a_MainActivity.this, Profile.class);
                startActivity(intent);

            }
        });


        // profile name and image in profile layout
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot : snapshot.getChildren()){

                    if(snapshot.exists()){
                        String name = snapshot.child("name").getValue(String.class);
                        String image = snapshot.child("image").getValue(String.class);

                        if(image != null){
                            Picasso.get().load(image).into(binding.userIcon);
                        }else {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // bottom navigation items color & txt color set
        BottomNavigationView bottomNavigationView = findViewById(R.id.bnView);
        @SuppressLint({"ResourceType", "UseCompatLoadingForColorStateLists"})
        ColorStateList iconColorStateList = getResources().getColorStateList(R.drawable.bnview_item_color);
        @SuppressLint({"ResourceType", "UseCompatLoadingForColorStateLists"}) ColorStateList
                textColorStateList = getResources().getColorStateList(R.drawable.bnview_item_color_text);
        bottomNavigationView.setItemIconTintList(iconColorStateList);
        bottomNavigationView.setItemTextColor(textColorStateList);

        // click item perform operation
        binding.bnView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // find id for MenuItem
                int id = item.getItemId();
                if(id== R.id.home){
                    binding.toolBar.setVisibility(View.VISIBLE);
                    binding.userIcon.setVisibility(View.VISIBLE);
                    binding.toolTxt.setText(greeting);
                    loadFragment(new a_HomeFragment(),false);
                } else if (id == R.id.search) {
                    binding.toolBar.setVisibility(View.VISIBLE);
                    binding.userIcon.setVisibility(View.VISIBLE);
                    binding.toolTxt.setText("Search");
                    loadFragment(new SearchFragment(),false);
                } else if (id==R.id.library) {
                    binding.toolBar.setVisibility(View.VISIBLE);
                    binding.userIcon.setVisibility(View.VISIBLE);
                    binding.toolTxt.setText("Library");
                    loadFragment(new LibraryFragment(),false);
                }
                return true;
            }
        });
        binding.bnView.setSelectedItemId(R.id.home);
    }
    private  void  loadFragment(Fragment fragment, boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if(flag){
            ft.add(R.id.fragment_container,fragment);
        }else {
            ft.replace(R.id.fragment_container,fragment);
        }
        ft.commit();
    }
}