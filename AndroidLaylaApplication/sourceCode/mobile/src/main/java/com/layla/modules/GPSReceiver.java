package com.layla.modules;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseUser;

public class GPSReceiver extends BroadcastReceiver
{

    double latitude, longitude;

    @Override
    public void onReceive(final Context context, final Intent calledIntent)
    {
        Log.d("LOC_RECEIVER", "Location RECEIVED!");

        latitude = calledIntent.getDoubleExtra("latitude", -1);
        longitude = calledIntent.getDoubleExtra("longitude", -1);

        updateRemote(latitude, longitude);

    }

    private void updateRemote(final double latitude, final double longitude )
    {
        ParseUser.getCurrentUser().put("latitude", latitude);
        ParseUser.getCurrentUser().put("longitude", longitude);
        ParseUser.getCurrentUser().saveInBackground();
    }
}