package com.example.welcome.test;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class Appointments1 extends AppCompatActivity implements
        View.OnClickListener {
    TextView app_name;
    TextView set_date;
    TextView set_time,confirm;
    Button set_not;
    private int mYear, mMonth, mDay, mHour, mMinute;
    int a1,a2,a3,a4,a5;
    public static String application_name;
    String appendminute,b2,b3,b4,b5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments1);
        app_name = (TextView) findViewById(R.id.app_name1);
        application_name = getIntent().getStringExtra("App_type");
        app_name.setText(application_name);
        set_date = (TextView) findViewById(R.id.setdate);
        set_time = (TextView) findViewById(R.id.settime);
        final Calendar temp=Calendar.getInstance();
        a1=temp.get(Calendar.YEAR);
        a2=temp.get(Calendar.MONTH);
        a3=temp.get(Calendar.DAY_OF_MONTH);
        a4=temp.get(Calendar.HOUR_OF_DAY);
        a5=temp.get(Calendar.MINUTE);
        if(temp.get(Calendar.MINUTE)<=9)
        {
            appendminute="0"+String.valueOf(temp.get(Calendar.MINUTE));
        }
        else
        {
            appendminute=String.valueOf(temp.get(Calendar.MINUTE));
        }
        set_time.setText(temp.get(Calendar.HOUR_OF_DAY)+":"+appendminute);
        set_date.setText(temp.get(Calendar.DAY_OF_MONTH)+"-"+(temp.get(Calendar.MONTH)+1)+"-"+temp.get(Calendar.YEAR));

        set_not = (Button) findViewById(R.id.set_not);
        set_not.setVisibility(View.VISIBLE);
        confirm=(TextView)findViewById(R.id.confirmtext);
        set_date.setOnClickListener(Appointments1.this);
        set_not.setOnClickListener(Appointments1.this);
        set_time.setOnClickListener(Appointments1.this);

    }

    public void onClick(View v) {

        if (v == set_date) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            //dis_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            // schedule.set(mYear,mMonth,mDay);
                            set_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        a1=year;
                        a2=monthOfYear;
                        a3=dayOfMonth;
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else if (v == set_time) {

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            //dis_time.setText(hourOfDay + ":" + minute);
                            a4=hourOfDay;
                            a5=minute;
                            String sminute=String.valueOf(minute);
                            if(a5<=9)
                            {
                                sminute="0"+sminute;
                            }
                            set_time.setText(hourOfDay + ":" + sminute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        else if(v ==set_not)
        {
                //set_not.setVisibility(View.INVISIBLE);
                //Calendar c1=Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                c2.set(Calendar.YEAR, a1);
                c2.set(Calendar.MONTH, a2);
                c2.set(Calendar.DAY_OF_MONTH, a3);
                c2.set(Calendar.HOUR_OF_DAY, a4);
                c2.set(Calendar.MINUTE, a5);
                c2.set(Calendar.SECOND, 0);
                Calendar c1 = Calendar.getInstance();
                long diff = (c2.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
                if(diff>=0) {
                    int id = (int) System.currentTimeMillis();
                    confirm.setText("Reminder set succesfully");
                    //Toast.makeText(this, String.valueOf(diff), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Appointments1.this, Alarm.class);
                    intent.putExtra("Name", application_name);
                    PendingIntent p1 = PendingIntent.getBroadcast(getApplicationContext(), id, intent, 0);
                    AlarmManager a = (AlarmManager) getSystemService(ALARM_SERVICE);
                    a.set(AlarmManager.RTC, System.currentTimeMillis() + diff, p1);
                    if(a2<=9)
                        b2="0"+String.valueOf(a2);
                    else
                        b2=String.valueOf(a2);
                    if(a3<=9)
                        b3="0"+String.valueOf(a3);
                    else
                        b3=String.valueOf(a3);
                    if(a4<=9)
                        b4="0"+String.valueOf(a4);
                    else
                        b4=String.valueOf(a4);
                    if(a5<=9)
                        b5="0"+String.valueOf(a5);
                    else
                        b5=String.valueOf(a5);
                    String value_to_be_set_lv=application_name+"\nTime:"+b4+b5+"\nDate:"+a1+b2+b3+"```id:"+id;
                    if(Appointments.temp_array.size()==0)
                        Appointments.temp_array.add("LIST OF APPOINTMENTS");
                    Appointments.temp_array.add(value_to_be_set_lv);
                    //Appointments.aa.notifyDataSetChanged();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String user=mAuth.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(user).child("Appointments");
                    HashMap<String ,String> hm = new HashMap<>();
                    for(int loop=0;loop<Appointments.temp_array.size();loop++)
                        hm.put(String.valueOf(loop),Appointments.temp_array.get(loop));
                    myRef.setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Toast.makeText(Appointments1.this,"Running",Toast.LENGTH_SHORT).show();
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Appointments1.this,"Successful",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Appointments1.this,"Not Successful",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    Intent back=new Intent(this,Appointments.class);
                    back.putExtra("APPID",String.valueOf(id));
                    back.putExtra("App_type",application_name);
                    startActivity(back);
                }
                else
                {
                    Toast.makeText(this,"Select Future date",Toast.LENGTH_LONG).show();
                }

                /*
            dis_date.setText(c2.get(Calendar.YEAR)+"  "+c2.get(Calendar.MONTH)+" "+c2.get(Calendar.DAY_OF_MONTH) +" "+c2.get(Calendar.HOUR_OF_DAY)+" "+c2.get(Calendar.MINUTE));
            n.setSmallIcon(R.drawable.ic_announcement_black_24dp);
            long diff=(c2.getTimeInMillis()-Calendar.getInstance().getTimeInMillis());
            n.setTicker("Sample app");
            dis_time.setText(String.valueOf(diff));
            n.setWhen(System.currentTimeMillis() + diff);
            n.setContentTitle("APP");
            n.setContentText("this is a sample app");
            Intent i=new Intent(Appointments1.this,Appointments1.class);
            PendingIntent p;
            p = PendingIntent.getActivity(getApplicationContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            n.setContentIntent(p);
            NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nm.notify(id,n.build());
            public void cancelAlarm(View view) {
		    if (manager != null) {
			    manager.cancel(pendingIntent);
	            Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
		        }
		      }
            */
            }
        }
}




