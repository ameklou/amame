package com.ocurelab.amame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocurelab.amame.R;
import com.ocurelab.amame.model.Topic;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    private List<Topic> topics;
    private Context context;
    private OnTopicClickedListner topicClickedListner;

    public interface OnTopicClickedListner{
        void onClick(View view, int position);
    }

    public TopicAdapter(List<Topic> topics, Context context, OnTopicClickedListner topicClickedListner) {
        this.topics = topics;
        this.context = context;
        this.topicClickedListner = topicClickedListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item,parent,false);
        return new ViewHolder(view, topicClickedListner);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicAdapter.ViewHolder holder, int position) {
        Topic topic= topics.get(position);
        holder.content.setText(topic.getContent());
        holder.username.setText(topic.getUsername());
        holder.title.setText(topic.getTitle());


    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView avatar;
        TextView username, title, content;
        private OnTopicClickedListner onTopicClickedListner;
        public ViewHolder(@NonNull View itemView, OnTopicClickedListner topicClickedListner1) {
            super(itemView);
            avatar=itemView.findViewById(R.id.avatar);
            username=itemView.findViewById(R.id.username);
            title=itemView.findViewById(R.id.title);
            content=itemView.findViewById(R.id.content);
            itemView.setOnClickListener(this);
            onTopicClickedListner=topicClickedListner1;
        }

        @Override
        public void onClick(View v) {
            onTopicClickedListner.onClick(v,getAdapterPosition());

        }
    }
}
