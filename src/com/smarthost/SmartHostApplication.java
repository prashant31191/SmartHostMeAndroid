package com.smarthost;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import co.touchlab.android.superbus.*;
import co.touchlab.android.superbus.log.BusLog;
import co.touchlab.android.superbus.provider.PersistedApplication;
import co.touchlab.android.superbus.provider.PersistenceProvider;
import co.touchlab.android.superbus.provider.gson.GsonSqlitePersistenceProvider;
import co.touchlab.android.superbus.provider.sqlite.SQLiteDatabaseFactory;
import co.touchlab.ir.IssueParam;
import co.touchlab.ir.process.AppMonitorDefaultExceptionHandler;
import co.touchlab.ir.util.ExceptionCallBack;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.smarthost.data.DatabaseHelper;
import com.smarthost.util.TLog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 10:42 AM
 */
public class SmartHostApplication extends StrictApplication implements PersistedApplication {

    private GsonSqlitePersistenceProvider mPersistenceProvider;
    private final static String SCOPE = "audience:server:client_id:19639029526-3t2g4tphhfeep7d0nl1eam0olls08vca.apps.googleusercontent.com";
    //TODO only for use in enums, this will change
    public static Context context;

    public static final boolean DEBUG = true;

    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;

        if (!DEBUG)
            setupTouchTrack();

        try
        {
            mPersistenceProvider = new GsonSqlitePersistenceProvider(new MyDatabaseFactory());

            new Thread()
            {
                @Override
                public void run()
                {
                    SuperbusService.notifyStart(SmartHostApplication.this);
                    //BusHelper.submitCommandSync(SmartHostApplication.this, new GlobalConfigDataLoadCommand());
                }
            }.start();
        }

        catch (StorageException e)
        {
            try {
                TLog.runtimeExceptionThrown(SmartHostApplication.this, SmartHostApplication.class, e.getMessage(), e);
            } catch (FileNotFoundException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        }
    }

    private void setupTouchTrack()
    {
        AppMonitorDefaultExceptionHandler.replaceExceptionHandler(this, new ExceptionCallBack() {
            @Override
            public List<IssueParam> exceptionTrigger(Context context) {

                return createIssueParams(SmartHostApplication.this);
            }
        });
    }

    public static List<IssueParam> createIssueParams(Context context)
    {
        AppPreferences appPreferences = AppPreferences.getInstance(context);
        List<IssueParam> params = new ArrayList<IssueParam>();
        params.add(new IssueParam("Google Account", appPreferences.getAccountEmail()));
        params.add(new IssueParam("UUID", appPreferences.getUuid()));
        return params;
    }

//    public static String getToken(Context context) {
//        final Random randomGenerator = new Random();
//        String token = null;
//        for (int wait = 0; wait < 5; ++wait) {
//
//            try {
//                token = GoogleAuthUtil.getToken(context,
//                        AppPreferences.getInstance(context).getAccountEmail(), SCOPE);
//
//            } catch (IOException e) {
//                // Apply exponential backoff.
//                try {
//                    Thread.sleep((1 << wait) * 1000 + randomGenerator.nextInt(1001));
//                } catch (InterruptedException e1) {
//                    throw new RuntimeException(e1);
//                }
//
//            } catch (GoogleAuthException e) {
//                // This is likely unrecoverable.
////                        throw new RuntimeException(e);
//            }
//        }
//        return token;
//    }

    private final class MyDatabaseFactory implements SQLiteDatabaseFactory
    {
        @Override
        public SQLiteDatabase getDatabase()
        {
            return DatabaseHelper.getInstance(SmartHostApplication.this).getWritableDatabase();
        }
    }

    @Override
    public PersistenceProvider getProvider()
    {
        return mPersistenceProvider;
    }

    @Override
    public BusLog getLog()
    {
        return null;
    }

    @Override
    public SuperbusEventListener getEventListener()
    {
        return null;
    }

    @Override
    public CommandPurgePolicy getCommandPurgePolicy()
    {
        return null;
    }

    @Override
    public ForegroundNotificationManager getForegroundNotificationManager()
    {
        return null;
    }
//
//    public static void doBigSync(final Context context) {
//        new Thread()
//        {
//            @Override
//            public void run()
//            {
//                AppPreferences.getInstance(context).setSync(true);
//                BusHelper.submitCommandSync(context, new BigSyncCommand());
//            }
//        }.start();
//    }

}