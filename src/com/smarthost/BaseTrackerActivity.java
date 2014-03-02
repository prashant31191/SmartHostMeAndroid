package com.smarthost;

import android.support.v4.app.FragmentActivity;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.smarthost.util.TLog;

/**
 * Created with IntelliJ IDEA.
 * User: davidredding
 * Date: 7/16/13
 * Time: 5:30 PM

 */

public abstract class BaseTrackerActivity extends FragmentActivity
{
    @Override
    protected void onStart()
    {
        super.onStart();
        TLog.ua(getClass(), "onStart");
        TLog.activityOpened(this,this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //If an activity is called on top of the current activity,
        //Cancel the supertoast so that it does not linger in the background
        SuperActivityToast.cancelAllSuperActivityToasts();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        TLog.ua(getClass(), "onStop");
        TLog.activityClosed(this,this);
    }
}

