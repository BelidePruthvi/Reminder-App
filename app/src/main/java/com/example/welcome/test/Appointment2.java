package com.example.welcome.test;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import java.util.StringTokenizer;

public class Appointment2 extends AppCompatActivity implements
        View.OnClickListener {
    TextView app_name;
    TextView set_date;
    TextView set_time;
    Button edit_not, cancel_not;
    private int mYear, mMonth, mDay, mHour, mMinute;
    int a1, a2, a3, a4, a5, id, index;
    public static String application_name;
    String details,details2,details1;
    StringTokenizer st1;
    int hours,year,minutes,month,day_of_month;
    String b2,b3,b4,b5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment2);
        app_name = (TextView) findViewById(R.id.app_name1);
        set_date = (TextView) findViewById(R.id.setdate);
        set_time = (TextView) findViewById(R.id.settime);
        edit_not = (Button) findViewById(R.id.edit_not);
        cancel_not = (Button) findViewById(R.id.cancel_not);
        set_date.setOnClickListener(Appointment2.this);
        set_time.setOnClickListener(Appointment2.this);
        edit_not.setOnClickListener(Appointment2.this);
        cancel_not.setOnClickListener(Appointment2.this);
        Intent curr = getIntent();
        Bundle extras = curr.getExtras();
        if (extras != null) {
            details = extras.getString("Details");
            index = extras.getInt("index");

        }
        StringTokenizer st = new StringTokenizer(details, "```");
        if (st.hasMoreElements())
            details2 = String.valueOf(st.nextElement());
        st1=new StringTokenizer(details2,"\n");
        if(st.hasMoreElements())
            app_name.setText(String.valueOf(st1.nextElement()));
        if(st.hasMoreElements())
        {
            String var=String.valueOf(st1.nextElement());
            var=var.substring(5);
            hours = Integer.parseInt(var.substring(0, 2));
            minutes = Integer.parseInt(var.substring(2));
        }
        if(st.hasMoreElements())
        {
            String var=String.valueOf(st1.nextElement());
            var=var.substring(5);
            year = Integer.parseInt(var.substring(0, 4));
            month = Integer.parseInt(var.substring(4, 6));
            day_of_month = Integer.parseInt(var.substring(6));
        }
        a1=year;
        a2=month;
        a3=day_of_month;
        a4=hours;
        a5=minutes;
        if (st.hasMoreElements()) {
            details1 = String.valueOf(st.nextElement());
            details1=details1.substring(3);
            id=Integer.parseInt(details1);
        }
        set_time.setText(hours+":"+minutes);
        set_date.setText(day_of_month+"-"+month+"-"+year);

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
                            a1 = year;
                            a2 = monthOfYear;
                            a3 = dayOfMonth;
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
                            a4 = hourOfDay;
                            a5 = minute;
                            String sminute = String.valueOf(minute);
                            if (a5 <= 9) {
                                sminute = "0" + sminute;
                            }
                            set_time.setText(hourOfDay + ":" + sminute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        } else if (v == edit_not) {
            //edit_not.setVisibility(View.INVISIBLE);
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
            if (diff >= 0) {
                //final int id = (int) System.currentTimeMillis();
                //confirm.setText("Reminder set succesfully");
                //Toast.makeText(this, String.valueOf(diff), Toast.LENGTH_LONG).show();
                /*Intent cur = getIntent();
                Bundle extras = cur.getExtras();
                if (extras != null) {
                    id = Integer.parseInt(extras.getString("APPID"));
                    index = extras.getInt("rem_details");
                    //The key argument here must match that used in the other activity
                }*/

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
                application_name=app_name.getText().toString();
                String value_to_be_set_lv=application_name+"\nTime:"+b4+b5+"\nDate:"+a1+b2+b3+"```id:"+id;
                Appointments.temp_array.set(index,value_to_be_set_lv);
                //Appointments.temp_array.notifyDataSetChanged();
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
                            Toast.makeText(Appointment2.this,"Successful",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Appointment2.this,"Not Successful",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent intent = new Intent(Appointment2.this, Alarm.class);
                intent.putExtra("Name", application_name);
                PendingIntent p1 = PendingIntent.getBroadcast(getApplicationContext(), id, intent, 0);
                AlarmManager a = (AlarmManager) getSystemService(ALARM_SERVICE);
                a.cancel(p1);
                a.set(AlarmManager.RTC, System.currentTimeMillis() + diff, p1);
                Intent back = new Intent(this, Appointments.class);
                startActivity(back);
            } else {
                Toast.makeText(this, "Select Future date", Toast.LENGTH_LONG).show();
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
        } else if (v == cancel_not) {
            Calendar c2 = Calendar.getInstance();
            c2.set(Calendar.YEAR, a1);
            c2.set(Calendar.MONTH, a2);
            c2.set(Calendar.DAY_OF_MONTH, a3);
            c2.set(Calendar.HOUR_OF_DAY, a4);
            c2.set(Calendar.MINUTE, a5);
            c2.set(Calendar.SECOND, 0);
            Calendar c1 = Calendar.getInstance();
            long diff = (c2.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
            if (diff >= 0) {
                //final int id = (int) System.currentTimeMillis();
                //confirm.setText("Reminder set succesfully");
                //Toast.makeText(this, String.valueOf(diff), Toast.LENGTH_LONG).show();
                /*Intent cur = getIntent();
                Bundle extras = cur.getExtras();
                if (extras != null) {
                    id = Integer.parseInt(extras.getString("APPID"));
                    index = extras.getInt("rem_details");
                    //The key argument here must match that used in the other activity
                }*/
                Appointments.temp_array.remove(index);
                if(Appointments.temp_array.size()==1)
                    Appointments.temp_array.remove(0);
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
                        //Toast.makeText(.this,"Running",Toast.LENGTH_SHORT).show();
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Appointment2.this,"Successful",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Appointment2.this,"Not Successful",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Intent intent = new Intent(Appointment2.this, Alarm.class);
                intent.putExtra("Name", application_name);
                intent.putExtra("index",index);
                PendingIntent p1 = PendingIntent.getBroadcast(getApplicationContext(), id, intent, 0);
                AlarmManager a = (AlarmManager) getSystemService(ALARM_SERVICE);
                a.cancel(p1);
                Intent back = new Intent(this, Appointments.class);
                startActivity(back);
            } else {
                Toast.makeText(this, "Select Future date", Toast.LENGTH_LONG).show();
            }

        }
        }
}


