package com.ocurelab.amame.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ocurelab.amame.R;
import com.ocurelab.amame.adapter.UserAdapter;
import com.ocurelab.amame.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    private RecyclerView userList;
    private LinearLayoutManager linearLayoutManager;
    private List<User> users;
    private RecyclerView.Adapter adapter;
    private String url="https://api.amame.org/api/users/";
    RequestQueue queue;


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);
        queue= Volley.newRequestQueue(this.getContext());
        userList=view.findViewById(R.id.userList);
        users=new ArrayList<>();


        UserAdapter.OnItemClickListner listner= new UserAdapter.OnItemClickListner() {
            @Override
            public void onClick(View view, int position) {

                MessageFragment messageFragment= new MessageFragment();
                Bundle args= new Bundle();
                args.putString("username",users.get(position).getUsername());
                args.putString("userId",users.get(position).getId());
                args.putString("bio",users.get(position).getBio());
                args.putString("phone",users.get(position).getPhone());
                args.putString("fireId",users.get(position).getFireId());
                messageFragment.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,messageFragment).addToBackStack(null).commit();
            }
        };

        adapter= new UserAdapter(users,getContext(),listner);
        linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        userList.setHasFixedSize(true);
        userList.setLayoutManager(linearLayoutManager);
        userList.setAdapter(adapter);
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

                for(int i=0; i<response.length();i++){
                    try {
                        JSONObject jsonObject=response.getJSONObject(i);
                        User user= new User();
                        user.setUsername(jsonObject.getString("username"));
                        user.setId(jsonObject.getString("id"));
                        user.setBio(jsonObject.getString("bio"));
                        user.setPhone(jsonObject.getString("phone"));
                        user.setFireId(jsonObject.getString("fireId"));

                        users.add(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



            }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
        }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(jsonArrayRequest);
    }

}
