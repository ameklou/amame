package com.ocurelab.amame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocurelab.amame.R;
import com.ocurelab.amame.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<User> userList= new ArrayList<>();
    private Context context;
    private OnItemClickListner onItemClickListner;



    public UserAdapter() {

    }

    public interface OnItemClickListner{
        void onClick(View view,int position);
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view,onItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user= userList.get(position);
        holder.username.setText(user.getUsername());
        //Picasso.get().load(user.getCover()).into(holder.cover);

    }


    public UserAdapter(List<User> userList, Context context, OnItemClickListner onItemClickListner) {
        this.userList = userList;
        this.context = context;
        this.onItemClickListner=onItemClickListner;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         TextView username;
         CircleImageView avatar;
        private OnItemClickListner itemClickListner;
        public ViewHolder(@NonNull View itemView, OnItemClickListner clickListner) {

            super(itemView);
            username=itemView.findViewById(R.id.name);
            avatar=itemView.findViewById(R.id.avatar);
            itemClickListner=clickListner;
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            itemClickListner.onClick(v,getAdapterPosition());
        }
    }
}
