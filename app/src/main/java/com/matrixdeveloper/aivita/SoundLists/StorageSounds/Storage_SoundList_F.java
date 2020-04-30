package com.matrixdeveloper.aivita.SoundLists.StorageSounds;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.mbms.DownloadRequest;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.downloader.PRDownloader;
import com.gmail.samehadar.iosdialog.IOSDialog;
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
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.matrixdeveloper.aivita.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.matrixdeveloper.aivita.R;
import com.matrixdeveloper.aivita.SimpleClasses.ApiRequest;
import com.matrixdeveloper.aivita.SimpleClasses.Callback;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;
import com.matrixdeveloper.aivita.SoundLists.Sounds_GetSet;
import com.matrixdeveloper.aivita.Video_Recording.GalleryVideos.GalleryVideos_A;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;
import cafe.adriel.androidaudioconverter.model.AudioFormat;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

public class Storage_SoundList_F extends RootFragment implements Player.EventListener {

    RecyclerView listview;
    Storage_Sounds_Adapter adapter;
    ArrayList<Stored_Sound_catagory_Get_Set> datalist;

    DownloadRequest prDownloader;
    static boolean active = false;
    Boolean isScrolling = false;

    View view;
    Context context;

    IOSDialog iosDialog;
    int currentItems, totalItems, scrollOutItems;
    int a=0,b=0,c=0,totalLength;
    private Handler mHandler = new Handler();
    Uri uri;
    String[] proj;
    Cursor audioCursor;
    LinearLayoutManager linearLayoutManager;


    public static String running_sound_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_stored_sound_list, container, false);
        context = getContext();

        proj = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA};// Can include more data for more details and check it.

        audioCursor = Objects.requireNonNull(getActivity()).getApplicationContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);


        running_sound_id = "none";

        iosDialog = new IOSDialog.Builder(context)
                .setCancelable(false)
                .setSpinnerClockwise(false)
                .setMessageContentGravity(Gravity.END)
                .build();



        datalist = new ArrayList<>();
        linearLayoutManager =new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        listview = view.findViewById(R.id.stored_listview);
        listview.setLayoutManager(linearLayoutManager);
        listview.setNestedScrollingEnabled(true);
        listview.setHasFixedSize(true);
        listview.getLayoutManager().setMeasurementCacheEnabled(false);

        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                    // Toast.makeText(HomeActivity.this, "Scrolling", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                //   Toast.makeText(GalleryVideos_A.this, String.valueOf(totalItems), Toast.LENGTH_SHORT).show();

                if (isScrolling && (currentItems+scrollOutItems==totalItems)) {
                    isScrolling = false;
                    a=c;
                    c=a-30;
                    b=scrollOutItems+2;
                    if (a<=30){
                        c=-1;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (c==-1){
                                //getAllVideoPath(GalleryVideos_A.this, 1, cursor);
                                getPlayList(1,audioCursor);
                            }
                        }
                    },300);


                }
            }
        });

                    //getAllVideoPath(GalleryVideos_A.this, 1, cursor);
                    getPlayList(0,audioCursor);

        AndroidAudioConverter.load(getContext(), new ILoadCallback() {
            @Override
            public void onSuccess() {
                // Great!
            }
            @Override
            public void onFailure(Exception error) {
                // FFmpeg is not supported by device
                error.printStackTrace();
            }
        });

        return view;
    }

    public void set_storage_adapter() {

        adapter = new Storage_Sounds_Adapter(context, datalist, new Storage_Sounds_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, Stored_Sounds_GetSet item) {
                Log.d("resp", item.acc_path);

                if (view.getId() == R.id.done) {
                    StopPlaying();
                    //   Down_load_mp3(item.id, item.sound_name, item.acc_path);

                    convertAudio(item.acc_path,item.sound_name);


                } else if (view.getId() == R.id.fav_btn) {
                    Call_Api_For_Fav_sound(item.id);
                } else {
                    if (thread != null && !thread.isAlive()) {
                        StopPlaying();
                        playaudio(view, item);
                    } else if (thread == null) {
                        StopPlaying();
                        playaudio(view, item);
                    }
                }
            }

        });

        listview.setAdapter(adapter);


    }


    private void getPlayList(int i, Cursor audioCursor) {

        datalist = new ArrayList<>();
        ArrayList<Stored_Sounds_GetSet> sound_list = new ArrayList<>();

        if(this.audioCursor != null){

            int itemNum = 0;
            if (i==1){
                itemNum =i;}

            if(this.audioCursor.moveToFirst()){
                do{
                    int audioID = this.audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                    int audioName = this.audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                    int audioPath = this.audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

                 //   Log.e("Music_Details", audioCursor.getString(audioPath));

                    Stored_Sounds_GetSet item = new Stored_Sounds_GetSet();
                    item.id = this.audioCursor.getString(audioID);
                    item.acc_path = this.audioCursor.getString(audioPath);
                    item.sound_name = this.audioCursor.getString(audioName);
//                    item.description = "description";
//                    item.section = "20";
//                    item.thum = "";
//                    item.date_created = "";

                    sound_list.add(item);
                    itemNum++;
                  //  audioList.add(audioCursor.getString(audioPath));
                }while(this.audioCursor.moveToNext() && itemNum <=30);

                Stored_Sound_catagory_Get_Set sound_catagory_get_set = new Stored_Sound_catagory_Get_Set();
                sound_catagory_get_set.catagory = "storage";
                sound_catagory_get_set.sound_list = sound_list;

                datalist.add(sound_catagory_get_set);
            }

        }
        set_storage_adapter();
       // this.audioCursor.close();

      //  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1, audioList);
       // audioView.setAdapter(adapter);
    }


    @Override
    public boolean onBackPressed() {
        getActivity().onBackPressed();
        return super.onBackPressed();
    }


    View previous_view;
    Thread thread;
    SimpleExoPlayer player;
    String previous_url = "none";

    public void playaudio(View view, final Stored_Sounds_GetSet item) {
        previous_view = view;

        if (previous_url.equals(item.acc_path)) {

            previous_url = "none";
            running_sound_id = "none";
        } else {

            previous_url = item.acc_path;
            running_sound_id = item.id;

            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "TikTok"));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(item.acc_path));


            player.prepare(videoSource);
            player.addListener(this);

            player.setPlayWhenReady(true);

        }

    }


    public void StopPlaying() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.removeListener(this);
            player.release();
        }

        show_Stop_state();

    }


    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }


    @Override
    public void onStop() {
        super.onStop();
        active = false;

        running_sound_id = "null";

        if (player != null) {
            player.setPlayWhenReady(false);
            player.removeListener(this);
            player.release();
        }

        show_Stop_state();

    }


    public void Show_Run_State() {

        if (previous_view != null) {
            previous_view.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            previous_view.findViewById(R.id.pause_btn).setVisibility(View.VISIBLE);
            previous_view.findViewById(R.id.done).setVisibility(View.VISIBLE);
        }

    }


    public void Show_loading_state() {
        previous_view.findViewById(R.id.play_btn).setVisibility(View.GONE);
        previous_view.findViewById(R.id.loading_progress).setVisibility(View.VISIBLE);
    }


    public void show_Stop_state() {

        if (previous_view != null) {
            previous_view.findViewById(R.id.play_btn).setVisibility(View.VISIBLE);
            previous_view.findViewById(R.id.loading_progress).setVisibility(View.GONE);
            previous_view.findViewById(R.id.pause_btn).setVisibility(View.GONE);
            previous_view.findViewById(R.id.done).setVisibility(View.GONE);
        }

        running_sound_id = "none";

    }



    private void Call_Api_For_Fav_sound(String video_id) {

        iosDialog.show();

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id, "0"));
            parameters.put("sound_id", video_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(context, Variables.fav_sound, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                iosDialog.cancel();
            }
        });

    }


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
            Show_loading_state();
        } else if (playbackState == Player.STATE_READY) {
            Show_Run_State();
        } else if (playbackState == Player.STATE_ENDED) {
            show_Stop_state();
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
    public void onSeekProcessed() {

    }

    public void convertAudio(String dir, String sound_name){

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Converting audio file...");
        progressDialog.show();

        File wavFile = new File(dir);
        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File convertedFile) {

                progressDialog.dismiss();

                    Intent output = new Intent();
                    output.putExtra("isSelected", "yes");
                    output.putExtra("storage","yes");
                    output.putExtra("sound_name", convertedFile.getName());
                    output.putExtra("sound_path", convertedFile.getPath());
                  //  Toast.makeText(context, item.id, Toast.LENGTH_SHORT).show();
                    getActivity().setResult(RESULT_OK, output);
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);
            }
            @Override
            public void onFailure(Exception error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "ERROR: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
       // Toast.makeText(getContext(), "Converting audio file...", Toast.LENGTH_SHORT).show();
        AndroidAudioConverter.with(getContext())
                .setFile(wavFile)
                .setFormat(AudioFormat.AAC)
                .setCallback(callback)
                .convert();
    }

}
