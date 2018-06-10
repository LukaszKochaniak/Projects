package com.layla.modules;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import com.layla.R;
import com.layla.doctor.PatientList;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ChatHotButtons extends AppCompatActivity {

    private Button buttonOne, buttonTwo, buttonThree, buttonFour;
    private String secondUser, parentNumber, supervisorsNumber, doctorNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retainNumbers();

        getUsername();

        setContentView(R.layout.activity_chat_hot_buttons);

        setToolbar();

        buttonOne = findViewById(R.id.button1);
        buttonTwo = findViewById(R.id.button2);
        buttonThree = findViewById(R.id.button3);
        buttonFour = findViewById(R.id.button4);

        buttonOnClickListeners();

    }

    public void getUsername()
    {
        secondUser = getIntent().getStringExtra("username");
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.chat_hot_buttons));
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    public void retainNumbers()
    {
        ParseQuery<ParseObject> query = new ParseQuery<>("EmergencyData");

        query.findInBackground((objects, e) -> {
            if(e == null)
            {
                if (objects.size() > 0)
                {
                    for (ParseObject object : objects)
                    {
                        parentNumber = "tel:" + object.getString("parentNumber");
                        supervisorsNumber = "tel:" + object.getString("supervisorsNumber");
                        doctorNumber = "tel:" + object.getString("doctorNumber");
                    }
                }
            }
        });
    }

    public void buttonOnClickListeners()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
        {
            buttonOne.setOnClickListener(view ->
            {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(parentNumber));
                startActivity(callIntent);
            });

            buttonTwo.setOnClickListener(view ->
            {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(supervisorsNumber));
                startActivity(callIntent);
            });

            buttonThree.setOnClickListener(view ->
            {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(doctorNumber));
                startActivity(callIntent);
            });

            buttonFour.setOnClickListener(view -> {
                Intent intent = new Intent(ChatHotButtons.this, Chat.class);
                intent.putExtra("username", secondUser);
                startActivity(intent);
            });
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

}
