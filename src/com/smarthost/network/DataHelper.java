package com.smarthost.network;

import android.content.Context;
import co.touchlab.android.superbus.PermanentException;
import co.touchlab.android.superbus.TransientException;
import co.touchlab.android.superbus.http.BusHttpClient;
import com.smarthost.data.DatabaseHelper;
import com.smarthost.util.TLog;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: izzyoji
 * Date: 6/19/13
 * Time: 1:52 PM
 */
public class DataHelper
{

    public static final String APP_URL = "http://heavydev.herokuapp.com/";

    interface RunOp
    {
        String path();

        void fillParams(ParameterMap params);

        void jsonReply(JSONObject json) throws IOException, SQLException;
    }

    private static void runNetworkCall(final Context c, final RunOp op) throws TransientException, PermanentException
    {
        BusHttpClient httpClient = new BusHttpClient(APP_URL);

        httpClient.setConnectionTimeout(10000);
        ParameterMap parameterMap = httpClient.newParams();
        op.fillParams(parameterMap);

        HttpResponse httpResponse = httpClient.get(op.path(), parameterMap);

        httpClient.checkAndThrowError();

        if (httpResponse == null)
            throw new TransientException("No service/server not running");

        try
        {
            String jsonData = httpResponse.getBodyAsString();

            final JSONObject json = (JSONObject) new JSONTokener(jsonData).nextValue();

            DatabaseHelper.getInstance(c).performDbTransactionOrThrowRuntime(new Callable<Void>()
            {
                @Override
                public Void call() throws Exception
                {
                    op.jsonReply(json);
                    return null;
                }
            }, "Blah. We should dump these messages.  Stack trace is enough.");
        }
        catch (JSONException e)
        {
            try {
                TLog.runtimeExceptionThrown(c, DataHelper.class, e.getMessage(), e);
            } catch (FileNotFoundException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        }
        catch (ClassCastException e)
        {
            try {
                TLog.runtimeExceptionThrown(c, DataHelper.class, e.getMessage(), e);
            } catch (FileNotFoundException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        }
    }
}
