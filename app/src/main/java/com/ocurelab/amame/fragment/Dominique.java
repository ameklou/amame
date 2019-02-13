package com.ocurelab.amame.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
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
import com.ocurelab.amame.R;
import com.ocurelab.amame.adapter.DomAdapter;
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
    private EditText doMessage;
    private Preferences preferences;
    private RecyclerView messageList;
    private LinearLayoutManager linearLayoutManager;
    private List<DomMessage> domMessages;
    private RecyclerView.Adapter adapter;
    private String url = "https://api.amame.org/domi/";
    private BroadcastReceiver mRegistrationBroadcastReceiver;



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


        new Handler().postDelayed(new Runnable() {
            public void run() {
                getData();
            }
        }, 500L);


        sendMessage.setOnClickListener(v -> {
            addMessage();
        });

        messageList=view.findViewById(R.id.dom_view);
        domMessages=new ArrayList<>();
        adapter=new DomAdapter(domMessages,getContext());
        linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(linearLayoutManager);
        messageList.setItemAnimator(new DefaultItemAnimator());
        messageList.setAdapter(adapter);
        //messageList.scrollToPosition(domMessages.size() - 1);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Util.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };


        return view;
    }

    private void handlePushNotification(Intent intent) {
        DomMessage domMessage= (DomMessage) intent.getSerializableExtra("domMassage");
        if (domMessage!=null){
            domMessages.add(domMessage);
            adapter.notifyDataSetChanged();
            if (adapter.getItemCount() > 1){
                messageList.getLayoutManager().smoothScrollToPosition(messageList,null,adapter.getItemCount()-1);
            }
        }

    }

    private void getData() {
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest("https://api.amame.org/domi/" + preferences.getUserId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("dom",response.toString());
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject=response.getJSONObject(i);
                                DomMessage domMessage=new DomMessage();
                                domMessage.setMessage(jsonObject.getString("message"));
                                domMessage.setReceiver(jsonObject.getBoolean("receiver"));
                                domMessages.add(domMessage);
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


    private void addMessage(){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, "https://api.amame.org/api/domis/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ohoo",response.toString());
                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() > 1){
                    messageList.getLayoutManager().smoothScrollToPosition(messageList,null,adapter.getItemCount()-1);
                }
                domMessages.clear();
                getData();
                doMessage.setText("");
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params=new HashMap<String,String>();
                params.put("message",doMessage.getText().toString().trim());
                //params.put("receiver","false");
                params.put("user",preferences.getUserId());

                return params;

        }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization","Bearer "+preferences.getToken());
                return params;
            }
    };
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        requestQueue.add(stringRequest);

}

    private void scrollToBottom() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            messageList.getLayoutManager().smoothScrollToPosition(messageList, null, adapter.getItemCount()-1 );
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Util.PUSH_NOTIFICATION));

       // NotificationUtils.clearNotifications();
    }
}
