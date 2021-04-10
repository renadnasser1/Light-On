package com.example.lighton;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Patterns;
        import android.view.View;
        import android.widget.Toast;

        import com.google.android.material.textfield.TextInputLayout;

        import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputName;
    private TextInputLayout textInputPassword;
    private DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database =  new DBHelper(this);
        textInputEmail = findViewById(R.id.text_input_email);
        textInputName = findViewById(R.id.text_input_name);
        textInputPassword = findViewById(R.id.text_input_password);
    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            textInputEmail.setError("Please enter a valid valid email address");
            return false;
        } else
        {
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
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        }else if(!PASSWORD_PATTERN.matcher(passwordInput).matches() ) { //.matches()
            textInputPassword.setError("Weak Password");
            return false;
        }else{
            textInputPassword.setError(null);
            textInputPassword.setErrorEnabled(false);
            return true;
        }
    }

    private String PASSWORD_PATTERN(String passwordInput) {
        return  passwordInput;
    }

    public void confirmInput(View v) {
        if (!validateEmail() | !validateName() | !validatePassword()) {
            return;
        }
        insertData();
//        String input = "Email: " + textInputEmail.getEditText().getText().toString();
//        input += "\n";
//        input += "Name: " + textInputName.getEditText().getText().toString();
//        input += "\n";
//        input += "Password: " + textInputPassword.getEditText().getText().toString();
//        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }

        public void insertData(){
        String name = textInputName.getEditText().getText().toString();
        String email = textInputEmail.getEditText().getText().toString();
        String passCode = textInputPassword.getEditText().getText().toString();

        boolean checkInset = database.insertUserData(name,email,passCode);
        if(checkInset){
            Toast.makeText(this,"Data saved successfully",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this,"Data Cannot be saved!",Toast.LENGTH_SHORT).show();

        }

    }
}