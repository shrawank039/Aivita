package com.matrixdeveloper.aivita.Profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.matrixdeveloper.aivita.Chat.Chat_Activity;
import com.matrixdeveloper.aivita.SeeFullImageFragment;
import com.squareup.picasso.Picasso;

import com.matrixdeveloper.aivita.Following.FollowingFragment;
import com.matrixdeveloper.aivita.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.matrixdeveloper.aivita.Profile.Liked_Videos.Liked_Video_F;
import com.matrixdeveloper.aivita.Profile.UserVideos.UserVideo_F;
import com.matrixdeveloper.aivita.R;

import com.matrixdeveloper.aivita.SimpleClasses.API_CallBack;
import com.matrixdeveloper.aivita.SimpleClasses.ApiRequest;
import com.matrixdeveloper.aivita.SimpleClasses.Callback;
import com.matrixdeveloper.aivita.SimpleClasses.Fragment_Callback;
import com.matrixdeveloper.aivita.SimpleClasses.Functions;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileFragment extends RootFragment implements View.OnClickListener {

    private View view;
    private Context context;

    private TextView follow_unfollow_btn;
    private TextView username,name, video_count_txt;
    private ImageView imageView, batchImage;
    private TextView follow_count_txt, fans_count_txt, heart_count_txt;
    private ImageView back_btn, setting_btn;

    String user_id, user_name, user_pic;
    Bundle bundle;

    protected TabLayout tabLayout;
    protected ViewPager pager;
    private ViewPagerAdapter adapter;
    public boolean isdataload = false;

    RelativeLayout tabs_main_layout;
    LinearLayout top_layout;

    public static String pic_url, batch_url;
    public ProfileFragment() {}

    Fragment_Callback fragment_callback;

    @SuppressLint("ValidFragment")
    public ProfileFragment(Fragment_Callback fragment_callback) {
        this.fragment_callback = fragment_callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getContext();

        bundle = getArguments();
        if (bundle != null) {
            user_id = bundle.getString("user_id");
            user_name = bundle.getString("user_name");
            user_pic = bundle.getString("user_pic");
        }

        return init();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_image:
                OpenfullsizeImage(pic_url);
                break;

            case R.id.follow_unfollow_btn:

                if (Variables.sharedPreferences.getBoolean(Variables.islogin, false))
                    Follow_unFollow_User();
                else
                    Toast.makeText(context, "Please login in to app", Toast.LENGTH_SHORT).show();

                break;

            case R.id.setting_btn:
                Open_Setting();
                break;

            case R.id.following_layout:
                Open_Following();
                break;

            case R.id.fans_layout:
                Open_Followers();
                break;

            case R.id.back_btn:
                try {
                    Objects.requireNonNull(getActivity()).onBackPressed();
                    break;
                }catch (Exception ignored){
                }
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    public View init() {

        name = view.findViewById(R.id.name);
        username=view.findViewById(R.id.username);
        imageView = view.findViewById(R.id.user_image);
        batchImage = view.findViewById(R.id.batchImage);
        imageView.setOnClickListener(this);

        video_count_txt = view.findViewById(R.id.video_count_txt);

        follow_count_txt = view.findViewById(R.id.follow_count_txt);
        fans_count_txt = view.findViewById(R.id.fan_count_txt);
        heart_count_txt = view.findViewById(R.id.heart_count_txt);


        setting_btn = view.findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);

        back_btn = view.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        follow_unfollow_btn = view.findViewById(R.id.follow_unfollow_btn);
        follow_unfollow_btn.setOnClickListener(this);


        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);

        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        setupTabIcons();


        tabs_main_layout = view.findViewById(R.id.tabs_main_layout);
        top_layout = view.findViewById(R.id.top_layout);


        ViewTreeObserver observer = top_layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final int height = top_layout.getMeasuredHeight();

                top_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);

                ViewTreeObserver observer = tabs_main_layout.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tabs_main_layout.getLayoutParams();
                        params.height = (int) (tabs_main_layout.getMeasuredHeight() + height);
                        tabs_main_layout.setLayoutParams(params);
                        tabs_main_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                                this);

                    }
                });

            }
        });


        view.findViewById(R.id.following_layout).setOnClickListener(this);
        view.findViewById(R.id.fans_layout).setOnClickListener(this);

        isdataload = true;
        Call_Api_For_get_Allvideos();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (is_run_first_time) {

            Call_Api_For_get_Allvideos();

        }

    }

    private void setupTabIcons() {

        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView1 = view1.findViewById(R.id.image);
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_view_carousel_black_24dp));
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView2 = view2.findViewById(R.id.image);
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        tabLayout.getTabAt(1).setCustomView(view2);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);

                switch (tab.getPosition()) {
                    case 0:

                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_view_carousel_black_24dp));
                        break;

                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                        break;
                }
                tab.setCustomView(v);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v = tab.getCustomView();
                ImageView image = v.findViewById(R.id.image);

                switch (tab.getPosition()) {
                    case 0:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_view_carousel_black_24dp));
                        break;
                    case 1:
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                        break;
                }

                tab.setCustomView(v);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final Resources resources;

        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();


        public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
            super(fm);
            this.resources = resources;
        }

        @Override
        public Fragment getItem(int position) {
            final Fragment result;
            switch (position) {
                case 0:
                    result = new UserVideo_F(user_id);
                    break;
                case 1:
                    result = new Liked_Video_F(user_id);
                    break;

                default:
                    result = null;
                    break;
            }

            return result;
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(final int position) {
            return null;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }


        /**
         * Get the Fragment by position
         *
         * @param position tab position of the fragment
         * @return
         */
        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }


    }


    boolean is_run_first_time = false;

    private void Call_Api_For_get_Allvideos() {

        if (bundle == null) {
            user_id = Variables.sharedPreferences.getString(Variables.u_id, "0");
        }

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("my_fb_id", Variables.sharedPreferences.getString(Variables.u_id, ""));
            parameters.put("fb_id", user_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(context, Variables.showMyAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                is_run_first_time = true;
                Parse_data(resp);
            }
        });


    }

    public void Parse_data(String responce) {


        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msgArray = jsonObject.getJSONArray("msg");

                JSONObject data = msgArray.getJSONObject(0);
                JSONObject user_info = data.optJSONObject("user_info");
                assert user_info != null;
                String a=user_info.optString("first_name") + " " + user_info.optString("last_name");
                name.setText(a);
                String b="@"+user_info.optString("username");
                username.setText(b);

                ProfileFragment.pic_url = user_info.optString("profile_pic");
                if (!ProfileFragment.pic_url.equalsIgnoreCase("")) {
                    Picasso.get()
                            .load(ProfileFragment.pic_url)
                            .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                            .resize(200, 200).centerCrop().into(imageView);
                }

                ProfileFragment.batch_url = user_info.optString("batch_url");
                if (!ProfileFragment.batch_url.equalsIgnoreCase("")) {
                    batchImage.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(ProfileFragment.batch_url)
                            //.placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                            .resize(30, 30).centerInside().into(batchImage);
                }

                follow_count_txt.setText(data.optString("total_following"));
                fans_count_txt.setText(data.optString("total_fans"));
                heart_count_txt.setText(data.optString("total_heart"));


                if (!data.optString("fb_id").
                        equals(Variables.sharedPreferences.getString(Variables.u_id, ""))) {

                    follow_unfollow_btn.setVisibility(View.VISIBLE);
                    JSONObject follow_Status = data.optJSONObject("follow_Status");
                    follow_unfollow_btn.setText(follow_Status.optString("follow_status_button"));
                    follow_status = follow_Status.optString("follow");
                }


                JSONArray user_videos = data.getJSONArray("user_videos");
                if (!user_videos.toString().equals("[" + "0" + "]")) {
                    video_count_txt.setText(user_videos.length() + " Videos");

                }


            } else {
           //     Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
        //    Toast.makeText(context, "Something wrong with Api", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    public void Open_Setting() {

        Open_Chat_F();

    }


    public String follow_status = "0";

    public void Follow_unFollow_User() {

        final String send_status;
        if (follow_status.equals("0")) {
            send_status = "1";
        } else {
            send_status = "0";
        }

        Functions.Call_Api_For_Follow_or_unFollow(getActivity(),
                Variables.sharedPreferences.getString(Variables.u_id, ""), user_id, send_status,
                new API_CallBack() {
                    @Override
                    public void ArrayData(ArrayList arrayList) {}

                    @Override
                    public void OnSuccess(String responce) {

                        if (send_status.equals("1")) {
                            follow_unfollow_btn.setText("UnFollow");
                            follow_status = "1";

                        } else if (send_status.equals("0")) {
                            follow_unfollow_btn.setText("Follow");
                            follow_status = "0";
                        }

                        Call_Api_For_get_Allvideos();
                    }

                    @Override
                    public void OnFail(String responce) { }

                });


    }


    //this method will get the big size of profile image.
    public void OpenfullsizeImage(String url) {
        if (!url.equalsIgnoreCase("")) {
            SeeFullImageFragment see_image_f = new SeeFullImageFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            Bundle args = new Bundle();
            args.putSerializable("image_url", url);
            see_image_f.setArguments(args);
            transaction.addToBackStack(null);

            View view = getActivity().findViewById(R.id.MainMenuFragment);
            if (view != null)
                transaction.replace(R.id.MainMenuFragment, see_image_f).commit();
            else
                transaction.replace(R.id.Profile_F, see_image_f).commit();
        }else {
            Toast.makeText(context, "Don't have profile pic!!!", Toast.LENGTH_SHORT).show();
        }

    }


    public void Open_Chat_F() {

        Chat_Activity chat_activity = new Chat_Activity();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("user_id", user_id);
        args.putString("user_name", user_name);
        args.putString("user_pic", user_pic);
        chat_activity.setArguments(args);
        transaction.addToBackStack(null);

        View view = getActivity().findViewById(R.id.MainMenuFragment);
        if (view != null)
            transaction.replace(R.id.MainMenuFragment, chat_activity).commit();
        else
            transaction.replace(R.id.Profile_F, chat_activity).commit();


    }


    public void Open_Following() {

        FollowingFragment following_f = new FollowingFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", user_id);
        args.putString("from_where", "following");
        following_f.setArguments(args);
        transaction.addToBackStack(null);


        View view = getActivity().findViewById(R.id.MainMenuFragment);

        if (view != null)
            transaction.replace(R.id.MainMenuFragment, following_f).commit();
        else
            transaction.replace(R.id.Profile_F, following_f).commit();


    }

    private void Open_Followers() {

        FollowingFragment following_f = new FollowingFragment();
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", user_id);
        args.putString("from_where", "fan");
        following_f.setArguments(args);
        transaction.addToBackStack(null);
        View view = getActivity().findViewById(R.id.MainMenuFragment);
        if (view != null) transaction.replace(R.id.MainMenuFragment, following_f).commit();
        else transaction.replace(R.id.Profile_F, following_f).commit();

    }


    @Override
    public void onDetach() {
        super.onDetach();

        if (fragment_callback != null)
            fragment_callback.Responce(new Bundle());

        Functions.deleteCache(context);

    }


}
