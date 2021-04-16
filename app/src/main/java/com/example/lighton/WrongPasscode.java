package com.example.lighton;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class WrongPasscode extends AppCompatActivity {
    TextView errorTitle;
    private DBHelper database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_passcode);

        setupElements();
        setErrorText();
    }

    public void setupElements() {
        errorTitle = findViewById(R.id.error_title);
        database = new DBHelper(this);
    }

    public void setErrorText(){

        String name = getData("name");
        errorTitle.setText("This is " + name + "'s phone.");
    }
    public String getData(String column){
        Cursor c = database.getData();
        c.moveToFirst();
        return c.getString(c.getColumnIndex(column));
    }
}