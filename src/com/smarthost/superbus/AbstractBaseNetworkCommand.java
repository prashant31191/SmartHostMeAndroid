package com.smarthost.superbus;

import android.content.Context;
import co.touchlab.android.superbus.PermanentException;
import co.touchlab.android.superbus.TransientException;
import co.touchlab.android.superbus.http.BusHttpClient;
import co.touchlab.android.superbus.provider.sqlite.SqliteCommand;
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

    abstract String getPath();

    abstract void fillParams(ParameterMap parameterMap, Context context) throws SQLException, PermanentException, FileNotFoundException;

    abstract void processResponse(HttpResponse response, Context context) throws SQLException, JSONException;

    @Override
    public void callCommand(Context context) throws TransientException, PermanentException {

        BusHttpClient httpClient = new BusHttpClient(DataHelper.APP_URL);
        try
        {

            ParameterMap params = httpClient.newParams();

            fillParams(params, context);

            httpClient.setConnectionTimeout(10000);
            HttpResponse httpResponse = httpClient.post(getPath(), params);

            //Check if anything went south
            httpClient.checkAndThrowError();

            if(httpResponse == null)
                throw new TransientException("No service/server not running");

            processResponse(httpResponse, context);

        } catch (SQLException e) {
            throw new PermanentException(e);
        } catch (JSONException e) {
            throw new PermanentException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
