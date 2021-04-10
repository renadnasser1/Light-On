package com.example.lighton;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TimerPasscode extends AppCompatActivity {
    private TextView countdownText;
    private Button verfiyButton;
    private EditText passcodeEditText;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds = 60000; // 60 sec == 1 min
    private boolean isTimerRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_passcode);

        setupElements();

        startStop();

    }

    public void setupElements(){
        countdownText = findViewById(R.id.countdown_text);
        verfiyButton = findViewById(R.id.moveTo);
        passcodeEditText = findViewById(R.id.passcode_editText);
    }

    public void startStop(){
        if(isTimerRunning){
            startTimer();
        } else {
            stopTimer();
        }
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000){ // 1 sec
            @Override
            public void onTick(long l) {
                timeLeftInMilliSeconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start(); //starting the timer

        isTimerRunning = true;
    }

    // use it when user enters the passcode
    public void stopTimer(){
        countDownTimer.cancel();
        isTimerRunning = false;
    }

    public void updateTimer(){
        int seconds = (int) timeLeftInMilliSeconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "00"; // min
        timeLeftText += ":";
        if(seconds < 10) timeLeftText += "0"; // 09, 08, ...etc
        timeLeftText += seconds;

        countdownText.setText(timeLeftText);

        // to change countdownText color when reaching some points
        if ( 60> seconds ){
            countdownText.setTextColor(Color.parseColor("#A0BC9E"));
            if ( 30> seconds ){
                countdownText.setTextColor(Color.parseColor("#e8c843"));
            }
            if ( 10> seconds ){
                countdownText.setTextColor(Color.parseColor("#DF7861"));
            }
        }
    }


    public void verfiyPasscode(){
        String passcode;

        passcode = passcodeEditText.getText().toString();

    }
}