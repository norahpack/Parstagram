package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        btnCreate=findViewById(R.id.btnCreate);


        btnCreate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    newUser(username, password);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null){
                            Log.e(TAG, "Issue with login", e);
                            Toast.makeText(SignupActivity.this, "Issue with login!", Toast.LENGTH_SHORT);

                            return;
                        }
                        Intent i = new Intent(SignupActivity.this, FeedActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(SignupActivity.this, "Success!", Toast.LENGTH_SHORT);
                    }
                });
            }
        });

    }

    private void newUser(String username, String password) throws ParseException {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUp();


        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(SignupActivity.this, "Error creating account", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Account Creation was successful!");
                etUsername.setText("");
                etPassword.setText("");
            }
        });
    }

}