package com.ocurelab.amame.fragment;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.ocurelab.amame.R;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopicFragment extends Fragment {
    private TextView topicUsername,topicContent,topicTitle;
    private MaterialButton answer;
    private String id;


    public TopicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        topicContent=view.findViewById(R.id.content);
        topicTitle=view.findViewById(R.id.title);
        topicUsername=view.findViewById(R.id.username);
        answer=view.findViewById(R.id.answer);

        if (getArguments() !=null){
            topicUsername.setText(getArguments().getString("username"));
            topicTitle.setText(getArguments().getString("title"));
            topicContent.setText(getArguments().getString("content"));
            id=getArguments().getString("id");
            answer.setOnClickListener(v -> {

            });
        }


        return view;
    }

}
