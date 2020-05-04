package com.matrixdeveloper.aivita.Video_Recording.GalleryVideos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Toast;

import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.daasuu.gpuv.composer.GPUMp4Composer;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.matrixdeveloper.aivita.R;
import com.matrixdeveloper.aivita.Video_Recording.GallerySelectedVideo.GallerySelectedVideo_A;
import com.matrixdeveloper.aivita.SimpleClasses.Functions;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;

import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.util.Matrix;
import com.googlecode.mp4parser.util.Path;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GalleryVideos_A extends AppCompatActivity {

    ArrayList<GalleryVideo_Get_Set> data_list;
    public  RecyclerView recyclerView;
    GalleryVideos_Adapter adapter;
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    GridLayoutManager layoutManager;
    int a=0,b=0,c=0,totalLength;
    private Handler mHandler = new Handler();
    Uri uri;
    String[] projection;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_videos);

        data_list = new ArrayList();

        recyclerView=findViewById(R.id.recylerview);
        layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        projection = new String[]{MediaStore.Video.VideoColumns.DATA};
      //  Toast.makeText(this, String.valueOf(projection.length), Toast.LENGTH_SHORT).show();
        cursor = this.getContentResolver().query(uri, projection, null, null, null);

        data_list=new ArrayList<>();

        adapter=new GalleryVideos_Adapter(this, data_list, new GalleryVideos_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, GalleryVideo_Get_Set item, View view) {
                MediaMetadataRetriever retriever = new  MediaMetadataRetriever();
                Bitmap bmp = null;
                try
                {
                    retriever.setDataSource(item.video_path);
                    bmp = retriever.getFrameAtTime();
                    int videoHeight=bmp.getHeight();
                    int videoWidth=bmp.getWidth();

                    Log.d("resp",""+videoWidth+"---"+videoHeight);

                }catch (Exception e){

                }

                if(item.video_duration_ms<19500){
                    Chnage_Video_size(item.video_path, Variables.gallery_resize_video);

                }else {

                    try {
                        startTrim(new File(item.video_path), new File(Variables.gallery_trimed_video), 1000, 18000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

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
                                     getAllVideoPath(GalleryVideos_A.this, 1, cursor);
                                }
                        }
                    },300);


                }
            }
        });

        getAllVideoPath(this, 0, cursor);


        findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_top,R.anim.out_from_bottom);

            }
        });

    }

    @Override
    public void onBackPressed() {
        cursor.close();
        super.onBackPressed();
    }

    public  void  getAllVideoPath(Context context, int i, Cursor cursor) {

        if (cursor != null) {

            int itemNum = 0;
            if (i==1){
                itemNum =i;}

              while (cursor.moveToNext() && itemNum <=30) {
                  // Toast.makeText(this, "getting", Toast.LENGTH_SHORT).show();
                GalleryVideo_Get_Set item=new GalleryVideo_Get_Set();
               //   projection = String[]{MediaStore.Video.VideoColumns.DATA};
                item.video_path=cursor.getString(0);
                item.video_duration_ms=getfileduration(Uri.parse(cursor.getString(0)));

                Log.d("resp",""+ item);

                 if(item.video_duration_ms>5000){
                     item.video_time=change_sec_to_time(item.video_duration_ms);
                     data_list.add(item);
                 }
                 itemNum++;
            }
             adapter.notifyDataSetChanged();
        }
    }



    // get the audio file duration that is store in our directory
    public long getfileduration(Uri uri) {
        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this, uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Integer.parseInt(durationStr);

            return file_duration;
            }
        catch (Exception e){

        }
        return 0;
    }


    public String change_sec_to_time(long file_duration){
        long second = (file_duration / 1000) % 60;
        long minute = (file_duration / (1000 * 60)) % 60;

        return String.format("%02d:%02d", minute, second);

    }


    public void Chnage_Video_size(String src_path,String destination_path){

        Functions.Show_determinent_loader(this,false,false);
        new GPUMp4Composer(src_path, destination_path)
                .size(540, 960)
                .videoBitrate((int) (0.25 * 16 * 540 * 960))
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {

                        Log.d("resp",""+(int) (progress*100));
                        Functions.Show_loading_progress((int)(progress*100));

                    }

                    @Override
                    public void onCompleted() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Functions.cancel_determinent_loader();

                                Intent intent=new Intent(GalleryVideos_A.this, GallerySelectedVideo_A.class);
                                intent.putExtra("video_path",Variables.gallery_resize_video);
                                startActivity(intent);

                            }
                        });


                    }

                    @Override
                    public void onCanceled() {
                        Log.d("resp", "onCanceled");
                    }

                    @Override
                    public void onFailed(Exception exception) {

                        Log.d("resp",exception.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    Functions.cancel_determinent_loader();

                                    Toast.makeText(GalleryVideos_A.this, "Try Again", Toast.LENGTH_SHORT).show();
                                }catch (Exception e){

                                }
                            }
                        });

                    }
                })
                .start();

    }

    public  void startTrim(final File src, final File dst, final int startMs, final int endMs) throws IOException {

        new AsyncTask<String,Void,String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {

                    FileDataSourceImpl file = new FileDataSourceImpl(src);
                    Movie movie = MovieCreator.build(file);
                    List<Track> tracks = movie.getTracks();
                    movie.setTracks(new LinkedList<Track>());
                    double startTime = startMs / 1000;
                    double endTime = endMs / 1000;
                    boolean timeCorrected = false;

                    for (Track track : tracks) {
                        if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                            if (timeCorrected) {
                                throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                            }
                            startTime = Functions.correctTimeToSyncSample(track, startTime, false);
                            endTime = Functions.correctTimeToSyncSample(track, endTime, true);
                            timeCorrected = true;
                        }
                    }
                    for (Track track : tracks) {
                        long currentSample = 0;
                        double currentTime = 0;
                        long startSample = -1;
                        long endSample = -1;

                        for (int i = 0; i < track.getSampleDurations().length; i++) {
                            if (currentTime <= startTime) {
                                startSample = currentSample;
                            }
                            if (currentTime <= endTime) {
                                endSample = currentSample;
                            } else {
                                break;
                            }
                            currentTime += (double) track.getSampleDurations()[i] / (double) track.getTrackMetaData().getTimescale();
                            currentSample++;
                        }
                        movie.addTrack(new CroppedTrack(track, startSample, endSample));
                    }

                    Container out = new DefaultMp4Builder().build(movie);
                    MovieHeaderBox mvhd = Path.getPath(out, "moov/mvhd");
                    mvhd.setMatrix(Matrix.ROTATE_180);
                    if (!dst.exists()) {
                        dst.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(dst);
                    WritableByteChannel fc = fos.getChannel();
                    try {
                        out.writeContainer(fc);
                    } finally {
                        fc.close();
                        fos.close();
                        file.close();
                    }

                    file.close();
                    return "Ok";
                }catch (IOException e){
                    return "error";
                }

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Functions.Show_indeterminent_loader(GalleryVideos_A.this,true,true);
            }

            @Override
            protected void onPostExecute(String result) {
                if(result.equals("error")){
                    Toast.makeText(GalleryVideos_A.this, "Try Again", Toast.LENGTH_SHORT).show();
                }else {
                    Functions.cancel_indeterminent_loader();
                    Chnage_Video_size(Variables.gallery_trimed_video, Variables.gallery_resize_video);
                }
            }


        }.execute();

    }


    @Override
    protected void onStart() {
        super.onStart();
        DeleteFile();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       DeleteFile();
    }

    public void DeleteFile(){
        File output = new File(Variables.outputfile);
        File output2 = new File(Variables.outputfile2);
        File output_filter_file = new File(Variables.output_filter_file);
        File gallery_trim_video = new File(Variables.gallery_trimed_video);
        File gallery_resize_video = new File(Variables.gallery_resize_video);

        if(output.exists()){
            output.delete();
        }
        if(output2.exists()){

            output2.delete();
        }
        if(output_filter_file.exists()){
            output_filter_file.delete();
        }

        if(gallery_trim_video.exists()){
            gallery_trim_video.delete();
        }

        if(gallery_resize_video.exists()){
            gallery_resize_video.delete();
        }



    }

}
