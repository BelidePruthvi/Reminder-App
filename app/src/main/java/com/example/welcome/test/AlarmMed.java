package com.example.welcome.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmMed extends BroadcastReceiver {
    NotificationCompat.Builder n;
    int id=(int)System.currentTimeMillis();
    String value="Medicine";
    public void onReceive(Context context, Intent intent) {
        n=new NotificationCompat.Builder(context);
        Bundle extras=intent.getExtras();
        if (extras != null) {
            value = extras.getString("MedicationName");
            //The key argument here must match that used in the other activity
        }
        n.setAutoCancel(true);
        n.setSmallIcon(R.drawable.ic_healing_black_24dp);
        n.setTicker("Medication");
        n.setWhen(System.currentTimeMillis());
        Toast.makeText(context,"Running",Toast.LENGTH_SHORT).show();
        n.setContentTitle(value);
        n.setContentText("Take your Medicine");
        n.setPriority(Notification.PRIORITY_MAX);
        Intent i=new Intent(context,Homepage.class);
        PendingIntent p;
        p = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        n.setContentIntent(p);
        NotificationManager nm=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(id,n.build());

    }
}
