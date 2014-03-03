package com.smarthost.loaders;

import android.content.Context;
import com.smarthost.data.Listing;
import com.smarthost.data.query.ListingQueries;
import com.smarthost.util.TLog;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 5:47 PM
 */
public class ListingsLoader extends BaseAsyncLoader<List<Listing>> {


    private String searchQuery;

    public ListingsLoader(Context context, String searchQuery){
        super(context);
        this.searchQuery = searchQuery;
    }

    public void setSearchQuery(String searchQuery){
        this.searchQuery = searchQuery;
    }

    public ListingsLoader(Context context) {
        super(context);
    }

    @Override
    public List<Listing> loadInBackground() {

        List<Listing> listings= new ArrayList<Listing>();
        try {

                listings = ListingQueries.findListingForSearchQuery(getContext(), searchQuery);


        } catch (SQLException e){
            try {
                TLog.classCastExceptionThrown(getContext(), ListingsLoader.class, e.getMessage(), e);
            } catch (FileNotFoundException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        }
        return listings;

    }


    @Override
    protected String getBroadcastString() {
        return  BroadcastSender.LISTING_TABLE;
    }
}
