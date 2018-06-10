package com.layla.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.layla.auth.*;
import com.parse.ParseUser;
import com.layla.*;

public class ResetPassword extends AppCompatActivity
{
    private EditText inputEmail;
    private ProgressBar progressBar;
    private Button btnReset;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = findViewById(R.id.email);
        progressBar = findViewById(R.id.progressBar);
        btnReset = findViewById(R.id.reset_button);
        btnLogin = findViewById(R.id.btn_login);

        buttonsListeners();
    }

    public void buttonsListeners()
    {
        btnReset.setOnClickListener(v ->
        {
            String email = inputEmail.getText().toString();
            if(TextUtils.isEmpty(email))
            {
                Toast.makeText(getApplicationContext(), getString(R.string.resetPassword_email), Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            Log.i("java", email.trim());

            ParseUser.requestPasswordResetInBackground(email, e ->
            {
                if(e == null)
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ResetPassword.this, Login.class));
                    finish();
                }
                else
                {
                    Log.e("LAYLA", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        btnLogin.setOnClickListener(v -> startActivity(new Intent(ResetPassword.this, Login.class))
        );
    }

}
