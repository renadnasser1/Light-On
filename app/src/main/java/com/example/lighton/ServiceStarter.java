package com.example.lighton;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.database.Cursor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class ServiceStarter extends BroadcastReceiver {
    public String myPhoneNumber;
    private DBHelper database;
    @Override
    public void onReceive(Context context, Intent intent) {
            database =  new DBHelper(context);
            String number = getData();
//            Toast.makeText(context,number,Toast.LENGTH_SHORT);
            // Checks Sim card State
            TelephonyManager telephoneMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int simState = telephoneMgr.getSimState();

            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    Log.i("SimStateListener", "Sim State absent");
                    break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    Log.i("SimStateListener", "Sim State network locked");
                    break;
                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    Log.i("SimStateListener", "Sim State pin required");
                    break;
                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    Log.i("SimStateListener", "Sim State puk required");
                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    Toast.makeText(context, "Sim State unknown", Toast.LENGTH_SHORT).show();
                    Log.i("SimStateListener", "Sim State unknown");
                    break;
                case TelephonyManager.SIM_STATE_READY:
                    Log.i("SimStateListener", "Sim State ready");
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    String phoneNumber = telephoneMgr.getLine1Number();
                    Log.i("SimStateListener", phoneNumber);
                    if(phoneNumber.equals(number)){
                        Log.i("SimStateListener", "equal");
                        break;
                    }
                    else{
                        Log.i("SimStateListener", "Sim card is changed");
                        notify(context);
                        goToTimerPasscode(context);

                        break;
                    }
            }
        }

//        }
    public String getData(){
        Cursor c = database.getData();
        c.moveToFirst();
        return c.getString(c.getColumnIndex("phone"));
    }
    public void goToTimerPasscode(Context context) {
        Intent i = new Intent(context, TimerPasscode.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    public void notify(  Context  context){
        Intent intent = new Intent(context, TimerPasscode.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("SIM Card change notification")
                .setContentText("The SIM CARD is changed,enter the passcode")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());

    }


}