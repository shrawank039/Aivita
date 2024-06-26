package com.matrixdeveloper.aivita.Discover;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matrixdeveloper.aivita.Home.HomeModel;
import com.matrixdeveloper.aivita.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.matrixdeveloper.aivita.Profile.MyVideos_Adapter;
import com.matrixdeveloper.aivita.R;
import com.matrixdeveloper.aivita.SimpleClasses.ApiRequest;
import com.matrixdeveloper.aivita.SimpleClasses.Callback;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;
import com.matrixdeveloper.aivita.WatchVideos.WatchVideos_F;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverVideo_F extends RootFragment {

    public RecyclerView recyclerView;
     ArrayList<HomeModel> data_list;
     MyVideos_Adapter adapter;
     View view;
     Context context;
     ImageView back_btn;
     private String user_id;
     private String tag_name="",endID="";
    private int scrollOutItems;
    private int end=11, endCount=12;

    RelativeLayout no_data_layout;
     public static int myvideo_count=0;

    public DiscoverVideo_F() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_discover_video, container, false);

        context=getContext();

        view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tag_name = bundle.getString("tag_name", "");
        }


        recyclerView=view.findViewById(R.id.recylerview);
        final GridLayoutManager layoutManager = new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if (data_list.size()-5 == scrollOutItems){

                    Call_Api_For_get_Allvideos(endID);
                }

            }
        });



        data_list=new ArrayList<>();
        adapter=new MyVideos_Adapter(context, data_list, new MyVideos_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, HomeModel item, View view) {

                OpenWatchVideo(postion);

            }
        });

        recyclerView.setAdapter(adapter);

        no_data_layout=view.findViewById(R.id.no_data_layout);


        Call_Api_For_get_Allvideos(endID);



        return view;

    }

    Boolean isVisibleToUser=false;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser=isVisibleToUser;
        if(view!=null && isVisibleToUser){
            Call_Api_For_get_Allvideos(endID);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if((view!=null && isVisibleToUser) && (!data_list.isEmpty() && !is_api_run)){
            Call_Api_For_get_Allvideos(endID);
        }
    }


    Boolean is_api_run=false;
    //this will get the all videos data of user and then parse the data
    private void Call_Api_For_get_Allvideos(String endID) {
        is_api_run=true;
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("tag_name", tag_name);
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,""));
            parameters.put("type", "#");
            parameters.put("end", "25");
            parameters.put("end_id", endID);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.getTaggedVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                is_api_run=false;
                Parse_data(resp);
            }
        });


    }

    public void Parse_data(String responce){

        data_list.clear();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){

                JSONArray user_videos=jsonObject.getJSONArray("msg");

                if(!String.valueOf(user_videos.length()).equals("0")){

                    no_data_layout.setVisibility(View.GONE);

                    for (int i=0;i<user_videos.length();i++) {
                        JSONObject itemdata = user_videos.optJSONObject(i);

                        HomeModel item=new HomeModel();
                        item.fb_id=user_id;

                        endID = itemdata.optString("id");

                        JSONObject user_info=itemdata.optJSONObject("user_info");

                        item.first_name=user_info.optString("first_name");
                        item.last_name=user_info.optString("last_name");
                        item.profile_pic=user_info.optString("profile_pic");

                        Log.d("resp", item.fb_id+" "+item.first_name);

                        JSONObject count=itemdata.optJSONObject("count");
                        item.like_count=count.optString("like_count");
                        item.video_comment_count=count.optString("video_comment_count");
                        item.views=count.optString("view");

                        JSONObject sound_data=itemdata.optJSONObject("sound");
                        item.sound_id=sound_data.optString("id");
                        item.sound_name=sound_data.optString("sound_name");
                        item.sound_pic=sound_data.optString("thum");


                        item.video_id=itemdata.optString("id");
                        item.liked=itemdata.optString("liked");
                        item.gif=Variables.base_url+itemdata.optString("gif");
                        item.video_url=Variables.base_url+itemdata.optString("video");
                        item.thum=Variables.base_url+itemdata.optString("thum");
                        item.created_date=itemdata.optString("created");

                        item.video_description=itemdata.optString("description");

                        data_list.add(item);
                    }

                    myvideo_count=data_list.size();

                }else {
                    no_data_layout.setVisibility(View.VISIBLE);
                }




                adapter.notifyDataSetChanged();

            }else {
               // Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
        //    Toast.makeText(context, "Something wrong with Api", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }





    private void OpenWatchVideo(int postion) {

        Intent intent=new Intent(getActivity(), WatchVideos_F.class);
        intent.putExtra("arraylist", data_list);
        intent.putExtra("position",postion);
        startActivity(intent);

    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }
}
