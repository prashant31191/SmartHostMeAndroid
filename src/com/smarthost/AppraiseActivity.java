package com.smarthost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.smarthost.ui.fragments.AppraiseFragment;
import com.smarthost.ui.fragments.FormFragment;
import com.smarthost.ui.fragments.ListingsFragment;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 2:53 PM
 */
public class AppraiseActivity extends BaseSecondaryActivity implements AppraiseFragment.AppraiseFragmentListener,
        FormFragment.FormFragmentListener{

    public static Intent getLaunchIntent(Context context) {
        Intent i = new Intent(context, AppraiseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews(getResources().getString(R.string.appraisals), R.drawable.ic_launcher, AppraiseFragment.newInstance());


    }

    @Override
    public void updateBedrooms(int number) {

        AppraiseFragment fragment = (AppraiseFragment) getSupportFragmentManager().findFragmentByTag(BaseSecondaryActivity.FRAGMENT_TAG);
        fragment.updateBedrooms(number);

    }

    @Override
    public void updateBathrooms(int number) {
        AppraiseFragment fragment = (AppraiseFragment) getSupportFragmentManager().findFragmentByTag(BaseSecondaryActivity.FRAGMENT_TAG);
        fragment.updateBathrooms(number);
    }

    @Override
    public void updateHomeOrPrivate(boolean home, boolean private_Room) {
        AppraiseFragment fragment = (AppraiseFragment) getSupportFragmentManager().findFragmentByTag(BaseSecondaryActivity.FRAGMENT_TAG);
        fragment.updateHomeOrPrivate(home,private_Room);
    }
}
