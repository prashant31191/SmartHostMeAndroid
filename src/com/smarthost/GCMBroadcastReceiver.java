package com.smarthost;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 1:52 PM
 */

public class GCMBroadcastReceiver extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 465;
    public static final String GCM_FIELD_INFO = "info";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String messageType = gcm.getMessageType(intent);

        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
        {
            sendNotification("Send error: " + intent.getExtras().toString(), context);
        }
        else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
        {
            sendNotification("Deleted messages on server: " + intent.getExtras().toString(), context);
        }
        else
        {
            sendNotification("Update: " + intent.getStringExtra(GCM_FIELD_INFO), context);
        }

        setResultCode(Activity.RESULT_OK);
    }

    // Put the GCM message into a notification and post it.
    private void sendNotification(String msg, Context context)
    {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                MyActivity.getLaunchIntent(context), 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(context.getString(R.string.push))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.drawable.ic_launcher))
                        .setSmallIcon(R.drawable.ic_launcher);

        builder.setContentIntent(contentIntent);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}
