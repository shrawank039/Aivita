package com.matrixdeveloper.aivita.SoundLists.StorageSounds;

import com.matrixdeveloper.aivita.SoundLists.Sounds_GetSet;

import java.util.ArrayList;

/**
 * Created by AQEEL on 2/22/2019.
 */


public class Stored_Sounds_GetSet {

  public String id,sound_name,description,section,thum,date_created;
  public String acc_path;
  //public String mp3_path;
}

class Stored_Sound_catagory_Get_Set {
public String catagory;
ArrayList<Stored_Sounds_GetSet> sound_list=new ArrayList<>();
}
