package com.smarthost.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import co.touchlab.ir.IssueReporter;
import co.touchlab.ir.MemLog;
import co.touchlab.ir.UserActionLog;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.smarthost.SmartHostApplication;
import com.smarthost.data.DatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kgalligan
 * Date: 6/11/13
 * Time: 8:29 AM
 */
public class TLog
{

    public static final String BUTTON_CLICKED = "button_clicked";
    public static final String VIEW_CLICKED = "view_clicked";
    public static final String RUNTIME_EXCEPTION = "runtime_exception";
    public static final String COMMAND_STARTED = "command_started";
    public static final String COMMAND_COMPLETED = "command_completed";
    public static final String PERMANENT_EXCEPTION = "permanent_exception";
    public static final String CLASS_CAST_EXCEPTION = "class_cast_exception";

    public static void i(Class c, String s)
    {
        String simpleName = c.getSimpleName();
        Log.i(simpleName, s);
        MemLog.i(simpleName, s);
    }

    public static void i(Class c, String s, Throwable t)
    {
        String simpleName = c.getSimpleName();
        Log.i(simpleName, s, t);
        MemLog.i(simpleName, s, t);
    }

    public static void e(Class c, String s)
    {
        String simpleName = c.getSimpleName();
        Log.e(simpleName, s);
        MemLog.e(simpleName, s);
    }

    public static void e(Class c, String s, Throwable t)
    {
        String simpleName = c.getSimpleName();
        Log.e(simpleName, s, t);
        MemLog.e(simpleName, s, t);
    }

    public static void e(Class c, Throwable t)
    {
        String simpleName = c.getSimpleName();
        Log.e(simpleName, null, t);
        MemLog.e(simpleName, t);
    }

    public static void d(Class c, String s)
    {
        String simpleName = c.getSimpleName();
        Log.d(simpleName, s);
        MemLog.d(simpleName, s);
    }

    public static void d(Class c, String s, Throwable t)
    {
        String simpleName = c.getSimpleName();
        Log.d(simpleName, s, t);
        MemLog.d(simpleName, s, t);
    }

    public static void w(Class c, String s)
    {
        String simpleName = c.getSimpleName();
        Log.w(simpleName, s);
        MemLog.w(simpleName, s);
    }

    public static void w(Class c, String s, Throwable t)
    {
        String simpleName = c.getSimpleName();
        Log.w(simpleName, s, t);
        MemLog.w(simpleName, s, t);
    }

    public static void ua(Class c, String s)
    {
        String simpleName = c.getSimpleName();
        Log.d(simpleName, s);
        MemLog.ua(simpleName, s);
    }

    public static void buttonClicked(Context context, Class c, String label)
    {
        String simpleName = c.getSimpleName();
        UserActionLog.buttonClicked(label);
        EasyTracker easyTracker = EasyTracker.getInstance(context);

        easyTracker.send(MapBuilder
                .createEvent(simpleName,
                        BUTTON_CLICKED,
                        label,
                        null)
                .build()
        );
    }

    public static void viewClicked(Context context, Class c, String label)
    {
        String simpleName = c.getSimpleName();
        UserActionLog.viewClicked(label);
        EasyTracker easyTracker = EasyTracker.getInstance(context);

        easyTracker.send(MapBuilder
                .createEvent(simpleName,
                        VIEW_CLICKED,
                        label,
                        null)
                .build()
        );
    }

    public static void runtimeExceptionThrown(Context context, Class c, String message, Throwable exception) throws FileNotFoundException {

        sendDatabaseWithIssueReport(context, exception);

        String simpleName = c.getSimpleName();
        EasyTracker easyTracker = EasyTracker.getInstance(context);

        easyTracker.send(MapBuilder
                .createEvent(simpleName,
                        RUNTIME_EXCEPTION,
                        message,
                        null)
                .build()
        );
    }

    public static void classCastExceptionThrown(Context context, Class c, String message, Throwable exception) throws FileNotFoundException {

        sendDatabaseWithIssueReport(context, exception);

        String simpleName = c.getSimpleName();
        EasyTracker easyTracker = EasyTracker.getInstance(context);

        easyTracker.send(MapBuilder
                .createEvent(simpleName,
                        CLASS_CAST_EXCEPTION,
                        message,
                        null)
                .build()
        );
    }

    public static void permanentExceptionThrown(Context context, String commandName, String message) throws FileNotFoundException {

        sendDatabaseWithIssueReport(context, null);

        EasyTracker easyTracker = EasyTracker.getInstance(context);

        easyTracker.send(MapBuilder
                .createEvent(commandName,
                        PERMANENT_EXCEPTION,
                        message,
                        null)
                .build()
        );
    }

    public static void commandStarted(Context context,  String commandName)
    {
        EasyTracker easyTracker = EasyTracker.getInstance(context);
        easyTracker.send(MapBuilder
                .createEvent(commandName,
                        COMMAND_STARTED,
                        null,
                        null)
                .build()
        );
    }

    public static void commandCompleted(Context context, String commandName)
    {
        EasyTracker easyTracker = EasyTracker.getInstance(context);
        easyTracker.send(MapBuilder
                .createEvent(commandName,
                        COMMAND_COMPLETED,
                        null,
                        null)
                .build()
        );
    }

    private static void sendDatabaseWithIssueReport(Context context, Throwable exception) throws FileNotFoundException {
        String inFileName = context.getDatabasePath(DatabaseHelper.DATABASE_FILE_NAME).getAbsolutePath();
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);
        IssueReporter.saveFile(context, fis, inFileName, "db", true);
        IssueReporter.sendIssueReport(context, true, PERMANENT_EXCEPTION, exception, SmartHostApplication.createIssueParams(context));
        try {
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void activityOpened(Context context, Activity activity){
        EasyTracker.getInstance(context).activityStart(activity);
    }

    public static void activityClosed(Context context, Activity activity){
        EasyTracker.getInstance(context).activityStop(activity);
    }

}