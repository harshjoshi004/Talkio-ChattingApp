package com.example.whattsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.whattsapp.adapters.ChatAdapter;
import com.example.whattsapp.databinding.ActivityChatDetailBinding;
import com.example.whattsapp.fragments.ChatsFragment;
import com.example.whattsapp.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String senderId = auth.getUid();
        String recieveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.textView.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.pfp).into(binding.ivProfileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel> messageList = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageList, this);
        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId+recieveId;
        final String recieverRoom = recieveId+senderId;

        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    MessageModel i = snapshot1.getValue(MessageModel.class);
                    messageList.add(i);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.editTextText.getText().toString();
                final MessageModel model = new MessageModel(senderId,message);
                model.setTimeStamp(new Date().getTime());

                binding.editTextText.setText("");
                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(recieverRoom)
                                        .push()
                                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });
            }
        });
    }
}