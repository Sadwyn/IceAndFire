package com.sadwyn.iceandfire.views.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.activities.MainActivity;

import static com.sadwyn.iceandfire.Constants.CSV_EXPORT_NOTIFICATION_ID;

public class ExportDataNotification {
    private Context context;

    public ExportDataNotification(Context context) {
        this.context = context;
    }

    public void showNotification() {
        Notification.Builder builder = new Notification.Builder(context)
                .setSmallIcon(android.R.drawable.stat_sys_upload)
                .setContentTitle("Exporting Data to CSV")
                .setContentText("This message will be hidden in few seconds");

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(CSV_EXPORT_NOTIFICATION_ID, builder.build());
    }


}
