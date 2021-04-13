package com.example.lighton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.se.omapi.Session;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.util.Log;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;



import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;

import javax.mail.Message;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import android.os.AsyncTask;
import android.os.Bundle;
import android.se.omapi.Session;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import javax.mail;
import androidx.appcompat.app.AppCompatActivity;
import java.net.* ;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.*;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {
    //Initialize varible
    EditText etTo, etSubject, etMessage;
    Button btSend;
    String sEmail, sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        etTo = findViewById(R.id.et_to);
        etSubject = findViewById(R.id.et_to_subject);
        etMessage = findViewById(R.id.et_message);
        btSend = findViewById(R.id.bt_send);

        //Sender email credential
        sEmail = "raghadalfheed76@gmail.com";
       // char[] sPassword = {'p','a','s','s'};
        String sPassword = "Raghadalfheed_7676";


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Properties properties = new Properties();
              properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
               // smtp.googlemail.com
                properties.put("mail.smtp.port",  "587");
            // properties.put("mail.smtp.user", sEmail);
               // properties.put("mail.smtp.socketFactory.fallback", "fallback");
//                properties.put("mail.smtp.EnableSSL.enable", "true");
//                properties.put("mail.smtp.socketFactory.port", "465");
//                properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

                javax.mail.Session session = javax.mail.Session.getInstance(properties, new javax.mail.Authenticator() {
                    @Override
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(sEmail,sPassword);
                    }
                });


                try {
                    //Initialize email content
                    javax.mail.Message message = new MimeMessage(session);

                    //Sender email
                    message.setFrom(new InternetAddress(sEmail));
                    //recipient email
                    message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(etTo.getText().toString().trim()));

                    //Email subject
                    message.setSubject(etSubject.getText().toString().trim());
                    //email message
                    message.setText(etMessage.getText().toString().trim());

                    //send email
                    new SendMail().execute(message);
                }catch (MessagingException e){
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
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
