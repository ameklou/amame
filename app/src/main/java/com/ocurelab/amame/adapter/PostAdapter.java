package com.ocurelab.amame.adapter;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocurelab.amame.R;
import com.ocurelab.amame.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List<Post>posts = new ArrayList<>();
    private Context context;
    private PostAdapter.OnItemClickListner onItemClickListner;

    public interface OnItemClickListner{
        void onClick(View view,int position);
    }

    public PostAdapter(List<Post> posts, Context context, OnItemClickListner onItemClickListner) {
        this.posts = posts;
        this.context = context;
        this.onItemClickListner=onItemClickListner;
    }



    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(view,onItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post post=posts.get(position);
        holder.category.setText(post.getCategory());
        holder.title.setText(post.getTitle());
        Picasso.get().load(post.getCover()).into(holder.cover);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cover;
        TextView title, category;
        private OnItemClickListner itemClickListner;
        public ViewHolder(@NonNull View itemView, OnItemClickListner clickListner) {
            super(itemView);
            cover=itemView.findViewById(R.id.cover);
            title=itemView.findViewById(R.id.title);
            category=itemView.findViewById(R.id.category);
            itemClickListner=clickListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                itemClickListner.onClick(v,getAdapterPosition());
        }
    }


}
