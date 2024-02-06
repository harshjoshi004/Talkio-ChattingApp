package com.example.whattsapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whattsapp.R;
import com.example.whattsapp.adapters.ChatAdapter;
import com.example.whattsapp.databinding.FragmentChatsBinding;
import com.example.whattsapp.databinding.FragmentSettingsBinding;
import com.example.whattsapp.databinding.FragmentStreamBinding;
import com.example.whattsapp.models.MessageModel;
import com.example.whattsapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class StreamFragment extends Fragment {
    FragmentStreamBinding binding;
    String myName;

    public StreamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStreamBinding.inflate(inflater, container, false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final String senderIdStream = FirebaseAuth.getInstance().getUid();

        final ChatAdapter adapter = new ChatAdapter(messageModels, getContext());

        binding.streamRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.streamRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("stream").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModels.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                    messageModels.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                myName = user.getUserName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.sendButtonStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = myName+": "+binding.enterMessageStream.getText().toString();
                final MessageModel model = new MessageModel(senderIdStream,message);

                model.setTimeStamp(new Date().getTime());
                binding.enterMessageStream.setText("");

                database.getReference().child("stream").push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });

        return binding.getRoot();
    }
}