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
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



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
// email and location varibles
//Initialize varible
EditText etTo, etSubject, etMessage;
    Button btSend;
    String sEmail, sPassword;
    String locationCountry;
    String locationCity;
    String locationAddress;
    double locationLatitude;
    double locationLongitude;
    Properties properties = new Properties();
    //location varibles
    Button btLocation;
    TextView textView1, textView2, textView3, textView4, textView5;
    FusedLocationProviderClient fusedLocationProviderClient;
    //end of email and location varibles
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

        //For email and location
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        // smtp.googlemail.com
        properties.put("mail.smtp.port", "587");
        //Sender email credential
        sEmail = "LightOnApplication@gmail.com";
        // char[] sPassword = {'p','a','s','s'};
        sPassword = "AAaa@@22";
        //initalize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //End for email and location
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
                Log.d("Areej tag", "statrt on click :" + toString());

                //check premeisson
                if (ActivityCompat.checkSelfPermission(TimerPasscode.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when premission graned
                    getLocation();
//                    sendEmail();
                } else {
                    //when permission denied
                    ActivityCompat.requestPermissions(TimerPasscode.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
//                Properties properties = new Properties();
//                properties.put("mail.smtp.auth", "true");
//                properties.put("mail.smtp.starttls.enable", "true");
//                properties.put("mail.smtp.host", "smtp.gmail.com");
//                // smtp.googlemail.com
//                properties.put("mail.smtp.port", "587");
                // properties.put("mail.smtp.user", sEmail);
                // properties.put("mail.smtp.socketFactory.fallback", "fallback");
//                properties.put("mail.smtp.EnableSSL.enable", "true");
//                properties.put("mail.smtp.socketFactory.port", "465");
//                properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

//                javax.mail.Session session = javax.mail.Session.getInstance(properties, new javax.mail.Authenticator() {
//                    @Override
//                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//                        return new javax.mail.PasswordAuthentication(sEmail, sPassword);
//                    }
//                });


                try {
//                    //Initialize email content
//                    javax.mail.Message message = new MimeMessage(session);
//
//                    //Sender email
//                    message.setFrom(new InternetAddress(sEmail));
//                    //recipient email
//                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("etwaa69@gmail.com"));
//
//                    //Email subject
//                    message.setSubject("Warning! Is your mobile lost?");
//                    //email message
//
//
//                    message.setText("Hello dear\nWe think your phone is lost!\nthis email is sent from Light On application " +
//                            "to provide you with your mobile location \n"+"latitude:"+locationLatitude+ ","+"\n"
//                           +" longitude:" +locationLongitude+" ,"+"\n" +" Country:"+ locationCountry+","+"\n"
//                            +" City:"+locationCity+","+"\n" +" Address:"+locationAddress+"\n\n"+"Thank you!");

                    //send email
//                    new SendMail().execute(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }


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
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //initalize location
                Log.d("Areej tag", "statrt on cmplete :" + toString());

                Location location = task.getResult();
                if(location == null){
                    Log.d("Areej tag", "اللوكيشن  نل :" + toString());

                }
                if(location != null){
                    Log.d("Areej tag", "اللوكيشن مب نل :" + toString());

                    try {
                        Log.d("Areej tag", "دخلت التراي :" + toString());

                        // inilize geoCoder
                        Geocoder geocoder = new Geocoder(TimerPasscode.this, Locale.getDefault());
                        Log.d("Areej tag", "سويت اوبجكت جي كودر :" + toString());

                        //inilize address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(), 1
                        );
                        Log.d("Areej tag", "سويت اللست :" + toString());

                        //set latitude on TextView
                        textView1.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude :</b><br></font>"
                                + addresses.get(0).getLatitude()));

                        locationLatitude=addresses.get(0).getLatitude();
                        textView2.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Longitude :</b><br></font>"
                                        + addresses.get(0).getLongitude()
                        ));
                        locationLongitude=addresses.get(0).getLongitude();
                        //Set country name
                        textView3.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Country Name :</b><br></font>"
                                        + addresses.get(0).getCountryName()
                        ));
                        locationCountry=addresses.get(0).getCountryName();
                        //Set locality
                        textView4.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Locality :</b><br></font>"
                                        + addresses.get(0).getLocality()
                        ));
                        locationCity=addresses.get(0).getLocality();
                        //set address
                        textView5.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Address :</b><br></font>"
                                        + addresses.get(0).getAddressLine(0)
                        ));
                        locationAddress=addresses.get(0).getAddressLine(0);
                        Log.d("Areej tag", "Yaaaaaaaay :" + toString());
                        sendEmail();
                    } catch (IOException e) {
                        Log.d("Areej tag", "ERRRROR raghaaaaaaaad :" + toString());

                        e.printStackTrace();
                    }


                }

            }
        });
    }//end get location method

    public void sendEmail(){
//        Properties properties = new Properties();
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.host", "smtp.gmail.com");
//        // smtp.googlemail.com
//        properties.put("mail.smtp.port", "587");
        // properties.put("mail.smtp.user", sEmail);
        // properties.put("mail.smtp.socketFactory.fallback", "fallback");
//                properties.put("mail.smtp.EnableSSL.enable", "true");
//                properties.put("mail.smtp.socketFactory.port", "465");
//                properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        javax.mail.Session session = javax.mail.Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(sEmail, sPassword);
            }
        });


        try {
            //Initialize email content
            javax.mail.Message message = new MimeMessage(session);

            //Sender email
            message.setFrom(new InternetAddress(sEmail));
            //recipient email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("etwaa69@gmail.com"));

            //Email subject
            message.setSubject("Warning! Is your mobile lost?");
            //email message


            message.setText("Hello dear\nWe think your phone is lost!\nthis email is sent from Light On application " +
                    "to provide you with your mobile location \n"+"latitude:"+locationLatitude+ ","+"\n"
                    +" longitude:" +locationLongitude+" ,"+"\n" +" Country:"+ locationCountry+","+"\n"
                    +" City:"+locationCity+","+"\n" +" Address:"+locationAddress+"\n\n"+"Thank you!");

            //send email
            new SendMail().execute(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//end send email method


    private class SendMail extends AsyncTask<javax.mail.Message,String,String> {

        //initalize progress par
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= ProgressDialog.show(TimerPasscode.this, "Please Wait", "Sending Mail...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //dismiss progress dialog
            progressDialog.dismiss();
            if (s.equals("success")){
                //when success

                //initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(TimerPasscode.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#509324'>Success</font>"));
                builder.setMessage("Mail send successfully.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //clear all edit text
                        etTo.setText("");
                        etSubject.setText("");
                        etMessage.setText("");
                    }
                });
                //show alert dialog
                builder.show();

            }else {
                //when error
                Toast.makeText(getApplicationContext(), "Something went worng ?", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(javax.mail.Message... messages) {
            try{
                Log.d("in Try", "ERRRROR 0000000 :");
                Transport.send(messages[0]);
                return "success";
            }
            catch (Exception ex){
                Log.d("Areej tag", "ERRRROR 12 :" + ex.toString());
                return "error";
            }
        }
    }



}//end class