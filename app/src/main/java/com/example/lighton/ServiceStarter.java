package com.example.lighton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SimChangedReceiver", "--> SIM state changed <--");
        Intent i = new Intent("com.prac.test.MyPersistingService");

        i.setClass(context, MainActivity.class);
        context.startService(i);

    }


}
