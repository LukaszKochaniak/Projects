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

import com.layla.R;
import com.parse.*;

public class ChangeDoctor extends AppCompatActivity
{
    private EditText newDoctor;
    private Button changeButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_doctor);

        setToolbar();

        newDoctor = findViewById(R.id.newDoctor);
        changeButton = findViewById(R.id.changeButton);
        progressBar = findViewById(R.id.progressBar);

        buttonListener();
    }

    private void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.changeDoctor));
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    private void buttonListener()
    {
        changeButton.setOnClickListener(view ->
        {
            if(TextUtils.isEmpty(newDoctor.getText().toString()))
            {
                Toast.makeText(getApplicationContext(), getString(R.string.changeDoctor_previousDoctor), Toast.LENGTH_SHORT).show();
                return;
            }

            if(!doesDoctorExist(newDoctor.getText().toString())) return;

            progressBar.setVisibility(View.VISIBLE);

            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.whereMatches("username", ParseUser.getCurrentUser().getUsername().toString());
            userQuery.findInBackground((objects, e) ->
            {
                if(e == null)
                {
                    if(objects.size() == 1)
                    {
                        for(ParseUser user : objects)
                        {
                            user.put("doctor", newDoctor.getText().toString());
                            user.saveInBackground();
                        }

                        Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                {
                    Log.e("LAYLA", e.getMessage());
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

                progressBar.setVisibility(View.INVISIBLE);
            });
        });
    }
    
    private boolean doesDoctorExist(String doctorName)
    {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Supervisor", true);
        query.whereEqualTo("username", doctorName);

        try
        {
            if(query.count() == 0)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.submitDoctor_noSuchDoctor), Toast.LENGTH_SHORT).show();
                return false;
            }
            else return true;
        }
        catch(ParseException e)
        {
            Log.e("LAYLA", e.getMessage());
            return false;
        }
    }
}
