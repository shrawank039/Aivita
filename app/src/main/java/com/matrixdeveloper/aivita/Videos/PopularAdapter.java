package com.matrixdeveloper.aivita.Videos;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import com.matrixdeveloper.aivita.R;

import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.CustomViewHolder > {

    public Context context;
    private PopularAdapter.OnItemClickListener listener;
    private ArrayList<Popular_Get_Set> dataList;



    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, Popular_Get_Set item, View view);
    }



    public PopularAdapter(Context context, ArrayList<Popular_Get_Set> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public PopularAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        PopularAdapter.CustomViewHolder viewHolder = new PopularAdapter.CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }



    @Override
    public void onBindViewHolder(final PopularAdapter.CustomViewHolder holder, final int i) {
        final Popular_Get_Set item= dataList.get(i);
        holder.setIsRecyclable(false);

        holder.bind(i, item, listener);
        String a = item.first_name + " " + item.last_name;
        holder.username.setText(a);

        //  Toast.makeText(context, item.like_count, Toast.LENGTH_SHORT).show();
        holder.like_txt.setText(item.like_count);
        holder.comment_txt.setText(item.video_comment_count);


        if (item.liked.equalsIgnoreCase("1"))
            holder.like_image.setImageDrawable(context.getDrawable(R.drawable.ic_heart));
        else
            holder.like_image.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_black_24dp));

        String b = "original sound - " + item.first_name + " " + item.last_name;
        if ((item.sound_name == null || item.sound_name.equals("") || item.sound_name.equals("null")))
            holder.sound_name.setText(b);
        else holder.sound_name.setText(item.sound_name);

        holder.sound_name.setSelected(true);
        holder.desc_txt.setText(item.video_description);

        if (!item.profile_pic.equals("")){
            Picasso.get().
                    load(item.profile_pic)
                    .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                    .resize(100, 100).into(holder.user_pic);}

        if ((item.sound_name == null || item.sound_name.equals("")) || item.sound_name.equals("null")){
            item.sound_pic = item.profile_pic;
            if (!item.sound_pic.equals("")) {
                Picasso.get().
                        load(item.sound_pic)
                        .placeholder(context.getResources().getDrawable(R.drawable.ic_round_music))
                        .resize(100, 100).into(holder.sound_image);
            }
        }
        else if (item.sound_pic.equals(""))
            item.sound_pic = "Null";
    }



    class CustomViewHolder extends RecyclerView.ViewHolder {

        PlayerView playerview;
        TextView username,desc_txt,sound_name;
        ImageView user_pic,sound_image;

        LinearLayout like_layout,comment_layout,shared_layout;
        ImageView like_image,comment_image;
        TextView like_txt,comment_txt;


        public CustomViewHolder(View view) {
            super(view);

            playerview=view.findViewById(R.id.playerview);

            username=view.findViewById(R.id.username);
            user_pic=view.findViewById(R.id.user_pic);
            sound_name=view.findViewById(R.id.sound_name);
            sound_image=view.findViewById(R.id.sound_image);

            like_layout=view.findViewById(R.id.like_layout);
            like_image=view.findViewById(R.id.like_image);
            like_txt=view.findViewById(R.id.like_txt);


            desc_txt=view.findViewById(R.id.desc_txt);

            comment_layout=view.findViewById(R.id.comment_layout);
            comment_image=view.findViewById(R.id.comment_image);
            comment_txt=view.findViewById(R.id.comment_txt);


            shared_layout=view.findViewById(R.id.shared_layout);
        }

        public void bind(final int postion, final Popular_Get_Set item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });


            user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });


            like_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (item.liked.equalsIgnoreCase("1")) {
                        //  Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
                        like_image.setImageDrawable(context.getDrawable(R.drawable.ic_favorite_black_24dp));
                        listener.onItemClick(postion, item, v);
                        item.liked="0";
                    } else {
                        //  Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
                        like_image.setImageDrawable(context.getDrawable(R.drawable.ic_heart));
                        listener.onItemClick(postion, item, v);
                        item.liked="1";
                    }
                }
            });

            comment_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });

            shared_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });



        }


    }


}