package com.layla.doctor;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.*;

import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.layla.*;

import java.util.ArrayList;
import java.util.List;

public class PatientList extends AppCompatActivity
{
    private String activityToStart;
    private ListView patientListView;
    private List<String> usernames;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        setToolbar();
        getActivityToStart();
        userList();
        patientChoose();
    }

    public void getActivityToStart()
    {
        Intent intent = getIntent();
        activityToStart = intent.getStringExtra("activity");
    }

    public void userList()
    {
        patientListView = findViewById(R.id.patientListView);
        usernames = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(PatientList.this, android.R.layout.simple_list_item_1, usernames);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("Supervisor", false);
        query.whereEqualTo("doctor", ParseUser.getCurrentUser().getUsername().toString());
        query.findInBackground((objects, e) ->
        {
            if(e == null)
            {
                if(objects.size() > 0)
                {
                    for(ParseUser user : objects)
                    {
                        usernames.add(user.getUsername());
                    }
                    patientListView.setAdapter(arrayAdapter);
                }
            }
            else
            {
                Log.e("LAYLA", e.getMessage());
            }
        });
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Patient list");
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    public void patientChoose()
    {
        patientListView.setOnItemClickListener((adapterView, view, i, l) ->
        {
            try
            {
                Class newActivity = Class.forName(activityToStart);
                Intent intent = new Intent(PatientList.this, newActivity);
                intent.putExtra("username", usernames.get(i).trim());
                startActivity(intent);
            }
            catch(ClassNotFoundException e)
            {
                Log.e("LAYLA", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
