package com.layla.settings;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.layla.GridMenu;
import com.layla.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class HotButtonsFill extends AppCompatActivity {

    EditText parentNumber, supervisorsNumber, doctorNumber;
    Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_buttons_fill);

        setToolbar();
        parentNumber = findViewById(R.id.parentNumber);
        supervisorsNumber = findViewById(R.id.supervisorNumber);
        doctorNumber = findViewById(R.id.doctorNumber);
        submitButton = findViewById(R.id.submitButton);

        onClickListener();

    }

    public void onClickListener()
    {
        submitButton.setOnClickListener(view -> {
            saveDataOnServer();
            startActivity(new Intent(HotButtonsFill.this, GridMenu.class));
        });
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.fill_hot_buttons);
        setSupportActionBar(myToolbar);
    }

    public void saveDataOnServer()
    {
        final ParseObject message = new ParseObject("EmergencyData");

        message.put("sender", ParseUser.getCurrentUser().getUsername());
        message.put("recipient", ParseUser.getCurrentUser().getString("doctor"));
        message.put("parentNumber", parentNumber.getText().toString());
        message.put("supervisorsNumber", supervisorsNumber.getText().toString());
        message.put("doctorNumber", doctorNumber.getText().toString());

        message.saveInBackground(e ->
        {
            if(e!=null)
            {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
