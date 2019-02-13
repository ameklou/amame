package com.ocurelab.amame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocurelab.amame.R;
import com.ocurelab.amame.model.DomMessage;



import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DomAdapter extends RecyclerView.Adapter<DomAdapter.ViewHolder> {
    private List<DomMessage> domMessages= new ArrayList<>();
    private Context context;
    private int SELF = 100;

    public DomAdapter(List<DomMessage> domMessages, Context context) {
        this.domMessages = domMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==SELF){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message,parent,false);
        }else {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dom_messages,parent,false);
        }

        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        DomMessage domMessage=domMessages.get(position);
        if (!domMessage.getReceiver()){
            return SELF;
        }


        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DomMessage domMessage=domMessages.get(position);
        holder.message.setText(domMessage.getMessage());


    }

    @Override
    public int getItemCount() {
        return domMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message,name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.message_body);

        }
    }
}
