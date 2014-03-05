package com.smarthost;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.UUID;


/**
 * @author David Redding
 * @created 7/16/13 3:18 PM
 */
public class AppPreferences {

    private static AppPreferences INSTANCE;

    public static final String


            ADDRESS         = "address",
            PRIVATE_ROOM    = "private_room",
            ENTIRE_HOUSE    = "entire_house",
            BEDROOMS    = "bedrooms",
            BATHROOMS    = "bathrooms",
            OCCUPANCY    = "occupancy";



    public static final String DEFAULT_TIME_INTERVAL = "60";
    public static final int UNIT_METRIC     = 0;
    public static final int UNIT_IMPERIAL   = 1;


    public static synchronized AppPreferences getInstance(Context context) {
        if(INSTANCE == null)
        {
            INSTANCE = new AppPreferences(context);
        }
        return INSTANCE;
    }

    private final SharedPreferences mSp;

    private AppPreferences(Context context)
    {
        mSp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getAddress()
    {
        return mSp.getString(ADDRESS, "Brooklyn NY");
    }

    public void setAddress(String address)
    {
        applyPreference(ADDRESS, address);
    }

    public boolean getPrivateRoom()
    {
        return mSp.getBoolean(PRIVATE_ROOM, false);
    }

    public void setPrivateRoom(boolean privateRoom)
    {
        applyPreference(PRIVATE_ROOM, privateRoom);
    }

    public boolean getEntireHouse()
    {
        return mSp.getBoolean(ENTIRE_HOUSE, true);
    }

    public void setEntireHouse(boolean entireHouse)
    {
        applyPreference(ENTIRE_HOUSE, entireHouse);
    }

    public int getBedrooms()
    {
        return mSp.getInt(BEDROOMS, 1);
    }

    public void setBedrooms(int bedrooms)
    {
        applyPreference(BEDROOMS, bedrooms);
    }

    public int getBathrooms()
    {
        return mSp.getInt(BATHROOMS, 1);
    }

    public void setBathrooms(int bathrooms)
    {
        applyPreference(BATHROOMS, bathrooms);
    }

    public int getOccupancy()
    {
        return mSp.getInt(OCCUPANCY, 1);
    }

    public void setOccupancy(int occupancy)
    {
        applyPreference(OCCUPANCY, occupancy);
    }


    @SuppressLint("CommitPrefEdits")
    public void applyPreference(String key, float value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putFloat(key, value);
        editorApply(editor);
    }

    @SuppressLint("CommitPrefEdits")
    public void applyPreference(String key, String value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(key, value);
        editorApply(editor);
    }

    @SuppressLint("CommitPrefEdits")
    public void applyPreference(String key, boolean value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putBoolean(key, value);
        editorApply(editor);
    }

    @SuppressLint("CommitPrefEdits")
    public void applyPreference(String key, int value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(key, value);
        editorApply(editor);
    }

    @SuppressLint("CommitPrefEdits")
    public void applyPreference(String key, long value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putLong(key, value);
        editorApply(editor);
    }


    public static final boolean USE_APPLY = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;

    /**
     * {@link android.content.SharedPreferences.Editor#apply()} is quicker than
     * {@link android.content.SharedPreferences.Editor#commit()} which wouldn't matter except that writing to
     * preferences is IO which we want to minimize as much as possible
     * @param editor
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void editorApply(SharedPreferences.Editor editor)
    {
        if(USE_APPLY) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public SharedPreferences getPrefs() {
        return mSp;
    }

}
