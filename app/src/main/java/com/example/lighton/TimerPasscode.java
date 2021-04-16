package com.example.lighton;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;
import com.google.android.material.textfield.TextInputLayout;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class TimerPasscode extends AppCompatActivity {
    private TextView countdownText;
    private TextInputLayout digitOne, digitTwo,digitThree,digitFour;
    private EditText pOne, pTwo, pThree, pFour;
    private String passcode;
    private Button verifyButton;
    private DBHelper database;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds = 60000; // 60 sec == 1 min
    private boolean isTimerRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_passcode);

        setupElements();
        startStop();
        setupFingerprint();
        moveToNextField(pOne, pTwo);
        moveToNextField(pTwo, pThree);
        moveToNextField(pThree, pFour);

    }

    public void setupElements(){
        countdownText = findViewById(R.id.countdown_text);
        database =  new DBHelper(this);
        verifyButton = findViewById(R.id.verifyButton);
        digitOne = findViewById(R.id.passcode1);
        digitTwo = findViewById(R.id.passcode2);
        digitThree = findViewById(R.id.passcode3);
        digitFour = findViewById(R.id.passcode4);
        pOne = findViewById(R.id.p1_editText);
        pTwo = findViewById(R.id.p2_editText);
        pThree = findViewById(R.id.p3_editText);
        pFour = findViewById(R.id.p4_editText);
    }

    private void setupFingerprint(){
        //for fingerprint
        Reprint.initialize(this);

        Reprint.authenticate(new AuthenticationListener() {
            public void onSuccess(int moduleTag) {
                showSuccess();
            }

            public void onFailure(AuthenticationFailureReason failureReason, boolean fatal,
                                  CharSequence errorMessage, int moduleTag, int errorCode) {
                showError(errorMessage,"FINGERPRINT ERROR");
            }
        });
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
            if ( 0 == seconds){
                verifyButton.setEnabled(false);
                Intent intent = new Intent(TimerPasscode.this, WrongPasscode.class);
                startActivity(intent);
                finish();

                //TODO: SEND AN EMAIL WITH CURRENT LOCATION

            }
        }
    }

    private boolean validatePassword() {
        String one = digitOne.getEditText().getText().toString().trim();
        String two = digitTwo.getEditText().getText().toString().trim();
        String three = digitThree.getEditText().getText().toString().trim();
        String four = digitFour.getEditText().getText().toString().trim();
        passcode = one + two + three + four;

        TextInputLayout textViews[] = {digitOne,digitTwo,digitThree,digitFour};

        Boolean flag = true;
        for(int i=0;i<4;i++){
            String ch = textViews[i].getEditText().getText().toString().trim();
            if (ch.isEmpty()){
                textViews[i].setError("Missing");

                flag = false;
            }else{
                textViews[i].setError(null);
                textViews[i].setErrorEnabled(false);
            }
        }
        return flag;
    }

    public void verfiyPasscode(View v){
        if (!validatePassword()) {
            return;
        }

        String pass = getData("passcode");
        if (pass.equalsIgnoreCase(passcode)){
            stopTimer();
            showSuccess();
        } else {
            showError("Please enter your passcode correctly","PASSCODE ERROR");
        }

    }

    public String getData(String column){
       Cursor c = database.getData();
       c.moveToFirst();
       return c.getString(c.getColumnIndex(column));
    }

    private void moveToNextField(EditText field, EditText nextField){
        field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(field.getText().toString().trim().length() == 1){
                    nextField.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void showError(CharSequence errorMessage, String title) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText((String)errorMessage)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void showSuccess() {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("You are save now!")
                .setContentText("‎The passcode you entered matches our records. Have a good day 🌷")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent intent = new Intent(TimerPasscode.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }


}