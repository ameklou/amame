package com.ocurelab.amame.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ocurelab.amame.R;
import com.ocurelab.amame.adapter.PostAdapter;
import com.ocurelab.amame.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private RecyclerView postList;
    private LinearLayoutManager linearLayoutManager;
    private List<Post> posts;
    private RecyclerView.Adapter adapter;
    private String url = "https://api.amame.org/api/posts/";

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        postList=view.findViewById(R.id.postList);
        posts=new ArrayList<>();

        PostAdapter.OnItemClickListner listner= (view1,position)->{
            Toast.makeText(getContext(),posts.get(position).getTitle(),Toast.LENGTH_SHORT).show();
        };
        adapter=new PostAdapter(posts,getContext(),listner);
        linearLayoutManager= new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        postList.setHasFixedSize(true);
        postList.setLayoutManager(linearLayoutManager);
        postList.setAdapter(adapter);



        getDate();

        return view;
    }

    private void getDate() {
        final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0;i<response.length();i++){
                            try {
                                JSONObject jsonObject=response.getJSONObject(i);
                                Post post= new Post();
                                post.setCategory(jsonObject.getString("category"));
                                post.setTitle(jsonObject.getString("title"));
                                post.setCover(jsonObject.getString("cover"));

                                posts.add(post);

                            }catch (JSONException e){
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }


                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
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
