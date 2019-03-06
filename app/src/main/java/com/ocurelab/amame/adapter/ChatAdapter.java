package com.ocurelab.amame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocurelab.amame.R;
import com.ocurelab.amame.model.Chat;
import com.ocurelab.amame.model.DomMessage;
import com.ocurelab.amame.utils.Preferences;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private int SELF = 100;
    private Preferences preferences;
    private List<Chat> chats=new ArrayList<>();

    public ChatAdapter(Context context, List<Chat> chats) {
        this.context = context;
        this.chats = chats;
        preferences=new Preferences(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==SELF){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message,parent,false);
        }else {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message,parent,false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat=chats.get(position);
        holder.message.setText(chat.getMessage());


    }
    @Override
    public int getItemViewType(int position) {
        Chat chat=chats.get(position);
        if (chat.getSender().equals(preferences.getUsername())){
            return SELF;
        }


        return position;
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView message,name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.message_body);

        }
    }
}
