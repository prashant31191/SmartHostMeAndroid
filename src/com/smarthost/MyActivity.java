package com.smarthost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.smarthost.ui.fragments.MyFragment;

public class MyActivity extends BaseActivity {


    public static Intent getLaunchIntent(Context context) {
        Intent i = new Intent(context, MyActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews("Intro", R.drawable.ic_launcher, MyFragment.newInstance());
    }
}
