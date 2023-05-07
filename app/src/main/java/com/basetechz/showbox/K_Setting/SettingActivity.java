package com.basetechz.showbox.K_Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.basetechz.showbox.J_Profile.EditProfileActivity;
import com.basetechz.showbox.E_Authentication.SignInActivity;
import com.basetechz.showbox.databinding.ActivitySettingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        String currentUserId = mAuth.getCurrentUser().getUid();

        // back before activity
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Click Edit Button to edit Profile
        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, EditProfileActivity.class));
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

                      if(name != null || image != null){
                          binding.profileName.setText(name);
                          Picasso.get().load(image).into(binding.cuProfileImg);
                      }else {
                          Toast.makeText(SettingActivity.this, "error", Toast.LENGTH_SHORT).show();
                      }
                  }else{
                      Toast.makeText(SettingActivity.this, "error", Toast.LENGTH_SHORT).show();
                  }
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(SettingActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
}