package com.layla;

import android.content.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.widget.*;

import com.layla.auth.*;
import com.mtramin.rxfingerprint.*;
import com.parse.ParseUser;

import io.reactivex.disposables.*;

public class MainMenu extends AppCompatActivity
{
    private Button btnLogin, btnMore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.layla.R.layout.activity_mainmenu);

        ifLogged();
        
        btnLogin = findViewById(com.layla.R.id.btn_login);
        btnMore = findViewById(R.id.btn_more_options);

        buttonsListeners();
        loginUsingFingerprint();
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

    public void ifLogged()
    {
        if(ParseUser.getCurrentUser() != null)
        {
            goToMenu();
        }
    }

    public void buttonsListeners()
    {
        btnMore.setOnClickListener(v -> startActivity(new Intent(MainMenu.this, MainMenuMore.class)));
        btnLogin.setOnClickListener(v -> startActivity(new Intent(MainMenu.this, Login.class)));
    }

    private void goToMenu()
    {
        startActivity(new Intent(MainMenu.this, GridMenu.class));
        finish();
    }
}
