package com.example.lighton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +           //no white spaces
                    ".{4,}");

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputName;
    private TextInputLayout digitOne, digitTwo, digitThree, digitFour;
    private EditText pOne, pTwo, pThree, pFour;
    private String passcode;
    private DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = new DBHelper(this);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputName = findViewById(R.id.text_input_name);
        digitOne = findViewById(R.id.passcode_1);
        digitTwo = findViewById(R.id.passcode_2);
        digitThree = findViewById(R.id.passcode_3);
        digitFour = findViewById(R.id.passcode_4);
        pOne = findViewById(R.id.p1);
        pTwo = findViewById(R.id.p2);
        pThree = findViewById(R.id.p3);
        pFour = findViewById(R.id.p4);

        moveToNextField(pOne, pTwo);
        moveToNextField(pTwo, pThree);
        moveToNextField(pThree, pFour);
    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid valid email address");
            return false;
        } else {
            textInputEmail.setError(null);
            textInputEmail.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateName() {
        String nameInput = textInputName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            textInputName.setError("Field can't be empty");
            return false;
        } else {
            textInputName.setError(null);
            textInputName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String one = digitOne.getEditText().getText().toString().trim();
        String two = digitTwo.getEditText().getText().toString().trim();
        String three = digitThree.getEditText().getText().toString().trim();
        String four = digitFour.getEditText().getText().toString().trim();
        passcode = one + two + three + four;

        TextInputLayout textViews[] = {digitOne, digitTwo, digitThree, digitFour};

        Boolean flag = true;
        for (int i = 0; i < 4; i++) {
            String ch = textViews[i].getEditText().getText().toString().trim();
            if (ch.isEmpty()) {
                textViews[i].setError("Missing");
                flag = false;
            } else {
                textViews[i].setError(null);
                textViews[i].setErrorEnabled(false);
            }
        }
        return flag;
    }

    private String PASSWORD_PATTERN(String passwordInput) {
        return passwordInput;
    }

    public void confirmInput(View v) {
        System.out.println("Confirm called");
        if (!validateEmail() | !validateName() | !validatePassword()) {
            return;
        }
        insertData();
        String input = "Email: " + textInputEmail.getEditText().getText().toString();
        input += "\n";
        input += "Name: " + textInputName.getEditText().getText().toString();
        input += "\n";
        input += "Password: " + passcode;
//        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }

    public void insertData() {
        String name = textInputName.getEditText().getText().toString();
        String email = textInputEmail.getEditText().getText().toString();
        String passCode = passcode;
        TelephonyManager telephoneMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            Toast.makeText(this,"inside if",Toast.LENGTH_SHORT).show();

            return;

        }
        String phoneNumber = telephoneMgr.getLine1Number();
        Log.d("phoneR",phoneNumber);
//        Toast.makeText(this,phoneNumber,Toast.LENGTH_SHORT).show();

        boolean checkInset = database.insertUserData(name,email,passCode,phoneNumber);
        if(checkInset){
//            Toast.makeText(this,"Data saved successfully",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this,"Data Cannot be saved!",Toast.LENGTH_SHORT).show();

        }

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
}