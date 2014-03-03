package com.smarthost.network.asynctasks;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import co.touchlab.android.superbus.BusHelper;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.smarthost.data.DatabaseHelper;
import com.smarthost.data.Listing;
import com.smarthost.data.query.ListingQueries;
import com.smarthost.loaders.BroadcastSender;
import com.smarthost.superbus.GetCityListingsCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 6:22 PM
 */
public class GetLocalListings implements Runnable{

    private String searchQuery;
    private Context context;

    public static final String SUCCESSFUL_LISTINGS = "successful_listings";
    public static final String LISTINGS = "listings";

    private List<Listing> listings;

    public GetLocalListings(Context context, String searchQuery) {
        this.context = context;
        this.searchQuery = searchQuery;
    }

    @Override
    public void run(){
        DatabaseHelper.getInstance(context).performDbTransactionOrThrowRuntime(
                new Callable<Void>() {


                    @Override
                    public Void call() throws Exception {

                        listings = ListingQueries.findListingForSearchQuery(context, searchQuery);

                        return null;
                    }
                }, "Could not add exercise to My Exercises");

        if(listings.size()>0)
            BroadcastSender.makeListingBroadcast(context);
        else
            BusHelper.submitCommandAsync(context, new GetCityListingsCommand(searchQuery));
    }

}
