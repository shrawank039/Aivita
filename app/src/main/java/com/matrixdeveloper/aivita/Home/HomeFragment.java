package com.matrixdeveloper.aivita.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.adcolony.sdk.AdColony;
import com.adcolony.sdk.AdColonyAdOptions;
import com.adcolony.sdk.AdColonyAppOptions;
import com.adcolony.sdk.AdColonyInterstitial;
import com.adcolony.sdk.AdColonyInterstitialListener;
import com.adcolony.sdk.AdColonyReward;
import com.adcolony.sdk.AdColonyRewardListener;
import com.adcolony.sdk.AdColonyUserMetadata;
import com.adcolony.sdk.AdColonyZone;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.RewardedVideoCallbacks;
import com.appodeal.ads.UserSettings;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daasuu.gpuv.egl.filter.GlWatermarkFilter;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.material.tabs.TabLayout;
import com.ixidev.gdpr.GDPRChecker;
import com.matrixdeveloper.aivita.Videos.Popular;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import com.matrixdeveloper.aivita.Comments.Comment_F;
import com.matrixdeveloper.aivita.Main_Menu.MainMenuActivity;
import com.matrixdeveloper.aivita.Main_Menu.MainMenuFragment;
import com.matrixdeveloper.aivita.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.matrixdeveloper.aivita.Profile.ProfileFragment;
import com.matrixdeveloper.aivita.R;
import com.matrixdeveloper.aivita.SimpleClasses.API_CallBack;
import com.matrixdeveloper.aivita.SimpleClasses.ApiRequest;
import com.matrixdeveloper.aivita.SimpleClasses.Callback;
import com.matrixdeveloper.aivita.SimpleClasses.Fragment_Callback;
import com.matrixdeveloper.aivita.SimpleClasses.Fragment_Data_Send;
import com.matrixdeveloper.aivita.SimpleClasses.Functions;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;
import com.matrixdeveloper.aivita.Taged.Taged_Videos_F;
import com.matrixdeveloper.aivita.VideoAction.VideoAction_F;
import com.matrixdeveloper.aivita.Video_Recording.Video_Recoder_A;
import com.matrixdeveloper.aivita.Videos.Followings;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.matrixdeveloper.aivita.SimpleClasses.Variables.sharedPreferences;

// this is the main view which is show all  the video in list
public class HomeFragment extends RootFragment implements Player.EventListener, Fragment_Data_Send {

    private View view;
    private Context context;
    private TextView tv_following, popular;
    private RecyclerView recyclerView;
    private ArrayList<HomeModel> data_list;
    private int currentPage = -1;
    private LinearLayoutManager layoutManager;
    private ProgressBar p_bar;
    private SwipeRefreshLayout swiperefresh;
    private int swipe_count = 0;
    private int currentItems, totalItems, scrollOutItems;
    private int scrollOut=2, end=11, endCount=12;
   // private InterstitialAd mInterstitialAd;
  //  private RewardedAd rewardedAd;
    public static final String APP_KEY = "6b271ea32476c0dcb5d995a612f1a61e8686437df8aafc77";
    boolean consent=true;
    private HomeAdapter adapter;
    final private String APP_ID = "app185a7e71e1714831a49ec7";
    final private String ZONE_ID = "vz9f27a1bf7c094dcd94";
    final private String TAG = "AdColony";
    private String endItemId="";
    private boolean adcolonyFilled=false;
    private AdColonyInterstitial ad;
    private AdColonyInterstitialListener listener;
    private AdColonyAdOptions adOptions;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        p_bar = view.findViewById(R.id.p_bar);
        tv_following = view.findViewById(R.id.tv_following);
        popular = view.findViewById(R.id.popular);
        recyclerView = view.findViewById(R.id.recylerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        data_list = new ArrayList<>();


        AdColonyAppOptions appOptions = new AdColonyAppOptions()
                .setUserID("unique_user_id")
                .setKeepScreenOn(true);

        // Configure AdColony in your launching Activity's onCreate() method so that cached ads can
        // be available as soon as possible.
        AdColony.configure(getActivity(), appOptions, APP_ID, ZONE_ID);

        // Optional user metadata sent with the ad options in each request
        AdColonyUserMetadata metadata = new AdColonyUserMetadata()
                .setUserAge(23)
                .setUserEducation(AdColonyUserMetadata.USER_EDUCATION_BACHELORS_DEGREE)
                .setUserGender(AdColonyUserMetadata.USER_MALE);

        // Ad specific options to be sent with request
        adOptions = new AdColonyAdOptions()
                .enableConfirmationDialog(false)
                .enableResultsDialog(false)
                .setUserMetadata(metadata);

        // Create and set a reward listener
        AdColony.setRewardListener(new AdColonyRewardListener() {
            @Override
            public void onReward(AdColonyReward reward) {
                // Query reward object for info here
                Log.d( TAG, "onReward" );
            }
        });

        // Set up listener for interstitial ad callbacks. You only need to implement the callbacks
        // that you care about. The only required callback is onRequestFilled, as this is the only
        // way to get an ad object.
        listener = new AdColonyInterstitialListener() {
            @Override
            public void onRequestFilled(AdColonyInterstitial ad) {
                // Ad passed back in request filled callback, ad can now be shown
             //   Toast.makeText(context, "adFilled", Toast.LENGTH_SHORT).show();
                HomeFragment.this.ad = ad;
                adcolonyFilled =true;
//                showButton.setEnabled(true);
//                progress.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onRequestFilled");
            }

            @Override
            public void onRequestNotFilled(AdColonyZone zone) {

              //  Toast.makeText(context, "adNOTFilled", Toast.LENGTH_SHORT).show();
                // Ad request was not filled
                //  progress.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onRequestNotFilled");
            }

            @Override
            public void onOpened(AdColonyInterstitial ad) {
                // Ad opened, reset UI to reflect state change
//                showButton.setEnabled(false);
                adcolonyFilled =false;
//                progress.setVisibility(View.VISIBLE);
                Log.d(TAG, "onOpened");
            }

            @Override
            public void onExpiring(AdColonyInterstitial ad) {
                // Request a new ad if ad is expiring
//                showButton.setEnabled(false);
                adcolonyFilled=false;
//                progress.setVisibility(View.VISIBLE);
                AdColony.requestInterstitial(ZONE_ID, this, adOptions);
                Log.d(TAG, "onExpiring");
            }
        };

        adapter = new HomeAdapter(context, data_list, new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, final HomeModel item, View view) {

                switch (view.getId()) {

                    case R.id.Home_follow_btn :
                        if (Variables.sharedPreferences.getBoolean(Variables.islogin, false)){
                            Follow_unFollow_User(item);
                            // Toast.makeText(context, "You followed user", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(context, "Please login in to app", Toast.LENGTH_SHORT).show();

                        break;

                    case  R.id.sound_image_layout :
                        AddedToFavourite(item);

                        break;
                    case R.id.user_pic:
                        onPause();
                        OpenProfile(item, false);
                        break;

                    case R.id.username:
                        onPause();
                        OpenProfile(item, false);
                        break;

                    case R.id.like_layout:
                        if (sharedPreferences.getBoolean(Variables.islogin, false))
                            Like_Video(postion, item);
                        else
                            Toast.makeText(context, "Please Login First!", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.comment_layout:
                        OpenComment(item);
                        break;

                    case R.id.shared_layout:

                        final VideoAction_F fragment = new VideoAction_F(item.video_id, new Fragment_Callback() {
                            @Override
                            public void Responce(Bundle bundle) {

                                if(bundle.getString("action").equals("save")){
                                    Save_Video(item);
                                }
                            }
                        });
                        fragment.show(getChildFragmentManager(), "");


                        break;
                }

            }
        });

        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

//        new GDPRChecker()
//                .withContext(getApplicationContext())
//                .withPrivacyUrl(getString(R.string.privacy_url)) // your privacy url
//                .withPublisherIds("pub-6272309782897719") // your admob account Publisher id
//                //  .withTestMode("9424DF76F06983D1392E609FC074596C") // remove this on real project
//                .check();

//        MobileAds.initialize(getContext(),
//                "ca-app-pub-3940256099942544~3347511713");
//        mInterstitialAd = new InterstitialAd(Objects.requireNonNull(getContext()));
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        Appodeal.setTesting(false);
        Appodeal.setLogLevel(com.appodeal.ads.utils.Log.LogLevel.none);
        Appodeal.setAutoCache(Appodeal.REWARDED_VIDEO, true);

        Appodeal.setUserAge(23);
        Appodeal.setUserGender(UserSettings.Gender.MALE);
        Appodeal.initialize(Objects.requireNonNull(getActivity()), APP_KEY, Appodeal.REWARDED_VIDEO, consent);

//        rewardedAd = new RewardedAd(Objects.requireNonNull(getContext()),
//                "ca-app-pub-6272309782897719/1077799568");
//        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
//            @Override
//            public void onRewardedAdLoaded() {
//                // Ad successfully loaded.
//            }
//            @Override
//            public void onRewardedAdFailedToLoad(int errorCode) {
//               // Toast.makeText(context, String.valueOf(errorCode), Toast.LENGTH_SHORT).show();
//                // Ad failed to load.
//            }
//        };
     //   rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        tv_following.setOnClickListener(v -> Open_Following());
      //  popular.setOnClickListener(v -> Open_Popular());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if (end-2 == scrollOutItems){

                  //  start = 1 + end;
                    end = end + endCount;

                    if (adcolonyFilled)
                        ad.show();
                    else
                        showAd();

                  //  Toast.makeText(context, endItemId + " : "+end, Toast.LENGTH_SHORT).show();
                    Call_Api_For_get_Allvideos(endItemId);
                }

//                if (scrollOutItems>scrollOut){
//
//                    scrollOut+=2;
//                   // rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
//                     //       showAd();
//                }
                int page_no = scrollOffset / height;
                if (page_no != currentPage) {
                    currentPage = page_no;
                    Release_Privious_Player();
                    Set_Player(currentPage);
                }
            }
        });


        swiperefresh = view.findViewById(R.id.swiperefresh);
        swiperefresh.setProgressViewOffset(false, 0, 200);

        swiperefresh.setColorSchemeResources(R.color.black);
        swiperefresh.setOnRefreshListener(() -> {
            currentPage = -1;
            Call_Api_For_get_Allvideos(endItemId);
        });

        Call_Api_For_get_Allvideos(endItemId);

        return view;
    }

    private void showAd() {

//        if (Appodeal.isLoaded(Appodeal.REWARDED_VIDEO)) {
//            Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
//        }

         Appodeal.setRewardedVideoCallbacks(new RewardedVideoCallbacks() {
             @Override
             public void onRewardedVideoLoaded(boolean b) {

             }

             @Override
             public void onRewardedVideoFailedToLoad() {

             }

             @Override
             public void onRewardedVideoShown() {

             }

             @Override
             public void onRewardedVideoShowFailed() {

             }

             @Override
             public void onRewardedVideoFinished(double v, String s) {

             }

             @Override
             public void onRewardedVideoClosed(boolean b) {

             }

             @Override
             public void onRewardedVideoExpired() {

             }

             @Override
             public void onRewardedVideoClicked() {

             }
         });

        Appodeal.show(Objects.requireNonNull(getActivity()), Appodeal.REWARDED_VIDEO);

//        if (rewardedAd.isLoaded()) {
//            Context activityContext = getContext();
//            RewardedAdCallback adCallback = new RewardedAdCallback() {
//                @Override
//                public void onRewardedAdOpened() {
//                    // Ad opened.
//                }
//                @Override
//                public void onRewardedAdClosed() {
//                    // Ad closed.
//                }
//                @Override
//                public void onUserEarnedReward(@NonNull RewardItem reward) {
//                    // User earned reward.
//                }
//
//                @Override
//                public void onRewardedAdFailedToShow(int errorCode) {
//                    // Ad failed to display.
//                }
//            };
//
//            rewardedAd.show((Activity) activityContext, adCallback);
//
//        }

    }

    private void Open_Following() {

        Release_Privious_Player();

        Followings following_f = new Followings();

        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", sharedPreferences.getString(Variables.u_id, ""));
        args.putString("from_where", "following");
        following_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, following_f);
        transaction.commit();

    }

    private void Open_Popular() {

        Release_Privious_Player();

        Popular popular_f = new Popular(bundle -> Call_Api_For_get_Allvideos(endItemId));

        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("id", sharedPreferences.getString(Variables.u_id, ""));
        args.putString("from_where", "following");
        popular_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, popular_f);
        transaction.commit();

    }

    private void Set_Adapter() {

        adapter.notifyDataSetChanged();

    }

    private void AddedToFavourite(final HomeModel item) {
        Timber.e(sharedPreferences.getString(Variables.u_id, "0"));
        Timber.e(item.sound_id);
        String fav_soundUrl="https://aivita.club/aivita/API/index.php?p=fav_sound";
        StringRequest stringRequest =new StringRequest(Request.Method.POST,fav_soundUrl, response -> {
            Timber.e(response);
            Timber.e(item.sound_id);
            Timber.e(sharedPreferences.getString(Variables.u_id, ""));
                //  Timber.e(response);

                   startActivity(new Intent(getActivity(), Video_Recoder_A.class));
                    Toast.makeText(context, "Added to Favourite", Toast.LENGTH_SHORT).show();
                   //  Toast.makeText(context, "Sorry this song is not belongs to aivita", Toast.LENGTH_SHORT).show();

         }, error -> {

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("fb_id", sharedPreferences.getString(Variables.u_id,""));
                params.put("sound_id",item.sound_id);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // Bottom two function will call the api and get all the videos form api and parse the json data
    private void Call_Api_For_get_Allvideos(String endItemId) {
        Log.d(Variables.tag, MainMenuActivity.token);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("end_id", endItemId);
            parameters.put("end", String.valueOf(endCount));
            parameters.put("type", "0");
            //parameters.put("token",MainMenuActivity.token);
            parameters.put("token", sharedPreferences.getString(Variables.device_token, "Null"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiRequest.Call_Api(context, Variables.showAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                swiperefresh.setRefreshing(false);
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
                for (int i = 0; i < msgArray.length(); i++) {
                    JSONObject itemdata = msgArray.optJSONObject(i);
                    HomeModel item = new HomeModel();
                    item.fb_id = itemdata.optString("fb_id");
                    endItemId = itemdata.optString("id");

                    JSONObject user_info = itemdata.optJSONObject("user_info");


                    item.first_name = user_info.optString("first_name");
                    item.last_name = user_info.optString("last_name");
                    item.profile_pic = user_info.optString("profile_pic");

                    JSONObject sound_data = itemdata.optJSONObject("sound");
                    item.sound_id = sound_data.optString("id");
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString(Variables.sid,sound_data.optString("id"));
                    item.sound_name = sound_data.optString("sound_name");
                    item.sound_pic = sound_data.optString("thum");


                    JSONObject count = itemdata.optJSONObject("count");
                    item.like_count = count.optString("like_count");
                    item.video_comment_count = count.optString("video_comment_count");


                    item.video_id = itemdata.optString("id");
                    item.liked = itemdata.optString("liked");
                    item.video_url = Variables.base_url + itemdata.optString("video");
                    item.video_description = itemdata.optString("description");

                    item.thum = Variables.base_url + itemdata.optString("thum");
                    item.follow =itemdata.optString("follow");
                    item.created_date = itemdata.optString("created");

                    data_list.add(item);
                }

              //  Toast.makeText(context, endItemId, Toast.LENGTH_SHORT).show();

                Set_Adapter();

            } else {
//                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
//            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    private void Call_Api_For_Singlevideos(final int postion) {


        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", sharedPreferences.getString(Variables.u_id, "0"));
            //parameters.put("token",Variables.sharedPreferences.getString(Variables.device_token,"Null"));
            parameters.put("video_id", data_list.get(postion).video_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ApiRequest.Call_Api(context, Variables.showAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                swiperefresh.setRefreshing(false);
                Singal_Video_Parse_data(postion, resp);
            }
        });


    }

    public void Singal_Video_Parse_data(int pos, String responce) {

        try {
            JSONObject jsonObject = new JSONObject(responce);
            String code = jsonObject.optString("code");
            if (code.equals("200")) {
                JSONArray msgArray = jsonObject.getJSONArray("msg");
                for (int i = 0; i < msgArray.length(); i++) {
                    JSONObject itemdata = msgArray.optJSONObject(i);
                    HomeModel item = new HomeModel();
                    item.fb_id = itemdata.optString("fb_id");

                    JSONObject user_info = itemdata.optJSONObject("user_info");

                    item.first_name = user_info.optString("first_name");
                    item.last_name = user_info.optString("last_name");
                    item.profile_pic = user_info.optString("profile_pic");

                    JSONObject sound_data = itemdata.optJSONObject("sound");
                    item.sound_id = sound_data.optString("id");
                    item.sound_name = sound_data.optString("sound_name");
                    item.sound_pic = sound_data.optString("thum");


                    JSONObject count = itemdata.optJSONObject("count");
                    item.like_count = count.optString("like_count");
                    item.video_comment_count = count.optString("video_comment_count");


                    item.video_id = itemdata.optString("id");
                    item.liked = itemdata.optString("liked");
                    item.video_url = Variables.base_url + itemdata.optString("video");
                    item.video_description = itemdata.optString("description");

                    item.thum = Variables.base_url + itemdata.optString("thum");
                    item.follow =itemdata.optString("follow");
                    item.created_date = itemdata.optString("created");

                    data_list.remove(pos);
                    data_list.add(pos, item);
                    adapter.notifyDataSetChanged();
                }


            } else {
//                Toast.makeText(context, "" + jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

    }

    // this will call when swipe for another video and
    // this function will set the player to the current video
    @SuppressLint("ClickableViewAccessibility")
    private void Set_Player(final int currentPage) {

        final HomeModel item = data_list.get(currentPage);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        DefaultLoadControl loadControl = new DefaultLoadControl.Builder().setBufferDurationsMs(32 * 1024, 64 * 1024, 1024, 1024).createDefaultLoadControl();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "Aivita"));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(item.video_url));

        Timber.tag("resp").d(item.video_url);

        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);


        View layout = layoutManager.findViewByPosition(currentPage);
        final PlayerView playerView = layout.findViewById(R.id.playerview);
        playerView.setPlayer(player);


        player.setPlayWhenReady(is_visible_to_user);
        privious_player = player;


        final RelativeLayout mainlayout = layout.findViewById(R.id.mainlayout);
        playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);
                    float deltaX = e1.getX() - e2.getX();
                    float deltaXAbs = Math.abs(deltaX);
                    // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                    if ((deltaXAbs > 100) && (deltaXAbs < 1000)) {
                        if (deltaX > 0) {
                            OpenProfile(item, true);
                        }
                    }

                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);
                    if (!player.getPlayWhenReady()) {
                        privious_player.setPlayWhenReady(true);
                    } else {
                        privious_player.setPlayWhenReady(false);
                    }

                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    Show_video_option(item);

                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {

                    if (!player.getPlayWhenReady()) privious_player.setPlayWhenReady(true);

                    if (sharedPreferences.getBoolean(Variables.islogin, false)) {
                        Show_heart_on_DoubleTap(item, mainlayout, e);
                        Like_Video(currentPage, item);
                    } else Toast.makeText(context, "Please Login into app", Toast.LENGTH_SHORT).show();
                    return super.onDoubleTap(e);

                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        TextView desc_txt = layout.findViewById(R.id.desc_txt);
        HashTagHelper.Creator.create(context.getResources().getColor(R.color.maincolor), hashTag -> {

            onPause();
            OpenHashtag(hashTag);

        }).handle(desc_txt);


        LinearLayout soundimage = (LinearLayout) layout.findViewById(R.id.sound_image_layout);
        Animation sound_animation = AnimationUtils.loadAnimation(context, R.anim.d_clockwise_rotation);
        soundimage.startAnimation(sound_animation);

        if (sharedPreferences.getBoolean(Variables.islogin, false))
            Functions.Call_Api_For_update_view(getActivity(), item.video_id);

        swipe_count++;
        if (swipe_count > 4) {
            swipe_count = 0;
        }

        Call_Api_For_Singlevideos(currentPage);

    }


    public void Show_heart_on_DoubleTap(HomeModel item, final RelativeLayout mainlayout, MotionEvent e) {

        int x = (int) e.getX() - 100;
        int y = (int) e.getY() - 100;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        final ImageView iv = new ImageView(getApplicationContext());
        lp.setMargins(x, y, 0, 0);
        iv.setLayoutParams(lp);
        if (item.liked.equals("1"))
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_heart));
        else
            iv.setImageDrawable(getResources().getDrawable(
                    R.drawable.ic_favorite_black_24dp));

        mainlayout.addView(iv);
        Animation fadeoutani = AnimationUtils.loadAnimation(context, R.anim.fade_out);

        fadeoutani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainlayout.removeView(iv);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv.startAnimation(fadeoutani);
    }

/*
    public void Show_add(){
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
    }*/


    @Override
    public void onDataSent(String yourData) {
        int comment_count = Integer.parseInt(yourData);
        HomeModel item = data_list.get(currentPage);
        item.video_comment_count = "" + comment_count;
        data_list.remove(currentPage);
        data_list.add(currentPage, item);
        adapter.notifyDataSetChanged();
    }

    // this will call when go to the home tab From other tab.
    // this is very importent when for video play and pause when the focus is changes
    boolean is_visible_to_user;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        is_visible_to_user = isVisibleToUser;

        if (privious_player != null && isVisibleToUser) privious_player.setPlayWhenReady(true);
        else if (privious_player != null && !isVisibleToUser) privious_player.setPlayWhenReady(false);
    }

    // when we swipe for another video this will relaese the privious player
    private SimpleExoPlayer privious_player;

    public void Release_Privious_Player() {
        if (privious_player != null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }

    // this function will call for like the video and Call an Api for like the video
    public void Like_Video(final int position, final HomeModel home_get_set) {
        String action = home_get_set.liked;

        if (action.equals("1")) {
            action = "0";
            home_get_set.like_count = "" + (Integer.parseInt(home_get_set.like_count) - 1);
        } else {
            action = "1";
            home_get_set.like_count = "" + (Integer.parseInt(home_get_set.like_count) + 1);
        }

        data_list.remove(position);
        home_get_set.liked = action;
        data_list.add(position, home_get_set);
        adapter.notifyDataSetChanged();

        Functions.Call_Api_For_like_video(getActivity(), home_get_set.video_id, action, new API_CallBack() {

            @Override
            public void ArrayData(ArrayList arrayList) {

            }

            @Override
            public void OnSuccess(String responce) {

            }

            @Override
            public void OnFail(String responce) {

            }
        });

    }


    // this will open the comment screen
    private void OpenComment(HomeModel item) {

        int comment_counnt = Integer.parseInt(item.video_comment_count);

        Fragment_Data_Send fragment_data_send = this;

        Comment_F comment_f = new Comment_F(comment_counnt, fragment_data_send);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("video_id", item.video_id);
        args.putString("user_id", item.fb_id);
        comment_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, comment_f).commit();



    }


    // this will open the profile of user which have uploaded the currenlty running video
    private void OpenProfile(HomeModel item, boolean from_right_to_left) {
        if (sharedPreferences.getString(Variables.u_id, "0").equals(item.fb_id)) {

            TabLayout.Tab profile = MainMenuFragment.tabLayout.getTabAt(4);
            profile.select();

        } else {
            ProfileFragment profile_f = new ProfileFragment(new Fragment_Callback() {
                @Override
                public void Responce(Bundle bundle) {
                    Call_Api_For_Singlevideos(currentPage);
                }
            });
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (from_right_to_left)
                transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);
            else
                transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);

            Bundle args = new Bundle();
            args.putString("user_id", item.fb_id);
            args.putString("user_name", item.first_name + " " + item.last_name);
            args.putString("user_pic", item.profile_pic);
            profile_f.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.MainMenuFragment, profile_f).commit();
        }

    }


    // this will open the profile of user which have uploaded the currenlty running video
    private void OpenHashtag(String tag) {

        Taged_Videos_F taged_videos_f = new Taged_Videos_F();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
        Bundle args = new Bundle();
        args.putString("tag", tag);
        taged_videos_f.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.MainMenuFragment, taged_videos_f).commit();


    }


    private void Show_video_option(final HomeModel home_get_set) {

        final CharSequence[] options = {"Save Video", "Cancel"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.AlertDialogCustom);

        builder.setTitle(null);

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Save Video")) {
                if (Functions.Checkstoragepermision(getActivity()))
                    Save_Video(home_get_set);

            } else if (options[item].equals("Cancel")) {

                dialog.dismiss();

            }

        });

        builder.show();

    }

    public void Save_Video(final HomeModel item) {
        Functions.Show_determinent_loader(context, false, false);
        PRDownloader.initialize(getActivity().getApplicationContext());
        DownloadRequest prDownloader = PRDownloader.download(item.video_url, Environment.getExternalStorageDirectory() + "/Aivita/", item.video_id + "no_watermark" + ".mp4")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                        int prog = (int) ((progress.currentBytes * 100) / progress.totalBytes);
                        Functions.Show_loading_progress(prog / 2);

                    }
                });


        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                Applywatermark(item);
            }

            @Override
            public void onError(Error error) {
                Delete_file_no_watermark(item);
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                Functions.cancel_determinent_loader();
            }
        });


    }

    public void Applywatermark(final HomeModel item) {


        Bitmap myLogo = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_watermark_image)).getBitmap();
        Bitmap bitmap_resize = Bitmap.createScaledBitmap(myLogo, 200, 200, false);
        GlWatermarkFilter filter = new GlWatermarkFilter(bitmap_resize, GlWatermarkFilter.Position.LEFT_TOP);
        new GPUMp4Composer(Environment.getExternalStorageDirectory() + "/aivita/" + item.video_id + "no_watermark" + ".mp4",
                Environment.getExternalStorageDirectory() + "/aivita/" + item.video_id + ".mp4")
                .filter(filter)

                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {

                        Timber.d("%s", (int) (progress * 100));
                        Functions.Show_loading_progress((int) ((progress * 100) / 2) + 50);

                    }

                    @Override
                    public void onCompleted() {
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Functions.cancel_determinent_loader();
                                Delete_file_no_watermark(item);
                                Scan_file(item);

                            }
                        });

                    }

                    @Override
                    public void onCanceled() {
                        Timber.d("onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {

                        Timber.d(exception.toString());

                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Delete_file_no_watermark(item);
                                    Functions.cancel_determinent_loader();
                                    Functions.cancel_determinent_loader();
                                    Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                }
                            }
                        });

                    }
                })
                .start();
    }


    public void Delete_file_no_watermark(HomeModel item) {
        File file = new File(Environment.getExternalStorageDirectory() + "/Aivita/" + item.video_id + "no_watermark" + ".mp4");
        if (file.exists()) {
            file.delete();
        }
    }

    public void Scan_file(HomeModel item) {
        MediaScannerConnection.scanFile(getActivity(),
                new String[]{Environment.getExternalStorageDirectory() + "/Aivita/" + item.video_id + ".mp4"},
                null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Timber.tag("ExternalStorage").i("Scanned " + path + ":");
                        Timber.tag("ExternalStorage").i("-> uri=" + uri);
                    }
                });
    }

    public boolean is_fragment_exits() {
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    // this is lifecyle of the Activity which is importent for play,pause video or relaese the player
    @Override
    public void onResume() {
        super.onResume();

        if (ad == null || ad.isExpired()) {
            AdColony.requestInterstitial(ZONE_ID, listener, adOptions);
        }

        if ((privious_player != null && is_visible_to_user) && !is_fragment_exits()) {
            privious_player.setPlayWhenReady(true);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (privious_player != null) {
            privious_player.setPlayWhenReady(false);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (privious_player != null) {
            privious_player.setPlayWhenReady(false);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (privious_player != null) {
            privious_player.release();
        }
    }

    // Bottom all the function and the Call back listener of the Expo player
    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }


    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }


    @Override
    public void onLoadingChanged(boolean isLoading) {

    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_BUFFERING) {
            p_bar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            p_bar.setVisibility(View.GONE);
        }


    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed(){}



    private void Follow_unFollow_User(final HomeModel item) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Variables.edit_profile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);

            }
        }, error -> {

        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("fb_id", sharedPreferences.getString(Variables.u_id,""));
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
        String a="0";
        if(item.follow.equalsIgnoreCase("true"))
            a="0";
        else
            a="1";
      //  Toast.makeText(context, a, Toast.LENGTH_SHORT).show();
        Functions.Call_Api_For_Follow_or_unFollow(getActivity(),
                sharedPreferences.getString(Variables.u_id, "0"), item.fb_id, a, new API_CallBack() {
                    @Override
                    public void ArrayData(ArrayList arrayList) {}
                    @Override
                    public void OnSuccess(String responce) { }
                    @Override
                    public void OnFail(String responce) {}
                });
    }

}
