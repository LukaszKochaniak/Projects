package com.layla.doctor;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.*;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.layla.*;

public class PatientSearch extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private String activeUsername;
    private Intent intent;
    private Button refresh; //only for view argument

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        saveFromServer();

        setContentView(R.layout.activity_patient_search);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK) finish();

        return super.onKeyDown(keyCode, event);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        refreshLocation(refresh);
        addMarker();
    }

    public void refreshLocation(View view)
    {
        saveFromServer();
        addMarker();
    }

    public void saveFromServer()
    {
        intent = getIntent();
        activeUsername = intent.getStringExtra("username").trim();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereMatches("username", activeUsername);
        query.setLimit(1);
        query.findInBackground((objects, e) ->
        {
            if(e == null)
            {
                for(ParseObject user : objects)
                {
                    longitude = user.getDouble("longitude");
                    latitude = user.getDouble("latitude");
                }
            }
            else
            {
                Log.e("LAYLA", e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
        SystemClock.sleep(200);
    }

    public void addMarker()
    {
        mMap.clear(); //delete marker
        LatLng user = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(user).title("Marker on " + activeUsername));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 15));
    }
}
