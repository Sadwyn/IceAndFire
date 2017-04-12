package com.sadwyn.iceandfire.views.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.sadwyn.iceandfire.activities.MainActivity;

import static com.sadwyn.iceandfire.Constants.CSV_EXPORT_NOTIFICATION_ID;

public class ExportDataNotification {
    private Context context;

    public ExportDataNotification(Context context) {
        this.context = context;
    }

    public void showNotification(){
        NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("Exporting Data to CSV")
                .setContentText("This message will hide in few seconds");

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        notificationManager.notify(CSV_EXPORT_NOTIFICATION_ID, builder.build());
    }


}
