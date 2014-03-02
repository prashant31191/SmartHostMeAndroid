package com.smarthost;

import android.app.Application;
import android.os.Build;
import android.os.Looper;
import android.os.StrictMode;

/**
 * @author David Redding
 * @created 7/16/13 3:18 PM
 */
public abstract class StrictApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        UiThreadContext.assertUiThread();

        triggerStrictMode();
    }

    private void triggerStrictMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.ThreadPolicy.Builder builder = new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog();
            if (Build.VERSION.SDK_INT >= 11)
                builder.penaltyFlashScreen();

            StrictMode.setThreadPolicy(builder.build());

            StrictMode.VmPolicy.Builder vmBuilder = new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .penaltyLog();
            //                    .penaltyDeath();
            if (Build.VERSION.SDK_INT >= 11)
                vmBuilder.detectLeakedClosableObjects();

            StrictMode.setVmPolicy(vmBuilder.build());
        }
    }

    private static final class UiThreadContext {
        public static void assertUiThread() {
            Thread uiThread = Looper.getMainLooper().getThread();
            Thread currentThread = Thread.currentThread();

            if (uiThread != currentThread)
                throw new RuntimeException("This call must be in UI thread");
        }

        public static void assertBackgroundThread() {
            try {
                Thread uiThread = Looper.getMainLooper().getThread();
                Thread currentThread = Thread.currentThread();

                if (uiThread == currentThread)
                    throw new RuntimeException("This call must be in background thread");
            } catch (Exception e) {
                //Probably in unit tests
                return;
            }
        }
    }

}
