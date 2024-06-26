package com.matrixdeveloper.aivita.Profile;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.matrixdeveloper.aivita.Following.FollowingFragment;
import com.matrixdeveloper.aivita.Main_Menu.MainMenuActivity;
import com.matrixdeveloper.aivita.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.matrixdeveloper.aivita.Profile.Liked_Videos.Liked_Video_F;
import com.matrixdeveloper.aivita.Profile.UserVideos.UserVideo_F;
import com.matrixdeveloper.aivita.R;
import com.matrixdeveloper.aivita.SeeFullImageFragment;
import com.matrixdeveloper.aivita.SimpleClasses.ApiRequest;
import com.matrixdeveloper.aivita.SimpleClasses.Callback;
import com.matrixdeveloper.aivita.SimpleClasses.Fragment_Callback;
import com.matrixdeveloper.aivita.SimpleClasses.Functions;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matrixdeveloper.aivita.Wallet.WalletActivity;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Tab_F extends RootFragment implements View.OnClickListener  {
    View view;
    Context context;


    private TextView username,video_count_txt,name;
    private ImageView imageView, batchImage;
    private LinearLayout instagram,facebook,youttube;
    public  TextView follow_count_txt,fans_count_txt,heart_count_txt;
    ImageView setting_btn;
    Bundle bundle;
    protected TabLayout tabLayout;
    protected ViewPager pager;
    private ViewPagerAdapter adapter;
    public boolean isdataload=false;
    RelativeLayout tabs_main_layout;
    LinearLayout top_layout,ll_wallet;
    public  static String pic_url;
    public  LinearLayout create_popup_layout;

    public Profile_Tab_F() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile_tab, container, false);
        context=getContext();

        instagram=view.findViewById(R.id.insta_icon);
        facebook=view.findViewById(R.id.facebook_icon);
        youttube=view.findViewById(R.id.youtube_icon);
        ll_wallet = view.findViewById(R.id.ll_wallet);



        ll_wallet.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), WalletActivity.class));
        });


                instagram.setOnClickListener(v -> {
                    String insta=Variables.sharedPreferences.getString(Variables.instagramlink, "https://www.instagram.com/");
                   // Log.e("instagramlink",Variables.sharedPreferences.getString(Variables.instagramlink, ""));
                    if (!insta.equalsIgnoreCase("")) {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse(insta));
                        startActivity(viewIntent);
                    }else {
                        Toast.makeText(context, "Don't have any added account!!!", Toast.LENGTH_LONG).show();
                    }
                });
                facebook.setOnClickListener(v -> {
                    String facebook=Variables.sharedPreferences.getString(Variables.facebooklink, "https://www.facebook.com/");
                    if(!facebook.equalsIgnoreCase("")) {
                        Intent viewIntent = new Intent("android.intent.action.VIEW",
                                Uri.parse(facebook));
                        startActivity(viewIntent);
                    }else {
                        Toast.makeText(context, "Don't have any added account!!!", Toast.LENGTH_LONG).show();
                    }
                });

                youttube.setOnClickListener(v -> {
                    String youtube=Variables.sharedPreferences.getString(Variables.youtubelink, "https://www.youtube.com/");
                    if (!youtube.equalsIgnoreCase("")) {
                        Intent viewIntent =
                                new Intent("android.intent.action.VIEW",
                                        Uri.parse(youtube));
                        startActivity(viewIntent);
                    }else {
                        Toast.makeText(context, "Don't have any added account!!!", Toast.LENGTH_LONG).show();
                    }
                });



        return init();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_image:
                OpenfullsizeImage(pic_url);
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

        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if((view!=null && isVisibleToUser) && !isdataload){
            if(Variables.sharedPreferences.getBoolean(Variables.islogin,false))
                init();
        }
        if((view!=null && isVisibleToUser) && isdataload){

            Call_Api_For_get_Allvideos();

        }





    }


    public View init(){

        username=view.findViewById(R.id.username);
        name=view.findViewById(R.id.name);
        imageView=view.findViewById(R.id.user_image);
        batchImage = view.findViewById(R.id.batch_url);
        imageView.setOnClickListener(this);

        video_count_txt=view.findViewById(R.id.video_count_txt);

        follow_count_txt=view.findViewById(R.id.follow_count_txt);
        fans_count_txt=view.findViewById(R.id.fan_count_txt);
        heart_count_txt=view.findViewById(R.id.heart_count_txt);

        setting_btn=view.findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(this);






        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        pager = view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);

        adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

        setupTabIcons();


        tabs_main_layout=view.findViewById(R.id.tabs_main_layout);
        top_layout=view.findViewById(R.id.top_layout);




        ViewTreeObserver observer = top_layout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                final int height=top_layout.getMeasuredHeight();

                top_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);

                ViewTreeObserver observer = tabs_main_layout.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) tabs_main_layout.getLayoutParams();
                        params.height= (int) (tabs_main_layout.getMeasuredHeight()+ height);
                        tabs_main_layout.setLayoutParams(params);
                        tabs_main_layout.getViewTreeObserver().removeGlobalOnLayoutListener(
                                this);

                    }
                });

            }
        });


        create_popup_layout=view.findViewById(R.id.create_popup_layout);
        view.findViewById(R.id.following_layout).setOnClickListener(this);
        view.findViewById(R.id.fans_layout).setOnClickListener(this);

        isdataload=true;
        update_profile();

        Call_Api_For_get_Allvideos();
        return view;
    }


    @SuppressLint("SetTextI18n")
    public void update_profile() {
        name.setText(Variables.sharedPreferences.getString(Variables.f_name, "")+ Variables.sharedPreferences.getString(Variables.l_name, ""));
        username.setText("@"+Variables.username);
        pic_url = Variables.sharedPreferences.getString(Variables.u_pic, "null");

        try {
            Picasso.get().load(pic_url)
                    .resize(200, 200)
                    .placeholder(R.drawable.profile_image_placeholder)
                    .centerCrop()
                    .into(imageView);
        } catch (Exception e) {
        }
    }


    private void setupTabIcons() {

        View view1 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView1= view1.findViewById(R.id.image);
        imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_view_carousel_black_24dp));
        tabLayout.getTabAt(0).setCustomView(view1);

        View view2 = LayoutInflater.from(context).inflate(R.layout.item_tabs_profile_menu, null);
        ImageView imageView2= view2.findViewById(R.id.image);
        imageView2.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        tabLayout.getTabAt(1).setCustomView(view2);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v=tab.getCustomView();
                assert v != null;
                ImageView image=v.findViewById(R.id.image);
                switch (tab.getPosition()){
                    case 0:
                        if(UserVideo_F.myvideo_count>0){
                            create_popup_layout.setVisibility(View.GONE);
                        }else {
                            create_popup_layout.setVisibility(View.VISIBLE);
                            Animation aniRotate = AnimationUtils.loadAnimation(context,R.anim.up_and_down_animation);
                            create_popup_layout.startAnimation(aniRotate);
                        }
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_view_carousel_black_24dp));
                        break;
                    case 1:
                        create_popup_layout.clearAnimation();
                        create_popup_layout.setVisibility(View.GONE);
                        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                        break;
                }
                tab.setCustomView(v);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v=tab.getCustomView();
                assert v != null;
                ImageView image=v.findViewById(R.id.image);

                switch (tab.getPosition()){
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

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final Resources resources;
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
        public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
            super(fm);
            this.resources = resources;
        }
        @Override
        public Fragment getItem(int position){
            final Fragment result;
            switch (position) {
                case 0:
                    result = new UserVideo_F(Variables.sharedPreferences.getString(Variables.u_id,""));
                    break;
                case 1:
                    result = new Liked_Video_F(Variables.sharedPreferences.getString(Variables.u_id,""));
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



        @NotNull
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



    //this will get the all videos data of user and then parse the data
    private void Call_Api_For_get_Allvideos() {

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("my_fb_id",Variables.sharedPreferences.getString(Variables.u_id,""));
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.showMyAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Parse_data(resp);
            }
        });



    }

    public void Parse_data(String responce) {
        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")) {
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                JSONObject data=msgArray.getJSONObject(0);
                JSONObject user_info=data.optJSONObject("user_info");

                assert user_info != null;
                Variables.referral_code=user_info.optString("referral_code");
                assert user_info != null;
                String a=user_info.optString("first_name") + " " + user_info.optString("last_name");
                String b="@"+user_info.optString("username");
                Variables.username=user_info.optString("username");
                username.setText(b);
                name.setText(a);
                ProfileFragment.pic_url=user_info.optString("profile_pic");
                if (!ProfileFragment.pic_url.equals("")) {
                    Picasso.get()
                            .load(ProfileFragment.pic_url)
                            .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                            .resize(200, 200).centerCrop().into(imageView);
                }

                ProfileFragment.batch_url=user_info.optString("batch_url");
                if (!ProfileFragment.batch_url.equals("")) {
                    batchImage.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(ProfileFragment.batch_url)
                           // .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                            .resize(30, 30).centerInside().into(batchImage);
                }

                follow_count_txt.setText(data.optString("total_following"));
                fans_count_txt.setText(data.optString("total_fans"));
                heart_count_txt.setText(data.optString("total_heart"));



                JSONArray user_videos=data.getJSONArray("user_videos");
                if(!user_videos.toString().equals("["+"0"+"]")){
                    video_count_txt.setText(user_videos.length()+"Videos");
                    create_popup_layout.setVisibility(View.GONE);
                }
                else {
                    create_popup_layout.setVisibility(View.VISIBLE);
                    Animation aniRotate = AnimationUtils.loadAnimation(context,R.anim.up_and_down_animation);
                    create_popup_layout.startAnimation(aniRotate);
                }

            }else {
             //   Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
         //   Toast.makeText(context, "Something wrong with Api", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }




    public void Open_Setting(){

        Open_menu_tab(setting_btn);


    }



    public void Open_Edit_profile(){
        Edit_Profile_F edit_profile_f = new Edit_Profile_F(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

                update_profile();
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, edit_profile_f).commit();
    }





    //this method will get the big size of profile image.
    public void OpenfullsizeImage(String url){
        if (!url.equalsIgnoreCase("")) {
            SeeFullImageFragment see_image_f = new SeeFullImageFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            Bundle args = new Bundle();
            args.putSerializable("image_url", url);
            see_image_f.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.MainMenuFragment, see_image_f).commit();
        }else {
            Toast.makeText(context, "Don't have profile pic!!!", Toast.LENGTH_SHORT).show();
        }
    }


    public void Open_menu_tab(View anchor_view){
        Context wrapper = new ContextThemeWrapper(context, R.style.AlertDialogCustom);
        PopupMenu popup = new PopupMenu(wrapper, anchor_view);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popup.setGravity(Gravity.TOP|Gravity.RIGHT);
        }
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.edit_Profile_id:
                        Open_Edit_profile();
                        break;

                    case R.id.referral_id:
                        Intent shareRide = new Intent();
                        shareRide.setAction(Intent.ACTION_SEND);
                        shareRide.putExtra(Intent.EXTRA_TEXT, "Use this referral code \"" +Variables.referral_code+
                                "\" while registering in Aivita. \n\n " +
                                "https://play.google.com/store/apps/details?id=com.matrixdeveloper.aivita");
                        shareRide.setType("text/plain");
                        Intent shareIntent = Intent.createChooser(shareRide, "Share Now");
                        startActivity(shareIntent);
                        break;
                    case R.id.logout_id:
                        Logout();
                        break;

                }
                return true;
            }
        });

    }



    public void Open_Following(){

        FollowingFragment following_f = new FollowingFragment(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {

                Call_Api_For_get_Allvideos();

            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", Variables.sharedPreferences.getString(Variables.u_id,""));
        args.putString("from_where","following");
        following_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, following_f).commit();

    }

    public void Open_Followers(){
        FollowingFragment following_f = new FollowingFragment(new Fragment_Callback() {
            @Override
            public void Responce(Bundle bundle) {
                Call_Api_For_get_Allvideos();
            }
        });
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", Variables.sharedPreferences.getString(Variables.u_id,""));
        args.putString("from_where","fan");
        following_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, following_f).commit();

    }

    // this will erase all the user info store in locally and logout the user
    public void Logout(){
        SharedPreferences.Editor editor= Variables.sharedPreferences.edit();
        editor.putString(Variables.u_id,"");
        editor.putString(Variables.u_name,"");
        editor.putString(Variables.u_pic,"");
        editor.putBoolean(Variables.islogin,false);
        editor.commit();
        getActivity().finish();
        startActivity(new Intent(getActivity(), MainMenuActivity.class));
    }



    @Override
    public void onDetach() {
        super.onDetach();
        Functions.deleteCache(context);
    }


}
