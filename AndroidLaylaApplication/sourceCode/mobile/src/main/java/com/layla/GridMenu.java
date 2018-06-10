package com.layla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.layla.doctor.PatientList;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.*;

public class GridMenu extends AppCompatActivity
{
    private List<Button> buttons = new ArrayList<>();
    
    private String supervisor;
    private boolean isSupervisor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        userTypeCheck();

        setContentView(R.layout.activity_grid_menu);
        initButtons();

        setToolbar();
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

    public void userTypeCheck()
    {
        isSupervisor = ParseUser.getCurrentUser().getBoolean("Supervisor");

        if(!isSupervisor) findUsersSupervisor();
    }

    private void findUsersSupervisor()
    {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Supervisor", true);
        query.findInBackground((objects, e) ->
        {
            if(e == null && objects.size() > 0)
            {
                for(ParseUser user : objects)
                {
                    supervisor = user.getUsername();
                }
            }
            else
            {
                Log.e("LAYLA", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void buttonOnClickListeners()
    {
        if(isSupervisor)
        {
            buttons.get(0).setOnClickListener(view ->
            {
                Intent intent = new Intent(getApplicationContext(), PatientList.class);
                intent.putExtra("activity", "com.layla.doctor.PatientSearch");
                startActivity(intent);
            });

            buttons.get(1).setOnClickListener(view ->
            {
                Intent intent = new Intent(getApplicationContext(), PatientList.class);
                intent.putExtra("activity", "com.layla.doctor.NoteReader");
                startActivity(intent);
            });

            buttons.get(2).setOnClickListener(view ->
            {
                Intent intent = new Intent(getApplicationContext(), PatientList.class);
                intent.putExtra("activity", "com.layla.modules.Chat");
                startActivity(intent);
            });

            buttons.get(3).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.layla.settings.GridSettings.class)));

            buttons.get(4).setOnClickListener(view ->
            {
                Intent intent = new Intent(getApplicationContext(), PatientList.class);
                intent.putExtra("activity", "com.layla.modules.EmergencyData");
                startActivity(intent);
            });

            buttons.get(5).setOnClickListener(view ->
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            });

        }
        else
        {
            buttons.get(0).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.layla.modules.PatientGPS.class)));

            buttons.get(1).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.layla.modules.Notebook.class)));

            buttons.get(2).setOnClickListener(view ->
            {
                Intent intent = new Intent(getApplicationContext(), com.layla.modules.ChatHotButtons.class);
                intent.putExtra("username", supervisor);
                startActivity(intent);
            });

            buttons.get(3).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.layla.settings.LoginToSettings.class)));

            buttons.get(4).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), com.layla.modules.EmergencyData.class)));

            buttons.get(5).setOnClickListener(view ->
            {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            });
        }
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.LAYLA));
        setSupportActionBar(myToolbar);
    }
}
