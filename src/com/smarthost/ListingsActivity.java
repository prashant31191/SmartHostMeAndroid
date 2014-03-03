package com.smarthost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import com.smarthost.ui.adapters.ListingsPagerAdapter;
import com.smarthost.ui.fragments.CalendarFragment;
import com.smarthost.ui.fragments.ListingsFragment;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 2:52 PM
 */
public class ListingsActivity extends BaseActivity implements
        ListingsFragment.ListingsFragmentListener,
        CalendarFragment.CalendarFragmentListener{

    String title;
    private ListingsPagerAdapter pagerAdapter;

    public static Intent getLaunchIntent(Context context) {
        Intent i = new Intent(context, ListingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        title = getString(R.string.listings);


        pagerAdapter = new ListingsPagerAdapter(getSupportFragmentManager(), this);
        initViews(title, pagerAdapter);

    }





    @Override
    public void buttonClicked() {

    }


}
