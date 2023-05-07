package com.basetechz.showbox.E_Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.basetechz.showbox.A_HomeFragment.Main_Activity.a_MainActivity;
import com.basetechz.showbox.databinding.ActivitySignUpBinding;
import com.basetechz.showbox.G_models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog dialog;
    DatabaseReference databaseReference;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating new account....");

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authentication();
            }
        });

        // this page to login page
        binding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }
    private void Authentication() {
        dialog.show();
        String personName,personEmail,personPassword;
        personName = binding.eTName.getText().toString();
        personEmail = binding.eTEmail.getText().toString();
        personPassword = binding.eTPassword.getText().toString();

        if(personName.isEmpty() || personEmail.isEmpty() || personPassword.isEmpty()){
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please Fill Up", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(personEmail,personPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();

                    String personBio = "Hi";
                    String personDob = "30/10/2000";
                    String personImage = "https://assets.stickpng.com/images/585e4bf3cb11b227491c339a.png";
                    String personGender = "Hi";


                    User firebaseUser = new User(personName, personBio, personDob,personGender,personEmail,personPassword,personImage);
                    String uid = task.getResult().getUser().getUid();


                    // database reference
                   databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                   databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           if (snapshot.exists()) {
                               // Item already exists in database
                               intents();
                           } else {
                               // Item doesn't exist in database, add it
                               databaseReference.child(uid).setValue(firebaseUser);
                                intents();
                           }
                       }
                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {
                           Toast.makeText(SignUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                   });
                }
            }
        });

    }

    // user exists or not


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            intent();
        }
    }

    // start intent method
    private  void  intent(){
        Intent intent = new Intent(SignUpActivity.this, a_MainActivity.class);
        startActivity(intent);
        finish();
    }



    private  void intents(){
        Intent intent = new Intent(SignUpActivity.this, com.basetechz.showbox.E_Authentication.UpdateProfileActivity.class);
        startActivity(intent);
        finish();
    }
}