package com.smarthost.superbus;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import co.touchlab.android.superbus.Command;
import co.touchlab.android.superbus.PermanentException;
import co.touchlab.android.superbus.TransientException;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 5:56 PM
 */
public class GetCityListingsCommand extends AbstractBaseNetworkCommand {

    public static final String SUCCESSFUL_LISTINGS = "successful_listings";
    public static final String LISTINGS = "listings";

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
    void processResponse(HttpResponse response, Context context) throws SQLException, JSONException {

        String text = response.getBodyAsString();

        Intent intent = new Intent().setAction(SUCCESSFUL_LISTINGS).putExtra(LISTINGS, text);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }
}
