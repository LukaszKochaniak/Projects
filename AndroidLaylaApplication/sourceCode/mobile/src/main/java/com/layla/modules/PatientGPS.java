package com.layla.modules;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.*;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;
import com.layla.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.location.LocationProvider.AVAILABLE;

public class PatientGPS extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastKnownLocation;
    private Button refreshButton;
    private static Context context;

    private static final int GPS_REQUEST_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == GPS_REQUEST_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 5, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patientgps);
        refreshButton = findViewById(R.id.button);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        refreshButton.setOnClickListener(view ->
        {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                
            }

            updateMarker();
            showAddress();
        });
    }

    public static Context getAppContext()
    {
        return PatientGPS.context;
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
        this.googleMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                lastKnownLocation = location;
                saveLocationToServer();
                updateMarker();
            }

            @Override
            public void onStatusChanged(String privder, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String s)
            {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }

            @Override
            public void onProviderDisabled(String s) {}
        };

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 5, locationListener);
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void saveLocationToServer()
    {
        if(lastKnownLocation != null)
        {
            ParseUser.getCurrentUser().put("latitude", lastKnownLocation.getLatitude());
            ParseUser.getCurrentUser().put("longitude", lastKnownLocation.getLongitude());
            ParseUser.getCurrentUser().saveInBackground();
        }
    }

    private void updateMarker()
    {
        if(lastKnownLocation == null) return;
        final float zoom = 15.0f;
        googleMap.clear();

        LatLng user = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

        googleMap.addMarker(new MarkerOptions().position(user).title("Marker on user")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, zoom));
    }

    private void showAddress()
    {
        Address address = getLastLocationAddress();
        String addressText = getTextFromAddress(address);

        Toast.makeText(getApplicationContext(), addressText, Toast.LENGTH_LONG).show();
    }

    private Address getLastLocationAddress()
    {
        if(lastKnownLocation == null) return null;
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try
        {
            List<Address> addressList = geocoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);

            if(addressList != null && !addressList.isEmpty())
            {
                return addressList.get(0);
            }
        }
        catch(IOException e)
        {
            Log.e("LAYLA", e.getMessage());
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return null;
    }

    private String getTextFromAddress(Address address)
    {
        if(address == null) return getString(R.string.unknown_address);

        return address.getCountryName() + " " + address.getLocality();
    }
}
