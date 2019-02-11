package com.ocurelab.amame.fragment;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import jp.wasabeef.richeditor.RichEditor;
import okhttp3.internal.cache.DiskLruCache;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ocurelab.amame.R;
import com.ocurelab.amame.adapter.PostAdapter;
import com.ocurelab.amame.model.Category;
import com.ocurelab.amame.utils.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTopicFragment extends Fragment  {
    private String url = "https://api.amame.org/api/topic_categories/";

    Spinner spinner;
    String categorie;
    RichEditor editor;
    TextInputEditText titre;
    MaterialButton save;
    Preferences preferences;

    public AddTopicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_topic, container, false);
         spinner=view.findViewById(R.id.category);

         titre=view.findViewById(R.id.topic_title);
         preferences=new Preferences(this.getContext());


        editor= view.findViewById(R.id.editor);
        editor.setEditorHeight(200);
        editor.setEditorFontSize(22);
        editor.setEditorFontColor(Color.RED);
        editor.setPadding(10, 10, 10, 10);
        editor.setPlaceholder("Votre message...");
        Log.d("toke",preferences.getUserId());

        save=view.findViewById(R.id.save);
        save.setOnClickListener(v -> {
            sendData();
        });

        return view;
    }

    private void sendData() {
        StringRequest stringRequest= new StringRequest(Request.Method.POST, "https://api.amame.org/api/add_topic/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),response.toString(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params=new HashMap<String,String>();
                params.put("content",editor.getHtml().toString().trim());
                params.put("title",titre.getText().toString().trim());
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


}
