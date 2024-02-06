package com.example.whattsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.whattsapp.databinding.ActivitySignUpBinding;
import com.example.whattsapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        //creating progress dialogue
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Wait a sec..");
        progressDialog.setMessage("Your account is being created..");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String id = binding.etMailId.getText().toString();
                String uname = binding.etUserName.getText().toString();
                String pass = binding.etPassword.getText().toString();

                if(TextUtils.isEmpty(id)||TextUtils.isEmpty(uname)||TextUtils.isEmpty(pass)){
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Credentials can't be empty!", Toast.LENGTH_SHORT).show();
                } else {

                    (auth.createUserWithEmailAndPassword(id, pass)).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //temporary user created
                                    Users user = new Users(uname, id, pass);

                                    //sent to real time database
                                    String dbId = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(dbId).setValue(user);
                                    // - MyDatabaseRoot (tree root)
                                    //        - Users (child 1)
                                    //            -dbId (child 1.1)
                                    //                -stored user (child 1.1.1) (leaf-node)

                                    //authentication success notifier (unrelated to realtime database work)
                                    progressDialog.dismiss();

                                    Toast.makeText(SignUpActivity.this, "Sign Up Successful!", Toast.LENGTH_SHORT).show();

                                                            //sign in code
                                    (auth.signInWithEmailAndPassword(id, pass)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressDialog.dismiss();
                                            if (task.isSuccessful()) {

                                                Toast.makeText(SignUpActivity.this, "Welcome to Talkio!", Toast.LENGTH_SHORT).show();

                                                //Go To Main Activity
                                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, id + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    );
                }
            }
        });

        binding.signInOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}