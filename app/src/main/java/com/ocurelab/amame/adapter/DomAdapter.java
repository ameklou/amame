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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ocurelab.amame.R;
import com.ocurelab.amame.model.DomMessage;
import com.ocurelab.amame.utils.Preferences;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

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
        DomMessage domMessage= domMessages.get(position);

        if (domMessage.getMessage()==null){
            holder.message.setVisibility(TextView.INVISIBLE);
            holder.name.setText(domMessage.getUser());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'à' HH:mm");
            Date date = (new Date(domMessage.getTime()));
            holder.date.setText(sdf.format(date));

            // image manager


            String imageUrl = domMessage.getImageUrl();
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

            if (domMessage.getReceiver()){
                Picasso.get().load(R.drawable.logo_amame).into(holder.senderImage);


            }else {
                Picasso.get().load(R.drawable.user).into(holder.senderImage);
            }

        }else if (domMessage.getImageUrl()==null){
            holder.message.setText(domMessages.get(position).getMessage());
            holder.name.setText(domMessages.get(position).getUser());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'à' HH:mm");
            Date date = (new Date(domMessages.get(position).getTime()));
            holder.date.setText(sdf.format(date));
            holder.messageImageView.setVisibility(ImageView.INVISIBLE);

            if (domMessages.get(position).getReceiver()){
                Picasso.get().load(R.drawable.logo_amame).into(holder.senderImage);


            }else {
                Picasso.get().load(R.drawable.user).into(holder.senderImage);
            }

        }

        holder.message.setText(domMessages.get(position).getMessage());
        holder.name.setText(domMessages.get(position).getUser());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'à' HH:mm");
        Date date = (new Date(domMessages.get(position).getTime()));
        holder.date.setText(sdf.format(date));

        if (domMessages.get(position).getReceiver()){
            Picasso.get().load(R.drawable.logo_amame).into(holder.senderImage);


        }else {
            Picasso.get().load(R.drawable.user).into(holder.senderImage);
        }


    }

    @Override
    public int getItemCount() {
        return domMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message,name, date;
        CircleImageView senderImage;
        ImageView messageImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.messageTextView);
            name=itemView.findViewById(R.id.messengerTextView);
            date=itemView.findViewById(R.id.dateTextView);
            senderImage=itemView.findViewById(R.id.messengerImageView);
            messageImageView=itemView.findViewById(R.id.messageImageView);

        }
    }
}
