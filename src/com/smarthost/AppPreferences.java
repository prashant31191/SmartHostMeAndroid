package com.smarthost;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
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
            UUID_KEY = "uuid",
            SYNC = "sync",

    ACCOUNT_EMAIL   = "account_email",
            ACCOUNT_NAME    = "account_name",
            ACCOUNT_AVATAR  = "account_avatar",

    UNITS = "units",
            ANIMATIONS ="animations",
            LAST_SYNC_TIME = "last_sync_time",
            DEFAULT_WEIGHT = "default_weight",
            DEFAULT_REPS = "default_reps",
            WEIGHT_INTERVAL = "weight_interval",

    AGENDA_FRAG_HINT = "agenda_frag_hint",
            EXPLORE_EXERCISES_FRAG_HINT = "explore_exercises_frag_hint",
            EXPLORE_WORKOUTS_FRAG_HINT = "explore_workouts_frag_hint",
            FIRST_RUN = "first_run",

    DOWNLOADED_CONTENT = "downloaded_content",
            TIMER_SECONDS = "timer_seconds",
            REPEAT_TIMER = "repeat_timer",

    PROFILE_PHOTO_CURRENT = "profile_photo_current",
            PROFILE_PHOTO_START = "profile_photo_start",
            PROFILE_PHOTO_DATE_CURRENT = "profile_photo_date_current",
            PROFILE_PHOTO_DATE_START = "profile_photo_date_start",
            PROFILE_WEIGHT_CURRENT = "profile_weight_current",
            PROFILE_WEIGHT_START = "profile_weight_start",

    CUSTOM = "other",
            ABS = "abs",
            BACK = "back",
            UPPER_ARMS = "biceps",
            CHEST="chest",
            CARDIO ="cardio",
            FOREARMS = "forearms",
            CALVES = "lower_legs",
            SHOULDERS ="shoulders",
            TRICEPS ="triceps",
            THIGHS = "upper_legs";

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

    /**
     * Each application will have an associated UUID and will be the same from install to uninstall except for
     * different accounts on multi user account devices (introduced in 4.2)
     * @return A UUID that will never be NULL
     */
    public String getUuid() {
        String uuid = mSp.getString(UUID_KEY, null);
        if(uuid == null)
        {
            uuid = setUuid();
        }
        return uuid;
    }

    private String setUuid() {
        String sUuid = UUID.randomUUID().toString();
        applyPreference(UUID_KEY, sUuid);
        return sUuid;
    }


    public boolean isSync()
    {
        return mSp.getBoolean(SYNC, false);
    }

    public void setSync(boolean val)
    {
        SharedPreferences.Editor edit = mSp.edit();
        edit.putBoolean(SYNC, val);
        edit.commit();
    }

    public String getAccountEmail()
    {
        return mSp.getString(ACCOUNT_EMAIL, "");
    }

    public void setAccountEmail(String email)
    {
        applyPreference(ACCOUNT_EMAIL, email);
    }

    public String getAvatarUrl()
    {
        return mSp.getString(ACCOUNT_AVATAR, "");
    }

    public void setAvatarUrl(String avatarUrl)
    {
        applyPreference(ACCOUNT_AVATAR, avatarUrl);
    }

    public String getAccountName()
    {
        return mSp.getString(ACCOUNT_NAME, "");
    }

    public void setAccountName(String accountName)
    {
        applyPreference(ACCOUNT_NAME, accountName);
    }

    public int getUnits()
    {
        return mSp.getInt(UNITS, UNIT_IMPERIAL);
    }

    public void setUnits(int units)
    {
        applyPreference(UNITS, units);
    }

    public void setDefaultWeight(int weight){
        applyPreference(DEFAULT_WEIGHT, String.valueOf(weight));
    }

    public int getWeight(){
        String value = mSp.getString(DEFAULT_WEIGHT , "100");
        return Integer.parseInt(value);
    }

    public void setDefaultReps(int reps) {
        applyPreference(DEFAULT_REPS, String.valueOf(reps));
    }

    public int getReps()
    {
        String value = mSp.getString(DEFAULT_REPS , "10");
        return Integer.parseInt(value);
    }

    public void setWeightInterval(int weightInterval)
    {
        applyPreference(WEIGHT_INTERVAL, weightInterval);
    }

    public int getWeightInterval()
    {
        return mSp.getInt(WEIGHT_INTERVAL, 5);
    }

    public boolean getAnimations()
    {
        return mSp.getBoolean(ANIMATIONS, true);
    }

    public void setAnimations(boolean animations)
    {
        applyPreference(ANIMATIONS, animations);
    }

    public long getLastSyncTime()
    {
        return mSp.getLong(LAST_SYNC_TIME, 0);
    }

    public void setLastSyncTime(long lastSyncTime)
    {
        applyPreference(LAST_SYNC_TIME, lastSyncTime);
    }

    public void setAgendaFragHint(boolean agendaFragHint)
    {
        applyPreference(AGENDA_FRAG_HINT, agendaFragHint);
    }

    public boolean getAgendaFragHint() {
        return mSp.getBoolean(AGENDA_FRAG_HINT, false);
    }

    public void setExploreExercisesFragHint(boolean exploreExercisesFragHint)
    {
        applyPreference(EXPLORE_EXERCISES_FRAG_HINT, exploreExercisesFragHint);
    }

    public boolean getExploreExercisesFragHint() {
        return mSp.getBoolean(EXPLORE_EXERCISES_FRAG_HINT, false);
    }

    public void setExploreWorkoutsFragHint(boolean exploreWorkoutsFragHint)
    {
        applyPreference(EXPLORE_WORKOUTS_FRAG_HINT, exploreWorkoutsFragHint);
    }

    public boolean getExploreWorkoutsFragHint() {
        return mSp.getBoolean(EXPLORE_WORKOUTS_FRAG_HINT, false);
    }

    public boolean isFirstRun() {
        return mSp.getBoolean(FIRST_RUN, true);
    }

    public void setHasRun() {
        applyPreference(FIRST_RUN, false);
    }

    public boolean downloadedContent() {
        return mSp.getBoolean(DOWNLOADED_CONTENT, false);
    }

    public void setDownloadedContent() {
        applyPreference(DOWNLOADED_CONTENT, true);
    }

    public int getTimerSeconds() {
        return mSp.getInt(TIMER_SECONDS, 30);
    }

    public void setTimerSeconds(int setSeconds) {
        applyPreference(TIMER_SECONDS, setSeconds);
    }

    public boolean getRepeatTimer() {
        return mSp.getBoolean(REPEAT_TIMER, false);
    }

    public void setRepeatTimer(boolean repeatTimer) {
        applyPreference(REPEAT_TIMER, repeatTimer);
    }





    public String getProfileCurrentPhoto() {
        return mSp.getString(PROFILE_PHOTO_CURRENT, null);
    }

    public void setProfileCurrentPhoto(String url) {
        applyPreference(PROFILE_PHOTO_CURRENT, url);
    }


    public void setProfileCurrentPhotoDate(long date) {
        applyPreference(PROFILE_PHOTO_DATE_CURRENT, date);
    }

    public long getProfileCurrentPhotoDate() {
        return mSp.getLong(PROFILE_PHOTO_DATE_CURRENT, System.currentTimeMillis());
    }

    public void setProfileCurrentWeight(int weight) {
        applyPreference(PROFILE_WEIGHT_CURRENT, weight);
    }

    public int getProfileCurrentWeight() {
        return mSp.getInt(PROFILE_WEIGHT_CURRENT, 0);
    }






    public String getProfileStartPhoto() {
        return mSp.getString(PROFILE_PHOTO_START, null);
    }

    public void setProfileStartPhoto(String url) {
        applyPreference(PROFILE_PHOTO_START, url);
    }



    public void setProfileStartPhotoDate(long date) {
        applyPreference(PROFILE_PHOTO_DATE_START, date);
    }

    public long getProfileStartPhotoDate() {
        return mSp.getLong(PROFILE_PHOTO_DATE_START, System.currentTimeMillis());
    }



    public void setProfileStartWeight(int weight) {
        applyPreference(PROFILE_WEIGHT_START, weight);
    }

    public int getProfileStartWeight() {
        return mSp.getInt(PROFILE_WEIGHT_START, 0);
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
