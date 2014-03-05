package com.smarthost.superbus;

import android.content.Context;
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
 * Time: 11:06 PM
 */
public class GetGeoCodeCommand extends AbstractBaseNetworkCommand {

    String address;


    public GetGeoCodeCommand() {
        setPriority(MUCH_HIGHER_PRIORITY);
    }

    public GetGeoCodeCommand(String address) {
        this.address= address;
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
        return "geocode/";
    }

    @Override
    void fillParams(ParameterMap parameterMap, Context context) throws SQLException, PermanentException, FileNotFoundException {
        parameterMap.add("q",address);
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

