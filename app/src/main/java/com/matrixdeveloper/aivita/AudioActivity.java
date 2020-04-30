package com.matrixdeveloper.aivita;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        ListView audioView = (ListView) findViewById(R.id.songView);

        ArrayList<String> audioList = new ArrayList<>();

        String[] proj = { MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME };// Can include more data for more details and check it.

        Cursor audioCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

        if(audioCursor != null){
            if(audioCursor.moveToFirst()){
                do{
                    int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);

                    audioList.add(audioCursor.getString(audioIndex));
                }while(audioCursor.moveToNext());
            }
        }
        audioCursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1, audioList);
        audioView.setAdapter(adapter);

    }
}