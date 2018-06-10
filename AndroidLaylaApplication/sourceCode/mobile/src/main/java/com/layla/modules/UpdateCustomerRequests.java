package com.layla.modules;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class UpdateCustomerRequests extends IntentService
{
    public UpdateCustomerRequests()
    {
        super("Layla");
    }

    public UpdateCustomerRequests(String name)
    {
        super(name);
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        addLocationListener();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {}

    private void addLocationListener()
    {
        Thread triggerService = new Thread(() ->
        {
            try
            {
                Looper.prepare();//Initialise the current thread as a looper.
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                Criteria c = new Criteria();
                c.setAccuracy(Criteria.ACCURACY_COARSE);

                final String PROVIDER = lm.getBestProvider(c, true);

                LocationListener myLocationListener = new LocationListener()
                {
                    @Override
                    public void onLocationChanged(Location location)
                    {
                        updateLocation(location);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {}

                    @Override
                    public void onProviderEnabled(String s) {}

                    @Override
                    public void onProviderDisabled(String s) {}
                };
                
                if(ActivityCompat.checkSelfPermission(getApplicationContext(),
                 Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                  ActivityCompat.checkSelfPermission(getApplicationContext(), 
                  Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                lm.requestLocationUpdates(PROVIDER, 600000, 0, myLocationListener);
                Log.d("LOC_SERVICE", "Service RUNNING!");
                Looper.loop();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }, "LocationThread");
        triggerService.start();
    }

    public static void updateLocation(Location location)
    {
        Context appCtx = PatientGPS.getAppContext();

        double latitude, longitude;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Intent filterRes = new Intent();
        filterRes.setAction("com.layla.modules.PatientGPS");
        filterRes.putExtra("latitude", latitude);
        filterRes.putExtra("longitude", longitude);
        appCtx.sendBroadcast(filterRes);
    }
}
