package com.smarthost.loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * User: matthewdavis
 * Date: 9/10/13
 * Time: 1:20 PM
 */
public class LoaderBroadcastReceiver extends BroadcastReceiver
{
    private BaseAsyncLoader loader;

    public LoaderBroadcastReceiver(BaseAsyncLoader loader)
    {
        this.loader = loader;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        loader.onContentChanged();
    }
}
