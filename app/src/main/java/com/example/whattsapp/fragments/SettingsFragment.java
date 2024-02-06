package com.example.whattsapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whattsapp.MainActivity;
import com.example.whattsapp.R;
import com.example.whattsapp.SignInActivity;
import com.example.whattsapp.databinding.FragmentSettingsBinding;
import com.example.whattsapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        //code
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Picasso.get()
                        .load(users.getProfilePic())
                        .placeholder(R.drawable.pfp)
                        .into(binding.ivSettingsProfileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.ivSettingsProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); //*/*
                startActivityForResult(intent, 33);
            }
        });
        binding.btnLogOutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        binding.btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newStatus, newName;
                newStatus = binding.etAboutSettings.getText().toString();
                newName = binding.etUserNameSettings.getText().toString();
                //to update multiple attributes make a key,value hashmap
                HashMap<String, Object> obj = new HashMap<>();
                obj.put("userName", newName);
                obj.put("status", newStatus);

                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
                Toast.makeText(getContext(), "Settings Saved!", Toast.LENGTH_SHORT).show();
            }
        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                Picasso.get()
                        .load(user.getProfilePic())
                        .placeholder(R.drawable.pfp)
                        .into(binding.ivSettingsProfileImage);

                binding.etAboutSettings.setText(user.getStatus());
                //binding.etAboutSettings.setHint("'you' in one sentence!");
                binding.etUserNameSettings.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) { //if user sets an image then this branch
            Uri sFile = data.getData();
            //URI = uniform resource identifier
            binding.ivSettingsProfileImage.setImageURI(sFile);

            final StorageReference storageRef = storage.getReference().child("profilePicture").child(FirebaseAuth.getInstance().getUid());
            storageRef.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("profilePic").setValue(uri.toString());
                        }
                    });

                    Toast.makeText(getContext(), "Image Uploaded successfuly!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}