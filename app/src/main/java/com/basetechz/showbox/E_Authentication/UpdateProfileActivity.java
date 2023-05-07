package com.basetechz.showbox.E_Authentication;

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
import android.widget.Toast;

import com.basetechz.showbox.A_HomeFragment.Main_Activity.a_MainActivity;
import com.basetechz.showbox.K_Setting.SettingActivity;
import com.basetechz.showbox.R;
import com.basetechz.showbox.*;
import com.basetechz.showbox.databinding.ActivityUpdateProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UpdateProfileActivity extends AppCompatActivity {
    ActivityUpdateProfileBinding binding;
    private final  int GALLERY_REQ_CODE = 1000;
    Uri uriFilepath;
    Bitmap bitmap;
    ProgressDialog dialog;
    FirebaseAuth mAuth;

    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Update Profile.......");

        // click save button
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                startActivity(new Intent(UpdateProfileActivity.this, a_MainActivity.class));
                finishAffinity();
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
    }
    // update profile
    private void updateProfile() {
        dialog.show();

        String personName, personBio, personDOB;
        personName = binding.personName.getText().toString();
        personBio = binding.personBio.getText().toString();
        personDOB = binding.personDOB.getText().toString();

        if (personName.isEmpty() || personBio.isEmpty() || personDOB.isEmpty()) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please Fill Up", Toast.LENGTH_SHORT).show();
        } else {
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

                                databaseReference.updateChildren(updates, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error != null) {
                                            Log.e("Firebase Update Error", error.getMessage());
                                            Toast.makeText(getApplicationContext(), "Update failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            dialog.dismiss();
                                            startActivity(new Intent(UpdateProfileActivity.this, SettingActivity.class));
                                            finishAffinity();
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

                databaseReference.updateChildren(updates, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            Log.e("Firebase Update Error", error.getMessage());
                            Toast.makeText(getApplicationContext(), "Update failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            dialog.dismiss();
                            startActivity(new Intent(UpdateProfileActivity.this, SettingActivity.class));
                            finishAffinity();
                            Toast.makeText(getApplicationContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == GALLERY_REQ_CODE && resultCode == RESULT_OK) {
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