package com.layla.modules;

import android.content.*;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.layla.*;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class EmergencyData extends AppCompatActivity {

    SharedPreferences myPrefs;
    EditText phoneNumberText, address, medicines, info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_data);

        setToolbar();

        phoneNumberText = findViewById(R.id.editTextContact);
        address = findViewById(R.id.editTextAddress);
        medicines = findViewById(R.id.editTextMedicines);
        info = findViewById(R.id.editTextInfo);

        restoreDataFromServer();

        //restoreData();
    }

    @Override
    protected void onDestroy()
    {
        //saveData();
        saveDataOnServer();
        super.onDestroy();
    }

    public void saveData()
    {
        myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();

        editor.putString("phoneNumberText", phoneNumberText.getText().toString());
        editor.putString("address", address.getText().toString());
        editor.putString("medicines", medicines.getText().toString());
        editor.putString("info", info.getText().toString());

        editor.apply();
    }

    public void setToolbar()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.emergency_data_toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_white_24dp));
        myToolbar.setNavigationOnClickListener(v -> finish());
    }

    public void saveDataOnServer()
    {
        ParseQuery<ParseObject> query = new ParseQuery<>("EmergencyData");

        query.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("recipient", ParseUser.getCurrentUser().getString("doctor"));

        query.findInBackground((objects, e) -> {
            if(e==null)
            {
                if(objects.size()>0)
                {
                    for(ParseObject object : objects)
                    {
                        object.put("phoneNumberText", phoneNumberText.getText().toString());
                        object.put("address", address.getText().toString());
                        object.put("medicines", medicines.getText().toString());
                        object.put("info", info.getText().toString());
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null)
                                {
                                    Toast.makeText(getApplicationContext(), "Successful save!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void restoreDataFromServer()
    {

        ParseQuery<ParseObject> query = new ParseQuery<>("EmergencyData");
        query.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query.whereEqualTo("recipient", ParseUser.getCurrentUser().getString("doctor"));

        query.findInBackground((objects, e) ->
        {
            if(e == null)
            {
                if(objects.size() > 0)
                {
                    for(ParseObject object : objects)
                    {
                        phoneNumberText.setText(object.getString("phoneNumberText"));
                        address.setText(object.getString("address"));
                        medicines.setText(object.getString("medicines"));
                        info.setText(object.getString("info"));
                    }
                }
            }
            else
            {
                Log.e("LAYLA", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void restoreData()
    {
        myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);

        phoneNumberText = findViewById(R.id.editTextContact);
        address = findViewById(R.id.editTextAddress);
        medicines = findViewById(R.id.editTextMedicines);
        info = findViewById(R.id.editTextInfo);

        phoneNumberText.setText(myPrefs.getString("phoneNumberText", ""));
        address.setText(myPrefs.getString("address", ""));
        medicines.setText(myPrefs.getString("medicines", ""));
        info.setText(myPrefs.getString("info",""));
    }

}
