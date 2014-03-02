package com.smarthost.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import co.touchlab.android.superbus.provider.sqlite.AbstractSqlitePersistenceProvider;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.smarthost.SmartHostApplication;
import com.smarthost.util.TLog;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by kgalligan on 5/19/13.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{
    public final static String DATABASE_FILE_NAME = "smarthostdb";
    private final static int BETA = 13;
    private final static int CURRENT = 15;

    private Context mContext;

    private static DatabaseHelper instance;

    public synchronized static DatabaseHelper getInstance(Context context)
    {
        if (instance == null)
            instance = new DatabaseHelper(context);

        return instance;
    }

    private DatabaseHelper(Context context)
    {
        super(context, DATABASE_FILE_NAME, null, CURRENT);
        mContext = context.getApplicationContext();
    }

    @Override
    public void onOpen(SQLiteDatabase db)
    {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    // @reminder Ordering matters, create foreign key dependant classes later
    private final Class[] mTableClasses = new Class[]{

//            Exercise.class


    };

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
    {
        try
        {
            for (Class mTableClass : mTableClasses)
            {
                TableUtils.getCreateTableStatements(connectionSource, mTableClass);
                TableUtils.createTable(connectionSource, mTableClass);
            }

            ((AbstractSqlitePersistenceProvider) ((SmartHostApplication) mContext).getProvider()).createTables(db);

        }
        catch (SQLException e)
        {
            try {

                TLog.runtimeExceptionThrown(mContext, DatabaseHelper.class, e.getMessage(), e);
            } catch (FileNotFoundException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException("Unable to create db");
        }

    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final ConnectionSource connectionSource, int oldVersion,
                          int newVersion){
    }

    public TransactionManager createTransactionManager()
    {
        return new TransactionManager(getConnectionSource());
    }

    /**
     * @param transaction
     * @param exceptionMessage
     * @throws RuntimeException on {@link SQLException}
     */
    public void performDbTransactionOrThrowRuntime(Callable<Void> transaction, String exceptionMessage) {
        try {
            TransactionManager transactionManager = createTransactionManager();
            transactionManager.callInTransaction(transaction);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), e.getMessage());
            try {
                TLog.runtimeExceptionThrown(mContext, DatabaseHelper.class, exceptionMessage, e);
            } catch (FileNotFoundException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(exceptionMessage, e);
        }
    }

    public static void backupDatabase(Context context) throws IOException {

        File dbFile = context.getDatabasePath(DATABASE_FILE_NAME);
        FileInputStream fis = new FileInputStream(dbFile);

        File outputDirectory = new File(
                Environment.getExternalStorageDirectory(), "DB_BACKUP");
        outputDirectory.mkdirs();



        String backupFileName = "smarthost_" + System.currentTimeMillis() + ".db3";

        File outputFile = new File(outputDirectory, backupFileName);

        OutputStream output = new FileOutputStream(outputFile);


        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        fis.close();

    }


}
