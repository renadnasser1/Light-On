package com.example.lighton;

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

//import javax.mail;

public class MainActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        etTo = findViewById(R.id.et_to);
        etSubject = findViewById(R.id.et_to_subject);
        etMessage = findViewById(R.id.et_message);
        btSend = findViewById(R.id.bt_send);

        //Assign location variable
        btLocation = findViewById(R.id.bt_location);
        textView1 = findViewById(R.id.text_view1);
        textView2 = findViewById(R.id.text_view2);
        textView3 = findViewById(R.id.text_view3);
        textView4 = findViewById(R.id.text_view4);
        textView5 = findViewById(R.id.text_view5);
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
        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Areej tag", "statrt on click :" + toString());
//
//                //check premeisson
//                if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    //when premission graned
//                    getLocation();
////                    sendEmail();
//                } else {
//                    //when permission denied
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//                }
            }
        });











        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Areej tag", "statrt on click :" + toString());

                //check premeisson
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //when premission graned
                    getLocation();
//                    sendEmail();
                } else {
                    //when permission denied
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
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
        });
    }


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
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
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
    }





    private class SendMail extends AsyncTask<javax.mail.Message,String,String> {

        //initalize progress par
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                    progressDialog= ProgressDialog.show(MainActivity.this, "Please Wait", "Sending Mail...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //dismiss progress dialog
                progressDialog.dismiss();
                if (s.equals("success")){
                    //when success

                    //initialize alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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


}
