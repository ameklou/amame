package com.ocurelab.amame.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ocurelab.amame.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {
    private TextView artCategory, artContent, artTitle;
    private ImageView artCover;

    public ArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        artCategory=view.findViewById(R.id.category);
        artContent=view.findViewById(R.id.content);
        artTitle=view.findViewById(R.id.title);
        artCover=view.findViewById(R.id.cover);

        if (getArguments()!=null){
            String title = getArguments().getString("title");
            String content =getArguments().getString("content");
            String category=getArguments().getString("category");
            String cover= getArguments().getString("cover");

            artTitle.setText(title);
            artContent.setText(content);
            artCategory.setText(category);
            Picasso.get().load(cover).into(artCover);
        }
        return view;
    }

}
