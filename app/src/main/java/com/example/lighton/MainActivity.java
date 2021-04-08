package com.example.lighton;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textOne, textTwo;
    private Button button;
    DBHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get UI elemnts
        textOne = findViewById(R.id.textView);
        textTwo = findViewById(R.id.textView2);
        button = findViewById(R.id.button);
        databaseHelper = new DBHelper(this);
       // insertData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

    }

    public void insertData(){
        //textOne.getText().toString(); Should be replaced
        String name = "name";
        String email = "namw@gmail.com";
        String passCode = "1234";

        boolean checkInset = databaseHelper.insertUserData(name,email,passCode);
        if(checkInset){
            Toast.makeText(this,"Data saved successfully",Toast.LENGTH_SHORT).show();
            //TODO Navigate
        }else{
            Toast.makeText(this,"Data Cannot be saved!",Toast.LENGTH_SHORT).show();

        }

    }

    public void getData(){
        //Get Cursor from DB
        Cursor res = databaseHelper.getData();
        //Check Row returned
        if(res.getCount()==0){
            Toast.makeText(this,"No entry exist",Toast.LENGTH_SHORT).show();
            return;
        }

        //Extract Data
        String name=null , email=null , passcode = null;
        while(res.moveToNext()){
            name = "Name "+res.getString(0)+"\n" ;
            email = "Email "+res.getString(1)+"\n";
            passcode = "Passcode"+res.getString(2)+"\n";
        }
        textOne.setText(name);
        textTwo.setText(email);
    }
}