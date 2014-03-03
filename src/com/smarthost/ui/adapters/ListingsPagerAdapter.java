package com.smarthost.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import com.smarthost.R;
import com.smarthost.ui.fragments.CalendarFragment;
import com.smarthost.ui.fragments.ListingsFragment;

import java.util.Calendar;


/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 10:36 AM
 */
public class ListingsPagerAdapter extends FragmentStatePagerAdapter {

    private static int LOCAL_FRAGMENT = 0;
    private static int CALENDAR_FRAGMENT = 1;

    private final Context context;

    private boolean detailsShown = false;

    public ListingsPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position){


        if( position == LOCAL_FRAGMENT){
                return ListingsFragment.newInstance();
        } else if(position == CALENDAR_FRAGMENT){
                return CalendarFragment.newInstance();
        }

        throw new RuntimeException();
    }

    @Override
    public int getCount(){
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position){

        if(position==LOCAL_FRAGMENT){
            return context.getString(R.string.local);
        }else if(position==CALENDAR_FRAGMENT){
            return context.getString(R.string.calendar);
        }

        Log.d("position: ", position + "");

        throw new RuntimeException();
    }

    public boolean isDetailsShown() {
        return detailsShown;
    }

    public void setDetailsShown(boolean detailsShown) {
        this.detailsShown = detailsShown;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
