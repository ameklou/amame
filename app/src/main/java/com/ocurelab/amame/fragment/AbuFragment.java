package com.ocurelab.amame.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ocurelab.amame.R;
import com.ocurelab.amame.utils.Preferences;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AbuFragment extends Fragment {
    TextInputEditText title, description, contact, city;
    MaterialButton sendReport;
    Preferences preferences;
    RequestQueue requestQueue;
    private boolean temoinState;
    private CheckBox temoin;


    public AbuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_abu, container, false);
        title = view.findViewById(R.id.title);
        description = view.findViewById(R.id.description);
        temoin = view.findViewById(R.id.temoin);
        contact = view.findViewById(R.id.contact);
        city = view.findViewById(R.id.city);
        sendReport = view.findViewById(R.id.sendReport);
        preferences = new Preferences(getContext());
         requestQueue = Volley.newRequestQueue(this.getContext());

        sendReport.setOnClickListener(v -> {
            addReport();
        });

        return view;
    }

    private void addReport() {
        if (temoin.isChecked()){
            temoinState=true;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.amame.org/api/reports/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(),"Report envoye un administrator vous contactera.",Toast.LENGTH_SHORT).show();
                        MainFragment mainFragment=new MainFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,mainFragment).commit();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Veillez remplir tous les champs",Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title.getText().toString().trim());
                params.put("description", description.getText().toString().trim());
                params.put("user", preferences.getUserId());
                params.put("contact",contact.getText().toString().trim());
                params.put("city",city.getText().toString().trim());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + preferences.getToken());
                return params;
            }


        };
        requestQueue.add(stringRequest);

    }
}
