package com.ocurelab.amame.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ocurelab.amame.R;
import com.ocurelab.amame.adapter.ChatAdapter;
import com.ocurelab.amame.adapter.DomAdapter;
import com.ocurelab.amame.model.Chat;
import com.ocurelab.amame.model.DomMessage;
import com.ocurelab.amame.utils.Preferences;
import com.ocurelab.amame.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private ImageButton sendMessage, addFile;
    private EditText doMessage;
    private Preferences preferences;
    private RecyclerView messageList;
    private LinearLayoutManager linearLayoutManager;
    private List<Chat> chats;
    private RecyclerView.Adapter adapter;
    private String url="https://api.amame.org/api/messages/";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private static final int REQUEST_IMAGE = 2;


    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_message, container, false);

        mAuth=FirebaseAuth.getInstance();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getArguments().getString("username"));

        sendMessage= view.findViewById(R.id.sendMessage);
        doMessage=view.findViewById(R.id.mess);
        preferences= new Preferences(this.getContext());
        database= FirebaseDatabase.getInstance();
        addFile= view.findViewById(R.id.add_file_button);

        sendMessage.setOnClickListener(v->{
            addMessage();
        });

        addFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE);
        });


        messageList=view.findViewById(R.id.chatView);
        chats=new ArrayList<>();
        adapter=new ChatAdapter(getContext(),chats);
        linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(linearLayoutManager);
        database.getReference("message").child(mAuth.getUid()).child(getArguments().getString("fireId"))
                .addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Chat chat=dataSnapshot.getValue(Chat.class);
                                chats.add(chat);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }
                );

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int messageCount = adapter.getItemCount();
                int lastItem= linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastItem== -1 || (positionStart >= (messageCount-1)&& lastItem==(positionStart-1))){
                    messageList.scrollToPosition(positionStart);

                }
            }
        });
        messageList.setAdapter(adapter);

        return view;
    }

    private void addMessage() {
        Map<String,Object> chat = new HashMap<>();
        chat.put("message",doMessage.getText().toString().trim());
        chat.put("sender",preferences.getUsername());
        chat.put("time", ServerValue.TIMESTAMP);
        chat.put("receiver",getArguments().getString("username"));
        chat.put("imageUrl",null);


        database.getReference("message").child(mAuth.getUid()).child(getArguments().getString("fireId")).push().setValue(chat);
        database.getReference("message").child(getArguments().getString("fireId")).child(mAuth.getUid()).push().setValue(chat);

        doMessage.setText("");
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode== REQUEST_IMAGE){
            if (resultCode==RESULT_OK){
                if (data!=null){
                    final Uri uri = data.getData();

                    final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/"+ UUID.randomUUID().toString());
                    ref.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Map<String,Object> chat = new HashMap<>();
                                    chat.put("message",null);
                                    chat.put("sender",preferences.getUsername());
                                    chat.put("time", ServerValue.TIMESTAMP);
                                    chat.put("receiver",getArguments().getString("username"));
                                    chat.put("imageUrl",ref.toString());

                                    database.getReference("message").child(mAuth.getUid()).child(getArguments().getString("fireId")).push().setValue(chat);
                                    database.getReference("message").child(getArguments().getString("fireId")).child(mAuth.getUid()).push().setValue(chat);
                                    progressDialog.dismiss();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    //Toast.makeText(IssueActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                }
                            });




                }
            }
        }
    }
}
