package com.smarthost.superbus;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import co.touchlab.android.superbus.PermanentException;
import co.touchlab.android.superbus.TransientException;
import co.touchlab.android.superbus.http.BusHttpClient;
import co.touchlab.android.superbus.provider.sqlite.SqliteCommand;
import com.smarthost.ListingsActivity;
import com.smarthost.network.DataHelper;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 11:36 AM
 */
public abstract class AbstractBaseNetworkCommand extends SqliteCommand {

    private final String FAILED_TO_PARSE = "failed_to_parse";

    abstract String getPath();

    abstract void fillParams(ParameterMap parameterMap, Context context) throws SQLException, PermanentException, FileNotFoundException;

    abstract void processResponse(HttpResponse response, Context context) throws SQLException, JSONException;

    @Override
    public void callCommand(Context context) throws TransientException, PermanentException {

        BusHttpClient httpClient = new BusHttpClient(DataHelper.LOCALHOST);
        try
        {


            ParameterMap params = httpClient.newParams();

            fillParams(params, context);

            httpClient.setConnectionTimeout(5000);
            HttpResponse httpResponse = httpClient.get(getPath(), params);

            //Check if anything went south
            httpClient.checkAndThrowError();

            if(httpResponse == null)
                throw new TransientException("No service/server not running");

            processResponse(httpResponse, context);

        } catch (SQLException e) {
            broadcastFail(context);
            throw new PermanentException(e);
        } catch (JSONException e) {
            broadcastFail(context);
            throw new PermanentException(e);
        } catch (FileNotFoundException e) {
            broadcastFail(context);
            throw new RuntimeException(e);
        }
    }

    private void broadcastFail(Context context){

        Intent intent = new Intent(FAILED_TO_PARSE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
