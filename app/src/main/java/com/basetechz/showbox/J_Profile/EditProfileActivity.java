package com.basetechz.showbox.J_Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.basetechz.showbox.K_Setting.SettingActivity;
import com.basetechz.showbox.databinding.ActivityEditProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EditProfileActivity extends AppCompatActivity {
    String gen;
    ActivityEditProfileBinding  binding;

    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

    private final  int GALLERY_REQ_CODE = 1000;
    Uri uriFilepath;
    Bitmap bitmap;
    ProgressDialog dialog;
    FirebaseAuth mAuth;
    private final boolean isClicked=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Update Profile.....");

        // click save button
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        // click set edit image button
        binding.editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,GALLERY_REQ_CODE);
            }
        });

        // set data into editProfile Activity method
        setData();

        // back to activity
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              startActivity(new Intent(EditProfileActivity.this, SettingActivity.class));
                onBackPressed();
                finish();
            }
        });
    }
    // data retrive method
    private void setData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot : snapshot.getChildren()){

                    if(snapshot.exists()){
                        String name = snapshot.child("name").getValue(String.class);
                        String image = snapshot.child("image").getValue(String.class);
                        String bio = snapshot.child("bio").getValue(String.class);
                        String dob = snapshot.child("dob").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);

                        if(name != null || image != null || bio != null || dob != null || gender != null){
                            binding.personName.setText(name);
                            Picasso.get().load(image).into(binding.profileImage);
                            binding.personBio.setText(bio);
                            binding.personDOB.setText(dob);
                            binding.personGender.setText(gender);
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
    }

    // updateProfile method
    private void updateProfile() {
        dialog.show();
        String personName, personBio, personDOB, personGender;
        personName = binding.personName.getText().toString();
        personBio = binding.personBio.getText().toString();
        personDOB = binding.personDOB.getText().toString();
        personGender = binding.personGender.getText().toString();

            if (uriFilepath != null) {
                // Upload the image to Firebase Storage
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("images/" + userId + ".jpg");
                UploadTask uploadTask = storageRef.putFile(uriFilepath);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded image
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Update the user's profile with the image URL and other details
                                String imageUrl = uri.toString();

                                Map<String, Object> updates = new HashMap<>();
                                updates.put("name", personName);
                                updates.put("bio", personBio);
                                updates.put("dob", personDOB);
                                updates.put("image", imageUrl); // add the image URL to the updates
                                updates.put("gender", personGender);

                                databaseReference.updateChildren(updates, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if(error != null){
                                            Log.e("Firebase Update Error", error.getMessage());
                                            Toast.makeText(getApplicationContext(), "Update failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }else{
                                            dialog.dismiss();
                                            onBackPressed();
                                            Toast.makeText(getApplicationContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            } else {
                // If the user didn't select a new image, update the profile with other details only
                Map<String, Object> updates = new HashMap<>();
                updates.put("name", personName);
                updates.put("bio", personBio);
                updates.put("dob", personDOB);
                updates.put("gender", personGender);

                databaseReference.updateChildren(updates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if(error != null){
                            Log.e("Firebase Update Error", error.getMessage());
                            Toast.makeText(getApplicationContext(), "Update failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            dialog.dismiss();
                            onBackPressed();
                            Toast.makeText(getApplicationContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK) {
            uriFilepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uriFilepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                binding.profileImage.setImageBitmap(bitmap);
            } catch (Exception exception) {
                System.out.printf(String.valueOf(exception));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}