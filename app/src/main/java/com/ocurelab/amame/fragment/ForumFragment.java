package com.ocurelab.amame.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ocurelab.amame.R;
import com.ocurelab.amame.adapter.TopicAdapter;
import com.ocurelab.amame.model.Topic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {
    private RecyclerView forumList;
    private LinearLayoutManager linearLayoutManager;
    private List<Topic> topics;
    private RecyclerView.Adapter adapter;
    private String url ="https://api.amame.org/api/topics/";
    RequestQueue queue;
    String username;


    FloatingActionButton addTopic;
    public ForumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forum, container, false);
        queue=Volley.newRequestQueue(this.getContext());
        forumList=view.findViewById(R.id.forumList);
        topics=new ArrayList<>();
        addTopic=view.findViewById(R.id.add_topic);

        addTopic.setOnClickListener(v->{
            AddTopicFragment addTopicFragment= new AddTopicFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,addTopicFragment).addToBackStack(null).commit();
        });


        TopicAdapter.OnTopicClickedListner listner= (view1,position)->{
            TopicFragment topicFragment= new TopicFragment();
            Bundle args = new Bundle();
            args.putString("title",topics.get(position).getTitle());
            args.putString("content",topics.get(position).getContent());
            args.putString("username",topics.get(position).getUsername());
            args.putString("id",topics.get(position).getId());
            topicFragment.setArguments(args);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,topicFragment).addToBackStack(null).commit();

        };



        adapter=new TopicAdapter(topics,getContext(), listner);
        linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        forumList.setHasFixedSize(true);
        forumList.setLayoutManager(linearLayoutManager);
        forumList.setAdapter(adapter);

        getData();

        return view;
    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("topi",response.toString());
                for(int i=0; i<response.length();i++){
                    try {
                        JSONObject jsonObject=response.getJSONObject(i);
                        Topic topic= new Topic();
                        topic.setTitle(jsonObject.getString("title"));
                        topic.setContent(jsonObject.getString("content"));
                        topic.setUsername(jsonObject.getString("user"));
                        topic.setId(jsonObject.getString("id"));

                        topics.add(topic);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        requestQueue.add(jsonArrayRequest);
    }

}
