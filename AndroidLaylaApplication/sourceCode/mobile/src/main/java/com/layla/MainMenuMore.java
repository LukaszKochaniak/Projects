package com.layla;

import android.content.*;
import android.os.*;
import android.support.v4.content.*;
import android.support.v7.app.*;

import android.support.v7.widget.Toolbar;
import android.widget.*;

import com.layla.auth.*;
import com.layla.settings.*;

public class MainMenuMore extends AppCompatActivity
{
    private Button btnSignup, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu_more);

        btnSignup = findViewById(R.id.btn_signup);
        btnReset = findViewById(R.id.btn_resetpassword);

        buttonsListeners();
        setToolbar();
    }

    private void buttonsListeners()
    {
        btnReset.setOnClickListener(v -> startActivity(new Intent(MainMenuMore.this, ResetPassword.class)));
        btnSignup.setOnClickListener(v -> startActivity(new Intent(MainMenuMore.this, SignUp.class)));
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.settings));
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> startActivity(new Intent(MainMenuMore.this, MainMenu.class)));
    }
}
