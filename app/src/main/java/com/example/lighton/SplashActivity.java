package com.example.lighton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (isRegistered()) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, RegisterActivity.class);
                }

                startActivity(intent);

                finish();
            }},4000);
    }

    public boolean isRegistered(){
        DBHelper db = new DBHelper(this);
        if(db.isEmpty()) {
            return false;
        }else{
            return  true;
        }

    }
}