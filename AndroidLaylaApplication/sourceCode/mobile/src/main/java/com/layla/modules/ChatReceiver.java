package com.layla.modules;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;
import com.layla.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatReceiver extends ParsePushBroadcastReceiver
{
    @Override
    protected NotificationCompat.Builder getNotification(Context context, Intent intent)
    {
        return super.getNotification(context, intent);
    }

    @Override
    public void onPushOpen(Context context, Intent intent)
    {
        Intent i = new Intent(context, Chat.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    protected void onPushReceive(Context context, Intent intent)
    {
        //here You can handle push before appearing into status e.g if you want to stop it.
        super.onPushReceive(context, intent);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.e("LAYLA", json.getString("alert").toString());

            final String notificationTitle = json.getString("title").toString();
            final String notificationContent = json.getString("alert").toString();
            final String uri = json.getString("uri");

            Intent resultIntent = null;
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);


            resultIntent = new Intent(context, Chat.class);
            stackBuilder.addParentStack(Chat.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            //Customize your notification - sample code
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle(notificationTitle)
                            .setContentText(notificationContent);

            int mNotificationId = 001;
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, builder.build());


        }
        catch(JSONException e)
        {
            Log.e("LAYLA", e.getMessage());
        }
    }

}
