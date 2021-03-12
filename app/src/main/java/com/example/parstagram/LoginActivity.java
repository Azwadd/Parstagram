package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button logIn;
    private Button signIn;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }
        // sets up some fading colors to brighten up dull login page //
        ConstraintLayout constraintLayout = findViewById(R.id.loginMenu);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(7000);
        animationDrawable.start();

        // defines all items on activity //
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        signIn = findViewById(R.id.signIn);
        signIn.setText("signIn");
        signUp = findViewById(R.id.signUp);
        signUp.setText("signUp");
        logIn = findViewById(R.id.logIn);
        logIn.setText("login");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn.setText("login");
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn.setText("signUp");
                if (ParseUser.getCurrentUser() != null) {
                    ParseUser.getCurrentUser().logOut();
                }
            }
        });

        // Once clicked we will verify the user's inputs //
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logIn.getText().equals("login")) {
                    Log.i(TAG, "onClick logIn");
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    loginUser(username, password);
                } else if (logIn.getText().equals("signUp")) {
                    Log.i(TAG, "onClick signUp");
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();
                    signUpUser(username, password);
                }
            }
        });

    }

    private void signUpUser(String username, String password) {
        Log.i(TAG, "Attempting to signup user");
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        // Set custom properties
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with signup", e);
                    Toast.makeText(getApplicationContext(), "Error with loginaz", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    goMainActivity();
                }
            }
        });
    }

    /** Check user credentials and displays data */
    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user");
        // Check credentials then open the next activity //
       ParseUser.logInInBackground(username, password, new LogInCallback() {
           @Override
           public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    return;
                } else { goMainActivity(); }
           }
       });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}