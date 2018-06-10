package com.layla.settings;

import android.content.*;
import android.net.*;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.*;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.layla.auth.*;
import com.parse.ParseUser;
import com.layla.*;

public class LoginToSettings extends AppCompatActivity
{
    private EditText passwordText;
    private Button button;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_open);

        setToolbar();

        passwordText = findViewById(R.id.oldPassword);
        button = findViewById(R.id.changeButton);
        progressBar = findViewById(R.id.progressBar);

        buttonListener();
    }

    private void buttonListener()
    {
        button.setOnClickListener(v ->
        {
            String inputPassword = passwordText.getText().toString();
            
            if(!isOnline())
            {
                Toast.makeText(getApplicationContext(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
                return;
            }

            if(TextUtils.isEmpty(inputPassword))
            {
                Toast.makeText(getApplicationContext(), getString(R.string.signUp_enterPassword), Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            ParseUser.logInInBackground(FingerprintHandler.getEmail(), inputPassword, (user, e) ->
            {
                if(user != null)
                {
                    startActivity(new Intent(getApplicationContext(), com.layla.settings.GridSettings.class));
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.e("LAYLA", e.getMessage());
                }
            });
        });
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.settings_login));
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
