package com.ocurelab.amame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ocurelab.amame.R;
import com.ocurelab.amame.model.DomMessage;
import com.ocurelab.amame.utils.Preferences;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DomAdapter extends RecyclerView.Adapter<DomAdapter.ViewHolder> {
    private List<DomMessage> domMessages= new ArrayList<>();
    private Context context;
    private int SELF = 100;
    private FirebaseAuth mAuth;




    public DomAdapter(List<DomMessage> domMessages, Context context) {
        this.domMessages = domMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dom_messages,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        DomMessage domMessage=domMessages.get(position);
        if (domMessage.getUser()==FirebaseAuth.getInstance().getUid()){
            return SELF;
        }


        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.message.setText(domMessages.get(position).getMessage());
        holder.name.setText(domMessages.get(position).getUser());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'à' HH:mm");
        Date date = (new Date(domMessages.get(position).getTime()));
        holder.date.setText(sdf.format(date));


    }

    @Override
    public int getItemCount() {
        return domMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message,name, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.messageTextView);
            name=itemView.findViewById(R.id.messengerTextView);
            date=itemView.findViewById(R.id.dateTextView);

        }
    }
}
