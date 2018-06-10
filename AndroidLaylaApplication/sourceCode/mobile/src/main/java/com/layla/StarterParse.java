package com.layla;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

public class StarterParse extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("ed27f8b32bcae4823258606d1f059228db9839dd")
                .clientKey("07bed1deda6b12c4ea3b51e9a722eeeaf8d4d414")
                .server("http://35.159.11.157:80/parse/")
                .build()
        );

        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("");

        //ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
