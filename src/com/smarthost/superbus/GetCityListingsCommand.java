package com.smarthost.superbus;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import co.touchlab.android.superbus.Command;
import co.touchlab.android.superbus.PermanentException;
import co.touchlab.android.superbus.TransientException;
import com.smarthost.data.DatabaseHelper;
import com.smarthost.data.Listing;
import com.smarthost.loaders.BroadcastSender;
import com.smarthost.network.DataHelper;
import com.smarthost.network.ListingHelper;
import com.smarthost.util.TLog;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 5:56 PM
 */
public class GetCityListingsCommand extends AbstractBaseNetworkCommand {

    String address;


    public GetCityListingsCommand() {
        setPriority(MUCH_HIGHER_PRIORITY);
    }

    public GetCityListingsCommand(String address) {
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
        return "listings/";
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
