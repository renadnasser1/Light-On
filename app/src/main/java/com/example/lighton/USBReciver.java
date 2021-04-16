package com.example.lighton;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.database.Cursor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.usb.UsbManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class USBReciver extends BroadcastReceiver {
    private DBHelper database;

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = getData("usbCheck");
        Log.d("intentaction", intent.getAction());
        database =  new DBHelper(context);
        Toast.makeText(context, intent.getAction(),
                Toast.LENGTH_LONG).show();

     Toast.makeText(context, ""+state, Toast.LENGTH_SHORT).show();



        notify(context);

    }
    public String getData(String column){
        Cursor c = database.getData();
        c.moveToFirst();
        return c.getString(c.getColumnIndex(column));
    }
    public void notify(  Context  context){
        Intent intent2 = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(context);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("USB notification")
                .setContentText("The application is disconnected from powe")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);



        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());

    }
}