package com.layla.settings;

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

import com.parse.ParseUser;
import com.layla.*;

public class ChangePassword extends AppCompatActivity
{
    private EditText password;
    private EditText repeatPassword;
    private Button changeButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setToolbar();

        password = findViewById(R.id.oldPassword);
        repeatPassword = findViewById(R.id.newPassword);
        changeButton = findViewById(R.id.changeButton);
        progressBar = findViewById(R.id.progressBar);

        buttonListener();
    }

    private void buttonListener()
    {
        changeButton.setOnClickListener(view ->
        {
            String newPassword = repeatPassword.getText().toString();
            String repeatedNewPassword = password.getText().toString();

            if(TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(repeatedNewPassword))
            {
                Toast.makeText(getApplicationContext(), getString(R.string.changePassword_enterPassword), Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            if(newPassword.equals(repeatedNewPassword))
            {
                ParseUser user = ParseUser.getCurrentUser();
                user.setPassword(repeatPassword.getText().toString());
                user.saveInBackground(e ->
                {
                    if(e == null)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Log.e("LAYLA", e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                finish();

                progressBar.setVisibility(View.INVISIBLE);
            }
            else
            {
                Toast.makeText(getApplicationContext(), getString(R.string.changePassword_passwordsDifferent), Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.changePassword));
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> finish());
    }
}
