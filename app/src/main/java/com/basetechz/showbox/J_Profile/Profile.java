package com.basetechz.showbox.J_Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.basetechz.showbox.F_Adapter.ViewPager.Profile_ViewPagerAdapter;
import com.basetechz.showbox.K_Setting.SettingActivity;
import com.basetechz.showbox.J_Profile.EditProfileActivity;
import com.basetechz.showbox.databinding.ActivityProfileBinding;
import com.basetechz.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    ActivityProfileBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        String currentUserId = mAuth.getCurrentUser().getUid();

        Profile_ViewPagerAdapter profile_viewPagerAdapter =
                new Profile_ViewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(profile_viewPagerAdapter);
        binding.tab.setupWithViewPager(binding.viewPager);

        // back button
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // setting button
        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        // set profile name and image in profile layout
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot : snapshot.getChildren()){

                    if(snapshot.exists()){
                        String name = snapshot.child("name").getValue(String.class);
                        String image = snapshot.child("image").getValue(String.class);
                        String caption = snapshot.child("bio").getValue(String.class);

                        if(name != null || image != null || caption != null){
                            binding.userName2.setText(name);
                            Picasso.get().load(image).into(binding.profileImage);
                            binding.userCaption.setText(caption);
                        }else {
                            Toast.makeText(Profile.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Profile.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // edit profile button
        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, EditProfileActivity.class));
            }
        });


    }
}