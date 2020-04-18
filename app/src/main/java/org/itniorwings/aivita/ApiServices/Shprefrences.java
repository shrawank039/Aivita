package org.itniorwings.aivita.ApiServices;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;

import com.google.gson.Gson;

import org.itniorwings.aivita.model.LoginModel;

/**
 * Created by aphroecs on 8/23/2016.
 */
public class Shprefrences {

    public static final String PREFERENCES = "App";
    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public void clearData()
    {
        editor.remove(PREFERENCES);
        editor.clear();
        editor.commit();
    }

    public Shprefrences(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    public void setString(String key, String val) {
        editor.putString(key, val);
        editor.commit();
    }

    public void setBoolean(String key, boolean val) {
        editor.putBoolean(key, val);
        editor.commit();
    }

    public void setInt(String key, int val) {
        editor.putInt(key, val);
        editor.commit();
    }


    public String getString(String key, String val) {
        return sharedpreferences.getString(key,val).toString();
    }

    public boolean getBoolean(String key, boolean val) {
        return sharedpreferences.getBoolean(key,val);
    }


    public void setLoginModel(String key, LoginModel obj)
    {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key, json);
        editor.commit();
    }

    public LoginModel getLoginModel(String key)
    {
        Gson gson = new Gson();
        String json = sharedpreferences.getString(key, "");
        LoginModel ob= gson.fromJson(json, LoginModel.class);
        return ob;
    }


    public void setRington(String key, Ringtone obj)
    {
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString(key, json);
        editor.commit();
    }

    public Ringtone getRington(String key)
    {
        Gson gson = new Gson();
        String json = sharedpreferences.getString(key, "");
        Ringtone ob= gson.fromJson(json, Ringtone.class);
        return ob;
    }


    public int getInt(String key, int val) {
        return sharedpreferences.getInt(key,val);
    }

}
