package com.smarthost.loaders;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;

/**
 * User: matthewdavis
 * Date: 9/9/13
 * Time: 2:02 PM
 */
public abstract class BaseAsyncLoader<D> extends AsyncTaskLoader<D>
{
    private D data;
    private LoaderBroadcastReceiver loaderBroadcastReceiver = null;

    public BaseAsyncLoader(Context context)
    {
        super(context);
    }

    @Override
    protected void onStartLoading()
    {
        if (data != null)
        {
            deliverResult(data);
        }

        if(loaderBroadcastReceiver == null)
        {
            loaderBroadcastReceiver = new LoaderBroadcastReceiver(this);
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(loaderBroadcastReceiver, new IntentFilter(getBroadcastString()));
        }

        if (takeContentChanged() || data == null)
        {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(D data)
    {
        if (isReset())
        {
            if (data != null)
            {
                onReleaseResources(data);
            }
        }

        D oldData = this.data;
        this.data = data;

        if(isStarted())
        {
            super.deliverResult(data);
        }

        if(oldData != null)
        {
            onReleaseResources(oldData);
        }
    }

    @Override
    protected void onStopLoading()
    {
        cancelLoad();
    }

    @Override
    protected void onReset()
    {
        super.onReset();

        onStopLoading();

        if(data != null)
        {
            onReleaseResources(data);
            data = null;
        }

        if(loaderBroadcastReceiver != null)
        {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(loaderBroadcastReceiver);
            loaderBroadcastReceiver = null;
        }
    }

    protected void onReleaseResources(D data){}
    protected abstract String getBroadcastString();
}
