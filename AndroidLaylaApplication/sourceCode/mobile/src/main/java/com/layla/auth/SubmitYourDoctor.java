package com.layla.auth;

import android.content.Intent;
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
import com.layla.settings.HotButtonsFill;
import com.parse.*;

public class SubmitYourDoctor extends AppCompatActivity
{
    private Button submitButton;
    private ProgressBar progressBar;
    private EditText newDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_your_doctor);

        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);
        newDoctor = findViewById(R.id.doctorName);

        setToolbar();

        buttonListener();
    }

    private void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.submitDoctor_submit));
        setSupportActionBar(myToolbar);
    }

    private void buttonListener()
    {
        submitButton.setOnClickListener(view ->
        {
            String doctorName = newDoctor.getText().toString();
            
            if(TextUtils.isEmpty(doctorName))
            {
                Toast.makeText(getApplicationContext(), getString(R.string.submitDoctor_enterName), Toast.LENGTH_SHORT).show();
                return;
            }
    
            if(!doesDoctorExist(doctorName)) return;

            progressBar.setVisibility(View.VISIBLE);
            
            ParseUser.getCurrentUser().put("doctor", doctorName);
            ParseUser.getCurrentUser().saveInBackground(e ->
            {
                if(e == null)
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SubmitYourDoctor.this, HotButtonsFill.class));
                }
                else
                {
                    Toast.makeText(SubmitYourDoctor.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("LAYLA", e.getMessage());
                }
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
