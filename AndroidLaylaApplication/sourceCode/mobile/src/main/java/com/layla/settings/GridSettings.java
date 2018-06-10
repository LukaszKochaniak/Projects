package com.layla.settings;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.layla.*;
import com.parse.ParseUser;
import com.layla.auth.Login;
import com.layla.auth.SignUp;

import java.util.*;

public class GridSettings extends AppCompatActivity
{
    private List<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_settings);

        setToolbar();
        initButtons();
    }

    public void buttonOnClickListeners()
    {
        buttons.get(0).setOnClickListener(view -> logOut());
        buttons.get(1).setOnClickListener(view -> changeDoctor());
        buttons.get(2).setOnClickListener(view -> changePassword());
        buttons.get(3).setOnClickListener(view -> deleteAccount());
        buttons.get(4).setOnClickListener(view -> startActivity(new Intent(GridSettings.this, HotButtonsFill.class)));
    }

    private void initButtons()
    {
        buttons.add(findViewById(R.id.button1));
        buttons.add(findViewById(R.id.button2));
        buttons.add(findViewById(R.id.button3));
        buttons.add(findViewById(R.id.button4));
        buttons.add(findViewById(R.id.button5));
        buttons.add(findViewById(R.id.button6));

        buttonOnClickListeners();
    }
    
    public void logOut()
    {
        ParseUser.logOutInBackground(e ->
        {
            if(e == null)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.logout_success), Toast.LENGTH_LONG).show();
            }
            else
            {
                Log.e("LAYLA", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        
        startActivity(new Intent(GridSettings.this, MainMenu.class));
    }

    public void deleteAccount()
    {
        new AlertDialog.Builder(GridSettings.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.confirm))
                .setMessage(getString(R.string.deleteAccount_message))
                .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> ParseUser.getCurrentUser().deleteInBackground(e ->
                {
                    if (e == null)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.deleteAccount_deleteSuccessful), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(GridSettings.this, SignUp.class));
                        finish();
                    }
                    else
                    {
                        Log.e("LAYLA", e.getMessage());
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }))
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    public void changeDoctor()
    {
        startActivity(new Intent(GridSettings.this, ChangeDoctor.class));
    }

    public void changePassword()
    {
        startActivity(new Intent(GridSettings.this, ChangePassword.class));
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.settings));
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> startActivity(new Intent(GridSettings.this, GridMenu.class)));
    }

}
