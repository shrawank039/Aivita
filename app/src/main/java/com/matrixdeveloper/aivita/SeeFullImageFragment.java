package com.matrixdeveloper.aivita;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import com.matrixdeveloper.aivita.R;

public class SeeFullImageFragment extends Fragment {

    View view;
    Context context;
    ImageButton close_gallery;
    ImageView single_image;
    String image_url;
    ProgressBar p_bar;
    int width, height;

    public SeeFullImageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_see_full_image, container, false);
        context = getContext();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        assert getArguments() != null;
        image_url = getArguments().getString("image_url");

        close_gallery = view.findViewById(R.id.close_gallery);
        close_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        p_bar = view.findViewById(R.id.p_bar);

        single_image = view.findViewById(R.id.single_image);


        p_bar.setVisibility(View.VISIBLE);

        if (!image_url.equalsIgnoreCase("")) {
            Picasso.get().
                    load(image_url).placeholder(R.drawable.image_placeholder)
                    .into(single_image, new Callback() {
                        @Override
                        public void onSuccess() {

                            p_bar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            // TODO Auto-generated method stub
                            p_bar.setVisibility(View.GONE);
                        }
                    });
             return view;
        }else {
            Toast.makeText(context, "Don't have profile pic!!!", Toast.LENGTH_SHORT).show();
            return null;
        }

    }


}


