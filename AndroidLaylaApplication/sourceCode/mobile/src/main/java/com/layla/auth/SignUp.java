package com.layla.auth;

import android.content.*;
import android.net.*;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.*;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseUser;
import com.layla.*;

public class SignUp extends AppCompatActivity
{
    private EditText inputEmail, inputPassword, inputUsername;
    private Button btnSignIn, btnSignUp;
    private ProgressBar progressBar;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);
        inputEmail = findViewById(R.id.email);
        inputUsername = findViewById(R.id.username);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        checkBox = findViewById(R.id.checkBox);

        btnSignIn.setOnClickListener(v -> startActivity(new Intent(SignUp.this, Login.class)));

        btnSignUp.setOnClickListener(v ->
        {
            final String email = inputEmail.getText().toString().trim();
            final String username = inputUsername.getText().toString().trim();
            final String password = inputPassword.getText().toString().trim();

            if(!isOnline())
            {
                Toast.makeText(getApplicationContext(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(getApplicationContext(), getString(R.string.signUp_enterEmail), Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(username))
            {
                Toast.makeText(getApplicationContext(), getString(R.string.signUp_enterUsername), Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(password))
            {
                Toast.makeText(getApplicationContext(), getString(R.string.signUp_enterPassword), Toast.LENGTH_SHORT).show();
                return;
            }
            if(password.length() < 6)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.signUp_passwordShort), Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            final ParseUser user = new ParseUser();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);

            user.signUpInBackground(e ->
            {
                if(e != null)
                {
                    Log.e("LAYLA", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE); 
                }
                else 
                {
                    FingerprintHandler.saveData(email, password);
                    user.put("Supervisor", checkBox.isChecked());
                    user.saveInBackground();
                    startActivity(new Intent(SignUp.this, checkBox.isChecked() ? GridMenu.class : SubmitYourDoctor.class));

                    finish();
                }
            });
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}