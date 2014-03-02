package com.smarthost;

import android.content.Context;
import android.content.Intent;

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
}
