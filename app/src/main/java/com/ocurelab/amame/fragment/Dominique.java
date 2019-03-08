package com.ocurelab.amame.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ocurelab.amame.R;
import com.ocurelab.amame.adapter.DomAdapter;
import com.ocurelab.amame.model.DomMessage;
import com.ocurelab.amame.utils.Preferences;
import com.ocurelab.amame.utils.Util;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dominique extends Fragment {
    private ImageButton sendMessage;
    private TextInputEditText doMessage;
    private Preferences preferences;
    private RecyclerView messageList;
    private LinearLayoutManager linearLayoutManager;
    private List<DomMessage> domMessages;
    private RecyclerView.Adapter adapter;
    private String url = "https://api.amame.org/domi/";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;



    public Dominique() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dominique, container, false);
        sendMessage= view.findViewById(R.id.sendMessage);
        doMessage=view.findViewById(R.id.mess);
        preferences= new Preferences(this.getContext());
        database = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();




        sendMessage.setOnClickListener(v -> {
            addMessage();
        });

        messageList=view.findViewById(R.id.dom_view);
        domMessages=new ArrayList<>();
        linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(linearLayoutManager);


        database.getReference("dominique").child(mAuth.getUid()).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        DomMessage domMessage= dataSnapshot.getValue(DomMessage.class);
                        domMessages.add(domMessage);
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

        adapter= new DomAdapter(domMessages,this.getContext());


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
        adapter.notifyDataSetChanged();












        return view;
    }



//    private void getData() {
//        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest("https://api.amame.org/domi/" + preferences.getUserId(),
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("dom",response.toString());
//                        for (int i=0;i<response.length();i++){
//                            try {
//                                JSONObject jsonObject=response.getJSONObject(i);
//                                DomMessage domMessage=new DomMessage();
//                                domMessage.setMessage(jsonObject.getString("message"));
//                                domMessage.setReceiver(jsonObject.getBoolean("receiver"));
//                                domMessages.add(domMessage);
//                                scrollToBottom();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                        adapter.notifyDataSetChanged();
//                        if (adapter.getItemCount() > 1){
//                            messageList.getLayoutManager().smoothScrollToPosition(messageList,null,adapter.getItemCount()-1);
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
//        requestQueue.add(jsonArrayRequest);
//    }


    private void addMessage(){
        Map<String,Object> dominique = new HashMap<>();
        dominique.put("message",doMessage.getText().toString().trim());
        dominique.put("user",preferences.getUsername());
        dominique.put("time", ServerValue.TIMESTAMP);
        dominique.put("receiver",false);


        database.getReference("dominique").child(mAuth.getUid()).push().setValue(dominique);
        doMessage.setText("");

//        StringRequest stringRequest= new StringRequest(Request.Method.POST, "https://api.amame.org/api/domis/", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("ohoo",response.toString());
//                adapter.notifyDataSetChanged();
//                if (adapter.getItemCount() > 1){
//                    messageList.getLayoutManager().smoothScrollToPosition(messageList,null,adapter.getItemCount()-1);
//                }
//                domMessages.clear();
//                getData();
//                doMessage.setText("");
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }){
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params=new HashMap<String,String>();
//                params.put("message",doMessage.getText().toString().trim());
//                //params.put("receiver","false");
//                params.put("user",preferences.getUserId());
//
//                return params;
//
//        }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("Content-Type","application/x-www-form-urlencoded");
//                params.put("Authorization","Bearer "+preferences.getToken());
//                return params;
//            }
//    };
//        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
//        requestQueue.add(stringRequest);

}



    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStart() {
        super.onStart();


    }
}
