package com.smarthost.superbus;

import android.content.Context;
import android.location.Address;
import co.touchlab.android.superbus.Command;
import co.touchlab.android.superbus.PermanentException;
import co.touchlab.android.superbus.TransientException;
import com.smarthost.data.DatabaseHelper;
import com.smarthost.data.Listing;
import com.smarthost.loaders.BroadcastSender;
import com.smarthost.network.ListingHelper;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * User: davidredding
 * Date: 3/4/14
 * Time: 9:10 PM
 */
public class GetPriceForAddressCommand extends AbstractBaseNetworkCommand {

    String address;
    String entire_home;
    String private_room;
    int bedrooms;
    int accommodates;
    int bathrooms;
    String date="";

    private static String ADDRESS = "address";
    private static String HOME = "entire_home";
    private static String PRIVATE_ROOM = "private_room";
    private static String BEDROOMS = "bedrooms";
    private static String ACCOMMODATES= "accommodates";
    private static String BATHROOMS = "bathrooms";
    private static String DATE = "date";

    public GetPriceForAddressCommand() {
        setPriority(MUCH_HIGHER_PRIORITY);
    }

    public GetPriceForAddressCommand(String address, String entire_home, String private_room, int bedrooms, int accommodates, int bathrooms, String date) {
        this.address = address;
        this.entire_home = entire_home;
        this.private_room = private_room;
        this.bedrooms = bedrooms;
        this.accommodates = accommodates;
        this.bathrooms = bathrooms;
        this.date = date;
    }

    public GetPriceForAddressCommand(Listing listing, boolean home, boolean room) {
        this.address = listing.addrees;
        this.entire_home = home+"";
        this.private_room = room+"";
        this.bedrooms = listing.bedrooms;
        this.accommodates = listing.occupancy;
        this.bathrooms = listing.bathrooms;
        this.date = "";
    }

    @Override
    public String logSummary() {
        return "Getting address listings: "+address;
    }

    @Override
    public boolean same(Command command) {
        if (command instanceof GetCityListingsCommand) {
            GetCityListingsCommand listingsCommand = (GetCityListingsCommand)command;
            if(this.address.equals(listingsCommand.address))
                return true;
        }
        return false;
    }

    @Override
    String getPath() {
        return "price/";
    }

    @Override
    void fillParams(ParameterMap parameterMap, Context context) throws SQLException, PermanentException, FileNotFoundException {
        parameterMap.add(ADDRESS,address);
        parameterMap.add(HOME, entire_home);
        parameterMap.add(ACCOMMODATES,accommodates+"");
        parameterMap.add(BEDROOMS,bedrooms+"");
        parameterMap.add(BATHROOMS,bathrooms+"");
        parameterMap.add(DATE,date+"");
    }


    @Override
    public void callCommand(Context context) throws TransientException, PermanentException {
        super.callCommand(context);
    }

    @Override
    void processResponse(HttpResponse response, final Context context) throws SQLException, JSONException {

        String jsonData = response.getBodyAsString();

        final JSONArray json = new JSONArray(jsonData);

        DatabaseHelper.getInstance(context).performDbTransactionOrThrowRuntime(new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {

                List<Listing> listings = ListingHelper.loadListings(context, json, address);

                return null;
            }
        }, "Blah. We should dump these messages.  Stack trace is enough.");

        BroadcastSender.makeListingBroadcast(context);


    }
}
