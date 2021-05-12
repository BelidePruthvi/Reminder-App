package com.example.welcome.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Alarm extends BroadcastReceiver {
    NotificationCompat.Builder n;
    int id=(int)System.currentTimeMillis();
    String value="default";
    int index;
    public void onReceive(final Context context, Intent intent) {
       /* MediaPlayer mp=MediaPlayer.create(context,R.raw.sample);
        Toast.makeText(context,"Wake up",Toast.LENGTH_LONG).show();
        mp.start();*/
        n=new NotificationCompat.Builder(context);
        Bundle extras=intent.getExtras();
        if (extras != null) {
            value = extras.getString("Name");
            index=extras.getInt("index");
        }
        n.setAutoCancel(true);
        n.setSmallIcon(R.drawable.ic_bookmark_black_24dp);
        //long diff=(c2.getTimeInMillis()-Calendar.getInstance().getTimeInMillis());
        n.setTicker("Appointment");
        //dis_time.setText(String.valueOf(diff));
        n.setWhen(System.currentTimeMillis());
        //Toast.makeText(context,Appointments1.application_name,Toast.LENGTH_SHORT).show();
        n.setContentTitle(value);
        n.setContentText("Time for your Appointment");
        Intent i=new Intent(context,Appointments1.class);
        PendingIntent p;
        p = PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        n.setContentIntent(p);
        n.setPriority(Notification.PRIORITY_MAX);
        NotificationManager nm=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(id,n.build());
        /*
        Appointments.temp_array.remove(index);
        if(Appointments.temp_array.size()==1)
            Appointments.temp_array.remove(0);
        //Appointments.aa.notifyDataSetChanged();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Appointment");
        HashMap<String ,String> hm = new HashMap<>();
        for(int loop=0;loop<Appointments.temp_array.size();loop++)
            hm.put(String.valueOf(loop),Appointments.temp_array.get(loop));
        myRef.setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context,"Running",Toast.LENGTH_SHORT).show();
                if(task.isSuccessful())
                {
                    Toast.makeText(context,"Successful",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context,"Not Successful",Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }
}
