package com.example.lighton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Userdata.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table UserData(email TEXT, passcode TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Boolean insertUserData(String email,String passcode){
        //Create necessarily var
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Set Content
        contentValues.put("email",email);
        contentValues.put("passcode",passcode);
        //Insert to the table
        long result = db.insert("UserData",null,contentValues);
        //Return if success
        if(result==-1){
            return false;}
        return true;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from UserData",null);
        return cursor;
    }
}
