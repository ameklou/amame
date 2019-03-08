package com.ocurelab.amame.fragment;


import android.content.IntentFilter;
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
import com.google.firebase.auth.FirebaseAuth;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private ImageButton sendMessage;
    private EditText doMessage;
    private Preferences preferences;
    private RecyclerView messageList;
    private LinearLayoutManager linearLayoutManager;
    private List<Chat> chats;
    private RecyclerView.Adapter adapter;
    private String url="https://api.amame.org/api/messages/";
    private FirebaseAuth auth;


    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_message, container, false);

        auth=FirebaseAuth.getInstance();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(auth.getUid());

        sendMessage= view.findViewById(R.id.sendMessage);
        doMessage=view.findViewById(R.id.mess);
        preferences= new Preferences(this.getContext());

        sendMessage.setOnClickListener(v->{
            addMessage();
        });
        new Handler().postDelayed(new Runnable() {
            public void run() {
                getData();
            }
        }, 500L);


        messageList=view.findViewById(R.id.chatView);
        chats=new ArrayList<>();
        adapter=new ChatAdapter(getContext(),chats);
        linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(linearLayoutManager);
        messageList.setItemAnimator(new DefaultItemAnimator());
        messageList.setAdapter(adapter);

        return view;
    }

    private void addMessage(){
        Log.d("username",preferences.getUsername());
       StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.amame.org/api/chats/",
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       doMessage.setText("");
                       chats.clear();
                       getData();

                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {

                   }
               }){
           @Override
            protected Map<String,String> getParams(){
               Map<String,String> params= new HashMap<String,String>();
               params.put("sender",preferences.getUsername());
               params.put("receiver",getArguments().getString("username"));
               params.put("message",doMessage.getText().toString().trim());

               return params;

            }

       };

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        requestQueue.add(stringRequest);

    }

    private void getData() {
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest("https://api.amame.org/api/messages/"+getArguments().getString("userId")+"/" + preferences.getUserId()+"/",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("chatm",response.toString());
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject=response.getJSONObject(i);
                                Chat chat=new Chat();
                                chat.setMessage(jsonObject.getString("message"));
                                chat.setReceiver(jsonObject.getString("receiver"));
                                chat.setSender(jsonObject.getString("sender"));
                                chats.add(chat);
                                scrollToBottom();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter.notifyDataSetChanged();
                        if (adapter.getItemCount() > 1){
                            messageList.getLayoutManager().smoothScrollToPosition(messageList,null,adapter.getItemCount()-1);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        requestQueue.add(jsonArrayRequest);
    }


    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            messageList.getLayoutManager().smoothScrollToPosition(messageList, null, adapter.getItemCount()-1 );
    }
}
