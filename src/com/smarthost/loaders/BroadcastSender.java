package com.smarthost.loaders;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 4:02 PM
 */
public class BroadcastSender
{

    /*
    One for each table
     */
    public static final String LISTING_TABLE = "LISTING_TABLE";


    public static void makeListingBroadcast(Context context){sendBroadcast(context, LISTING_TABLE);}

    private static void sendBroadcast(Context context, String broadcast)
    {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(broadcast));
    }


}
