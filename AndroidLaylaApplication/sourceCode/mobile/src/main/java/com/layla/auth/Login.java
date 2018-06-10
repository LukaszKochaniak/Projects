package com.layla.auth;

import android.content.*;
import android.net.*;
import android.os.*;
import android.support.v4.content.*;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.*;
import android.view.View;
import android.widget.*;

import com.layla.*;
import com.layla.R;
import com.mtramin.rxfingerprint.*;
import com.parse.ParseUser;

import io.reactivex.disposables.*;

public class Login extends AppCompatActivity
{
    private EditText inputEmailText, inputPasswordText;
    private ProgressBar progressBar;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu_login);
        
        inputEmailText = findViewById(com.layla.R.id.username);
        inputPasswordText = findViewById(com.layla.R.id.password);
        progressBar = findViewById(com.layla.R.id.progressBar);
        btnLogin = findViewById(com.layla.R.id.btn_login);
        
        buttonsListeners();
        loginUsingFingerprint();
        setToolbar();
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

    public void buttonsListeners()
    {
        btnLogin.setOnClickListener(v ->
        {
            String inputEmail = inputEmailText.getText().toString();
            String inputPassword = inputPasswordText.getText().toString();

            if(!isOnline())
            {
                Toast.makeText(getApplicationContext(), getString(R.string.noInternet), Toast.LENGTH_LONG).show();
                return;
            }

            if(TextUtils.isEmpty(inputEmail))
            {
                Toast.makeText(getApplicationContext(), getString(R.string.signUp_enterEmail), Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(inputPassword))
            {
                Toast.makeText(getApplicationContext(), getString(R.string.signUp_enterPassword), Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            ParseUser.logInInBackground(inputEmail, inputPassword, (user, e) ->
            {
                if(user != null)
                {
                    FingerprintHandler.saveData(inputEmail, inputPassword);
                    goToMenu();
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("LAYLA", e.getMessage());
                }
            });
        });
    }

    private void goToMenu()
    {
        startActivity(new Intent(Login.this, GridMenu.class));
        finish();
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.settings));
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> startActivity(new Intent(Login.this, MainMenu.class)));
    }
}
