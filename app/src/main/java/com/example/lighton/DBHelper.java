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
        db.execSQL("create Table UserData(name TEXT,email TEXT, passcode TEXT, phone TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public Boolean insertUserData(String name,String email,String passcode,String phone ){
        //Create necessarily var
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Set Content
        contentValues.put("name",name);
        contentValues.put("email",email);
        contentValues.put("passcode",passcode);
        contentValues.put("phone",phone);


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

    public boolean isEmpty(){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("Select * from UserData", null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getInt(0) == 0) {
                    return false;
                }
            } else {
                return true;
            }
        }catch (Exception e){
            return true;
        }
        return false;

    }

    public void delete(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ "UserData");
    }
}


//    public void insertData(){
//        //textOne.getText().toString(); Should be replaced
//        String name = "name";
//        String email = "namw@gmail.com";
//        String passCode = "1234";
//        String phone = "0555"
//
//        boolean checkInset = databaseHelper.insertUserData(name,email,passCode,phone);
//        if(checkInset){
//            Toast.makeText(this,"Data saved successfully",Toast.LENGTH_SHORT).show();
//            //TODO Navigate
//        }else{
//            Toast.makeText(this,"Data Cannot be saved!",Toast.LENGTH_SHORT).show();
//
//        }
//
//    }
//
//    public void getData(){
//        //Get Cursor from DB
//        Cursor res = databaseHelper.getData();
//        //Check Row returned
//        if(res.getCount()==0){
//            Toast.makeText(this,"No entry exist",Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        //Extract Data
//        String name=null , email=null , passcode=null , phone= null;
//        while(res.moveToNext()){
//            name = "Name "+res.getString(0)+"\n" ;
//            email = "Email "+res.getString(1)+"\n";
//            passcode = "Passcode"+res.getString(2)+"\n";
//             phone = "phone"+res.getString(3)+"\n";
//        }
//        textOne.setText(name);
//        textTwo.setText(email);
//    }