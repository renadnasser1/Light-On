package com.example.lighton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

    }

    public void deleteData(View view) {
        db.delete();
        finish();
    }

    public void goToTimerPasscode(View v){

        Intent intent = new Intent(this, TimerPasscode.class);
        startActivity(intent);
    }
}