package com.smarthost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.smarthost.ui.fragments.ListingsFragment;
import com.smarthost.ui.fragments.SettingsFragment;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 3:04 PM
 */
public class SettingsActivity extends BaseActivity {

    public static Intent getLaunchIntent(Context context) {
        Intent i = new Intent(context, SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews("Settings", R.drawable.ic_launcher, SettingsFragment.newInstance());
    }


}
