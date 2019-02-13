package com.ocurelab.amame.fragment;


import android.os.Bundle;

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
import com.ocurelab.amame.R;
import com.ocurelab.amame.adapter.AnswerAdapter;
import com.ocurelab.amame.model.Answer;
import com.ocurelab.amame.model.Post;
import com.ocurelab.amame.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerFragment extends Fragment {


    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Answer> answerList;
    private RecyclerView.Adapter adapter;
    private EditText message;
    private ImageButton sendMessage;
    private Preferences preferences;



    public AnswerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_answer, container, false);
        message=view.findViewById(R.id.message);
        sendMessage=view.findViewById(R.id.sendMessage);
        preferences=new Preferences(getContext());

        if (getArguments() !=null){

            loadAnswers(getArguments().getString("topicId"));



        }
        sendMessage.setOnClickListener(v -> {
            sendAnser();
        });
        answerList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.answerList);
        linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        adapter=new AnswerAdapter(answerList,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void sendAnser() {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, "https://api.amame.org/api/answers/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("respo",response.toString());

                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() > 1){
                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,null,adapter.getItemCount()-1);
                }
                answerList.clear();
                loadAnswers(getArguments().getString("topicId"));
                message.setText("");
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.d("Erro",error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params=new HashMap<String,String>();
                params.put("message",message.getText().toString().trim());
                params.put("topic",getArguments().getString("topicId"));
                params.put("owner",preferences.getUserId());

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

    private void loadAnswers(String topicId) {
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest("https://api.amame.org/answer/"+topicId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject=response.getJSONObject(i);
                                Answer answer= new Answer();
                                answer.setMessage(jsonObject.getString("message"));
                                answer.setOwner(jsonObject.getString("owner"));
                                answer.setTopic(jsonObject.getString("topic"));


                                answerList.add(answer);

                            }catch (JSONException e){
                                e.printStackTrace();

                            }
                        }


                        adapter.notifyDataSetChanged();

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

}
