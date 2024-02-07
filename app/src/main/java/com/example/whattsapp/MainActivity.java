package com.example.whattsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whattsapp.adapters.FragmentAdapter;
import com.example.whattsapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseUser CurUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Talkio");
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.horiz));
        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            finish();
        }
        else if(item.getItemId() == R.id.log_out){
            auth.signOut();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
        // Inside your onOptionsItemSelected method, for the delete_account case
        else if(item.getItemId() == R.id.delete_account){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Confirm Deletion");
            builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User confirmed, proceed with deletion
                    deleteUserAccount();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // User canceled deletion
                    dialog.dismiss();
                }
            });

            builder.show();
        }

        return true;
    }
    private void deleteUserAccount() {
        CurUser = auth.getCurrentUser();
        if (CurUser != null) {
            CurUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        // Delete user data from the Realtime Database
                        FirebaseDatabase.getInstance().getReference().child("Users").child(CurUser.getUid()).removeValue();
                        Intent delint = new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(delint);
                        finish();
                        Toast.makeText(MainActivity.this, "User Deleted Successfully..", Toast.LENGTH_SHORT).show();
                    } else {

                        Exception exception = task.getException();
                        if (exception != null) {
                            exception.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            
            Toast.makeText(MainActivity.this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }

}