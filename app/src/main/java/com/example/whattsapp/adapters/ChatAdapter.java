package com.example.whattsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whattsapp.R;
import com.example.whattsapp.models.MessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{
    ArrayList<MessageModel> list;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECIEVER_VIEW_TYPE = 2;


    public ChatAdapter(ArrayList<MessageModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.sampler_reciever,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel messageModel = list.get(position);
        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());

        }else{
            ((RecieverViewHolder)holder).recieverMsg.setText(messageModel.getMessage());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        } else{
            return RECIEVER_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecieverViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder{
        TextView recieverMsg, revieverTime;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.recieverText);
            revieverTime = itemView.findViewById(R.id.recieverTime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.senderMessage);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}

