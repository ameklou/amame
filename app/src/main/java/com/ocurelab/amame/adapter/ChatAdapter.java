package com.ocurelab.amame.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ocurelab.amame.R;
import com.ocurelab.amame.model.Chat;
import com.ocurelab.amame.model.DomMessage;
import com.ocurelab.amame.utils.Preferences;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.their_message,parent,false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat=chats.get(position);

        if (chat.getMessage()!=null){
            holder.message.setText(chat.getMessage());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'à' HH:mm");
            Date date = (new Date(chat.getTime()));
            holder.date.setText(sdf.format(date));
            holder.messageImageView.setVisibility(ImageView.INVISIBLE);

        }else{

            holder.message.setVisibility(TextView.INVISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'à' HH:mm");
            Date date = (new Date(chat.getTime()));
            holder.date.setText(sdf.format(date));

            // image manager


            String imageUrl = chat.getImageUrl();
            if (imageUrl.startsWith("gs://")){
                StorageReference storageReference = FirebaseStorage.getInstance()
                        .getReferenceFromUrl(imageUrl);

                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            String downloadUrl = task.getResult().toString();
                            Picasso.get().load(downloadUrl).into(holder.messageImageView);
                        }else {
                            Log.w("ErrorDownloadUrl", "Getting download url was not successful.",
                                    task.getException());
                        }
                    }
                });
            }


            //end of image manager

        }






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
        TextView message,date;
        ImageView messageImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.messageTextView);
            date=itemView.findViewById(R.id.date);
            messageImageView=itemView.findViewById(R.id.messageImageView);

        }
    }
}
