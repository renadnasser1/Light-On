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


//        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//            Toast.makeText(context, "boot", Toast.LENGTH_SHORT).show();
//            Log.d("XXXX", "BOOT");
//            Intent i = new Intent(context, MainActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);



//        } else {
            database =  new DBHelper(context);
            String number = getData();
            Toast.makeText(context,number,Toast.LENGTH_SHORT);
            // Checks Sim card State
            TelephonyManager telephoneMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int simState = telephoneMgr.getSimState();

            switch (simState) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    Toast.makeText(context, "Sim State absent", Toast.LENGTH_SHORT).show();
                    Log.i("SimStateListener", "Sim State absent");
                    break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    Toast.makeText(context, "Sim State network locked", Toast.LENGTH_SHORT).show();
                    Log.i("SimStateListener", "Sim State network locked");
                    break;
                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    Toast.makeText(context, "Sim State pin required", Toast.LENGTH_SHORT).show();

                    Log.i("SimStateListener", "Sim State pin required");
                    break;
                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    Toast.makeText(context, "Sim State puk required", Toast.LENGTH_SHORT).show();
                    Log.i("SimStateListener", "Sim State puk required");
                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN:
                    Toast.makeText(context, "Sim State unknown", Toast.LENGTH_SHORT).show();
                    Log.i("SimStateListener", "Sim State unknown");
                    break;
                case TelephonyManager.SIM_STATE_READY:
                    Toast.makeText(context, "Sim State ready", Toast.LENGTH_SHORT).show();


                    Log.i("SimStateListener", "Sim State ready");
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    String phoneNumber = telephoneMgr.getLine1Number();
                    Log.i("SimStateListener", phoneNumber);
                    Toast.makeText(context, phoneNumber, Toast.LENGTH_LONG).show();
                    if(phoneNumber.equals(number)){
                        Toast.makeText(context, "Equal", Toast.LENGTH_LONG).show();
                        break;
                    }
                    else{
                        Log.i("SimStateListener", "Sim card is changed");
                        Toast.makeText(context, "notification", Toast.LENGTH_LONG).show();
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