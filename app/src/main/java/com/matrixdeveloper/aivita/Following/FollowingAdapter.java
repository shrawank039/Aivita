package com.matrixdeveloper.aivita.Following;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import com.matrixdeveloper.aivita.R;

import java.util.ArrayList;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.CustomViewHolder> {
    public Context context;

    String following_or_fans;

    ArrayList<Following_Get_Set> datalist;

    public interface OnItemClickListener {
        void onItemClick(View view, int postion, Following_Get_Set item);
    }

    public FollowingAdapter.OnItemClickListener listener;

    public FollowingAdapter(Context context, String following_or_fans, ArrayList<Following_Get_Set> arrayList, FollowingAdapter.OnItemClickListener listener) {
        this.context = context;
        this.following_or_fans = following_or_fans;
        datalist = arrayList;
        this.listener = listener;
    }

    @Override
    public FollowingAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_following, viewGroup, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        FollowingAdapter.CustomViewHolder viewHolder = new FollowingAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView user_image;
        TextView user_name;
        TextView user_id;
        TextView action_txt;
        RelativeLayout mainlayout;

        public CustomViewHolder(View view) {
            super(view);

            mainlayout = view.findViewById(R.id.mainlayout);

            user_image = view.findViewById(R.id.user_image);
            user_name = view.findViewById(R.id.user_name);
            user_id = view.findViewById(R.id.user_id);

            action_txt = view.findViewById(R.id.action_txt);
        }

        public void bind(final int pos, final Following_Get_Set item, final FollowingAdapter.OnItemClickListener listener) {

            mainlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });

            action_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pos, item);
                }
            });

        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final FollowingAdapter.CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);

        Following_Get_Set item = datalist.get(i);

        holder.user_name.setText(item.first_name + " " + item.last_name);

        if (!item.profile_pic.equalsIgnoreCase("")) {
            Picasso.get()
                    .load(item.profile_pic)
                    .placeholder(R.drawable.profile_image_placeholder)
                    .into(holder.user_image);
        }

        holder.user_id.setText(item.username);

        if (item.is_show_follow_unfollow_btn) {
            holder.action_txt.setVisibility(View.VISIBLE);

            if (following_or_fans.equals("following")) {

                if (item.follow.equals("0")) {
                    holder.action_txt.setText("Follow");
                    holder.action_txt.setBackgroundColor(ContextCompat.getColor(context, R.color.redcolor));
                    holder.action_txt.setTextColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    holder.action_txt.setText("UnFollow");
                    holder.action_txt.setBackground(ContextCompat.getDrawable(context, R.drawable.d_gray_border));
                    holder.action_txt.setTextColor(ContextCompat.getColor(context, R.color.black));
                }


            } else {

                if (item.follow.equals("0")) {
                    holder.action_txt.setText("Follow");
                    holder.action_txt.setBackgroundColor(ContextCompat.getColor(context, R.color.redcolor));
                    holder.action_txt.setTextColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    holder.action_txt.setText("Friends");
                    holder.action_txt.setBackground(ContextCompat.getDrawable(context, R.drawable.d_gray_border));
                    holder.action_txt.setTextColor(ContextCompat.getColor(context, R.color.black));
                }
            }

        } else holder.action_txt.setVisibility(View.GONE);

        holder.bind(i, datalist.get(i), listener);

    }

}