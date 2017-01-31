package com.individual.noteapp.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.individual.noteapp.application.AppController;


/**
 * Created by Blackout on 1/28/2017.
 */

public class AppSharePrefs {

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    private static final String MY_PREFERENCES = "noteAppPrefs" ;
    private static final String APP_OPEN_COUNTER = "openCounter";

    private static final String LAST_FOLDER = "lastFolder";

    public AppSharePrefs(Context context) {
        sharedpreferences = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    public static AppSharePrefs getInstance() {
        return new AppSharePrefs(AppController.getContext());
    }

    public boolean saveLastFolderId(Long folderId) {
        return putLongValue(LAST_FOLDER,folderId);
    }

    public Long getLastFolderId() {
        Long lastFolderId = getLongValue(LAST_FOLDER);
       return lastFolderId == Long.MIN_VALUE ? Long.valueOf(Integer.MAX_VALUE) : lastFolderId;
    }

    public boolean isFirstTimeOpenApp() {
        if(!sharedpreferences.getBoolean(APP_OPEN_COUNTER , false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(APP_OPEN_COUNTER, true);
            editor.apply();
            return true;
        }
        else {
            return false;
        }
    }

    public boolean putStringValue(String key , String value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();

        return editor.commit();
    }

    public String getStringValue(String key)
    {
        return sharedpreferences.getString(key, "");
    }

    public boolean putIntValue(String key , int value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.apply();

        return editor.commit();
    }

    public int getIntValue(String key)
    {
        return sharedpreferences.getInt(key, Integer.MIN_VALUE);
    }

    public boolean putLongValue(String key , long value) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(key, value);
        editor.apply();

        return editor.commit();
    }

    public long getLongValue(String key)
    {
        return sharedpreferences.getLong(key, Long.MIN_VALUE);
    }

    public boolean updateStringValue(String key , String value) {

        editor.putString(key , value);

        return editor.commit();
    }

    public boolean updateIntValue(String key , int value) {

        editor.putInt(key, value);

        return editor.commit();
    }

    public boolean updateLongValue(String key , long value) {

        editor.putLong(key , value);

        return editor.commit();
    }



}
