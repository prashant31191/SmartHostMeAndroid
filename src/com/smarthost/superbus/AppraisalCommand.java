package com.smarthost.superbus;

import android.content.Context;
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
 * Time: 11:43 AM
 */
public class AppraisalCommand extends AbstractBaseNetworkCommand {

    @Override
    String getPath() {
        return null;
    }

    @Override
    void fillParams(ParameterMap parameterMap, Context context) throws SQLException, PermanentException, FileNotFoundException {

    }

    @Override
    void processResponse(HttpResponse response, Context context) throws SQLException, JSONException {

    }


    /*
    WCDNE
     */

    @Override
    public String logSummary() {
        return null;
    }

    @Override
    public boolean same(Command command) {
        return false;
    }

    @Override
    public void callCommand(Context context) throws TransientException, PermanentException {
        super.callCommand(context);
    }
}
