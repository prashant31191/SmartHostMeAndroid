package com.smarthost.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.smarthost.R;
import com.smarthost.ui.fragments.CalendarFragment;
import com.smarthost.ui.fragments.FormFragment;
import com.smarthost.ui.fragments.ListingsFragment;

import java.util.List;

/**
 * User: davidredding
 * Date: 3/4/14
 * Time: 6:40 PM
 */
public class AppraiseFormFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private static int LOCAL_FRAGMENT = 0;
    private static int CALENDAR_FRAGMENT = 1;

    public AppraiseFormFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if( position == LOCAL_FRAGMENT){
            return FormFragment.newInstance(R.layout.fragment_form_rooms);
        } else if(position == CALENDAR_FRAGMENT){
            return FormFragment.newInstance(R.layout.fragment_form_sleeping);
        }
        else
            throw new RuntimeException();
    }

    @Override
    public int getCount() {
        return 2;
    }


}

