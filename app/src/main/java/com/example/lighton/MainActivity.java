package com.example.lighton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.BoringLayout;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import static android.widget.Toast.*;

public class MainActivity extends AppCompatActivity {
        DBHelper db ;
    private BroadcastReceiver receiver;
    private Object Context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db =  new DBHelper(this);
        CheckBox simpleCheckBox = (CheckBox) findViewById(R.id.CheckBox);
        setUSBcheck(simpleCheckBox);


        simpleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                    updateUSBCheck(isChecked);
            }
                }
        );


//        TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
//       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//
//           return;
//       }

        ReciverReigster();


    }
    public void ReciverReigster(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(receiver, filter);
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(receiver, filter2);
    }
    public void setUSBcheck(CheckBox checkBox){
        int state =  getUSBcheck();
        if (state ==1){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }
    }
    public int getUSBcheck(){

      DBHelper database;
        database =  new DBHelper(this);
        Cursor c = database.getData();
        c.moveToFirst();
        return c.getInt(c.getColumnIndex("usbCheck"));
    }
    public void updateUSBCheck(Boolean checkBoxState) {
        DBHelper database;
        database =  new DBHelper(this);

        if (checkBoxState) {
            if (database.update(1)) {
                makeText(this, "update", LENGTH_SHORT).show();

            } else {
                makeText(this, "erorro", LENGTH_SHORT).show();
            }
        } else {
            if (database.update(0)) {
                makeText(this, "update", LENGTH_SHORT).show();

            } else {
                makeText(this, "erorro", LENGTH_SHORT).show();
            }
        }
    }

    public void deleteData(View v) {
        db.delete();
        finish();
    }

    public void goToTimerPasscode(){

        Intent intent = new Intent(this, TimerPasscode.class);
        startActivity(intent);
    }
}