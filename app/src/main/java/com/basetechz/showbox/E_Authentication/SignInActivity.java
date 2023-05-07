package com.basetechz.showbox.E_Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.basetechz.showbox.A_HomeFragment.Main_Activity.a_MainActivity;
import com.basetechz.showbox.R;
import com.basetechz.showbox.*;
import com.basetechz.showbox.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseAuth mAuth;
    ProgressDialog dialog;
    boolean isOnClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Login Process.....");


        // click login Button
        binding.LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAuthentication();
            }
        });

        // back to signUP page
        binding.SignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, com.basetechz.showbox.E_Authentication.SignUpActivity.class));
                finish();
            }
        });
    }
    private void loginAuthentication() {
        dialog.show();
        String personEmail, personPassword;
        personEmail = binding.eTEmail.getText().toString();
        personPassword = binding.eTPassword.getText().toString();
        mAuth = FirebaseAuth.getInstance();

        if( personEmail.isEmpty() || personPassword.isEmpty()){
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Please Fill Up", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(personEmail,personPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    intent();
                }else{
                    Toast.makeText(SignInActivity.this,
                            Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            intent();
        }
    }
    // start intent method
    private  void intent(){
        Intent intent = new Intent(SignInActivity.this, a_MainActivity.class);
        startActivity(intent);
        finish();
    }
}