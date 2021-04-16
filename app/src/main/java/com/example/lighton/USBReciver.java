package com.example.lighton;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.database.Cursor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class USBReciver extends BroadcastReceiver {
    private DBHelper database;

    @Override
    public void onReceive(Context context, Intent intent) {
        database =  new DBHelper(context);
        int state = getUSBcheck("usbCheck");
        if (state == 1){
            notify(context);
        }

    }
    public int getUSBcheck(String column){
        Cursor c = database.getData();
        c.moveToFirst();
        return c.getInt(c.getColumnIndex(column));
    }
    public void notify(  Context  context){
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("USB notification")
                .setContentText("The application is disconnected from power")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());

    }
}