package com.layla.auth;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.layla.*;
import com.mtramin.rxfingerprint.RxFingerprint;
import com.parse.ParseUser;

import io.reactivex.disposables.Disposable;

public class FingerLogin extends AppCompatActivity {

    Button btnLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ifLogged();
        setContentView(R.layout.activity_finger_login);

        progressBar = findViewById(com.layla.R.id.progressBar);
        btnLogin = findViewById(R.id.btn_login);

        buttonsListeners();
        loginUsingFingerprint();

    }

    public void buttonsListeners()
    {
        btnLogin.setOnClickListener(v -> startActivity(new Intent(FingerLogin.this, Login.class)));

    }


    public void ifLogged()
    {
        if(ParseUser.getCurrentUser() != null)
        {
            goToMenu();
        }
    }

    private void loginUsingFingerprint()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(RxFingerprint.isAvailable(this))
            {
                Disposable disposable = RxFingerprint.authenticate(this)
                        .subscribe(fingerprintAuthenticationResult ->
                        {
                            switch (fingerprintAuthenticationResult.getResult())
                            {
                                case FAILED:
                                    Toast.makeText(this, getString(R.string.login_fingerprintUnknown), Toast.LENGTH_SHORT).show();
                                    break;
                                case HELP:
                                    Toast.makeText(this, fingerprintAuthenticationResult.getMessage(), Toast.LENGTH_SHORT).show();
                                    break;
                                case AUTHENTICATED:
                                    ParseUser.logInInBackground(FingerprintHandler.getEmail(), FingerprintHandler.getPassword(), (user, e) ->
                                    {
                                        if(user != null) goToMenu();
                                    });
                            }
                        }, throwable ->
                        {
                            Log.e("LAYLA", "authenticate", throwable);
                        });
            }
        }
    }

    private void goToMenu()
    {
        startActivity(new Intent(FingerLogin.this, GridMenu.class));
        finish();
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
