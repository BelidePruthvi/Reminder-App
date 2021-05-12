package com.example.welcome.test;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class MedicationEdit extends AppCompatActivity {

    TextView dosage,frequency,time,remaining,notify,date,save;
    EditText med_name;
    int a1,a2,a3,a4,a5;
    String timeperiod="daily";
    String appendminute,medicine_name="Medicine";
    int no_of_pills=1;
    int pills_remain=10;
    int notify_when=5;
    int old_id=-1;
    int index=-1;
    int old_remid=-1;
    int year,month,day_of_month,hours,minutes;
    int send_dosage=1,send_frequency=1,send_remaining=3,send_notify=1;
    final String[] frequencyitems=new String[]{"every hour","daily","once in two days","once in three days","once in a week"};
    final String[] dosageitems=new String[]{"0.5 Pill(s)","1 Pill(s)","2 Pill(s)","5 Pill(s)"};
    final String[] remitems=new String[]{"2","3","5","10","20","30","40","50"};
    final String[] notitems=new String[]{"2 pill(s)","5 pill(s)","10 pill(s)","20 pill(s)"};
    String b2,b3,b4,b5;
    String details,details1,var;
    String details2;
    StringTokenizer st1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_edit);
        dosage=(TextView)findViewById(R.id.dosage);
        frequency=(TextView)findViewById(R.id.frequency);
        time=(TextView)findViewById(R.id.time);
        save=(TextView)findViewById(R.id.textTrack);
        med_name=(EditText)findViewById(R.id.med_name);
        final Calendar temp=Calendar.getInstance();
        a1=temp.get(Calendar.YEAR);
        a2=temp.get(Calendar.MONTH);
        a3=temp.get(Calendar.DAY_OF_MONTH);
        a4=temp.get(Calendar.HOUR_OF_DAY);
        a5=temp.get(Calendar.MINUTE);
        Intent curr=getIntent();
        Bundle extras = curr.getExtras();
        if (extras != null) {
            details=extras.getString("Details");
            index = extras.getInt("index");

        }
        StringTokenizer st=new StringTokenizer(details,"```");
        if(st.hasMoreElements())
            details2=String.valueOf(st.nextElement());
        if(st.hasMoreElements()) {
            details1 = String.valueOf(st.nextElement());
            //Toast.makeText(MedicationEdit.this,details1,Toast.LENGTH_SHORT).show();
            st1 = new StringTokenizer(details1, "\n");
        if(st1.hasMoreElements()) {
            var = String.valueOf(st1.nextElement());
            old_id = Integer.parseInt(var.substring(3));
        }
        if(st1.hasMoreElements()) {
            var = String.valueOf(st1.nextElement());
            old_remid = Integer.parseInt(var.substring(7));
        }
        if(st1.hasMoreElements()) {
            var = String.valueOf(st1.nextElement());
            medicine_name = var.substring(9);
        }
        if(st1.hasMoreElements()) {
            var = String.valueOf(st1.nextElement());
            send_dosage = Integer.parseInt(var.substring(7));
        }if(st1.hasMoreElements()) {
                var = String.valueOf(st1.nextElement());
                send_frequency = Integer.parseInt(var.substring(10));
            }if(st1.hasMoreElements()) {
                var = String.valueOf(st1.nextElement());
                send_notify = Integer.parseInt(var.substring(7));
            }if(st1.hasMoreElements()) {
                var = String.valueOf(st1.nextElement());
                send_remaining = Integer.parseInt(var.substring(10));
            }if(st1.hasMoreElements()) {
                var = String.valueOf(st1.nextElement());
                var = var.substring(5);
                year = Integer.parseInt(var.substring(0, 4));
                month = Integer.parseInt(var.substring(4, 6));
                day_of_month = Integer.parseInt(var.substring(6));
            }if(st1.hasMoreElements()) {
                var = String.valueOf(st1.nextElement());
                var = var.substring(5);
                hours = Integer.parseInt(var.substring(0, 2));
                minutes = Integer.parseInt(var.substring(2));
            }
        }
        med_name.setText(medicine_name);
        time.setText(hours+":"+minutes);
        remaining=(TextView)findViewById(R.id.remaining);
        notify=(TextView)findViewById(R.id.textnotify);
        date=(TextView)findViewById(R.id.date);
        date.setText(day_of_month+"-"+month+"-"+year);
        dosage.setText(dosageitems[send_dosage]);
        no_of_pills=CalculatePills(dosageitems[send_dosage]);
        notify.setText("Notify me when only"+"\n"+notitems[send_notify]+" are left");
        notify_when=CalculateNotifyWhen(notitems[send_notify]);
        frequency.setText(frequencyitems[send_frequency]);
        timeperiod=frequencyitems[send_frequency];
        remaining.setText(remitems[send_remaining]);
        pills_remain=Integer.parseInt(remitems[send_remaining]);
        a5=minutes;
        a4=hours;
        a1=year;
        a2=month;
        a3=day_of_month;
        frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder al_fre=new AlertDialog.Builder(MedicationEdit.this);
                al_fre.setTitle("Select frequency");
                al_fre.setSingleChoiceItems(frequencyitems,send_frequency, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        frequency.setText(frequencyitems[i]);
                        timeperiod=frequencyitems[i];
                        dialog.dismiss();
                        send_frequency=i;
                        //timeperiod=frequencyitems[send_frequency];
                    }
                });
                al_fre.setNeutralButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });
                AlertDialog ad_fre=al_fre.create();
                ad_fre.show();
            }
        });
        dosage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder al_dos=new AlertDialog.Builder(MedicationEdit.this);
                al_dos.setTitle("Select dosage");
                al_dos.setSingleChoiceItems(dosageitems,send_dosage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dosage.setText(dosageitems[i]);
                        dialog.dismiss();
                        no_of_pills=CalculatePills(dosageitems[i]);
                        send_dosage=i;
                        //no_of_pills=CalculatePills(dosageitems[send_dosage]);
                    }
                });
                al_dos.setNeutralButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });
                AlertDialog ad_dos=al_dos.create();
                ad_dos.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                //int mHour = c.get(Calendar.HOUR_OF_DAY);
                //int mMinute = c.get(Calendar.MINUTE);
                int mHour=hours;
                int mMinute=minutes;
                TimePickerDialog timePickerDialog = new TimePickerDialog(MedicationEdit.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                //dis_time.setText(hourOfDay + ":" + minute);
                                String sminute=String.valueOf(minute);
                                if(minute<=9)
                                {
                                    sminute="0"+sminute;
                                }
                                time.setText(hourOfDay + ":" + sminute);
                                a4=hourOfDay;
                                a5=minute;

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        remaining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder al_rem=new AlertDialog.Builder(MedicationEdit.this);
                al_rem.setTitle("Select remaining tablets");
                al_rem.setSingleChoiceItems(remitems,send_remaining, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        remaining.setText(remitems[i]);
                        dialog.dismiss();
                        send_remaining=i;
                        pills_remain=Integer.parseInt(remitems[i]);
                        //pills_remain=Integer.parseInt(remitems[send_remaining]);

                    }
                });
                al_rem.setNeutralButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });
                AlertDialog ad_rem=al_rem.create();
                ad_rem.show();
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder al_not=new AlertDialog.Builder(MedicationEdit.this);
                al_not.setTitle("Notify when");
                al_not.setSingleChoiceItems(notitems,send_notify, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        notify.setText("Notify me when only"+"\n"+notitems[i]+" are left");
                        dialog.dismiss();
                        send_notify=i;
                        notify_when=CalculateNotifyWhen(notitems[i]);
                        //notify_when=CalculateNotifyWhen(notitems[send_notify]);
                    }
                });
                al_not.setNeutralButton("cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });
                AlertDialog ad_not=al_not.create();
                ad_not.show();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear,mMonth,mDay;
                /*mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                */
                mYear=year;
                mMonth=month;
                mDay=day_of_month;

                DatePickerDialog datePickerDialog = new DatePickerDialog(MedicationEdit.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                a1=year;
                                a2=monthOfYear;
                                a3=dayOfMonth;
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }
    public void editFunction(View v)
    {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR, a1);
        c.set(Calendar.MONTH, a2);
        c.set(Calendar.DAY_OF_MONTH, a3);
        c.set(Calendar.HOUR_OF_DAY,a4);
        c.set(Calendar.MINUTE,a5);
        c.set(Calendar.SECOND,0);
        long diff = (c.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
        if(diff>=0) {
            int num=CalculateTimep();
            Intent curr=getIntent();
            Bundle extras = curr.getExtras();
            if (extras != null) {
                index = extras.getInt("index");
            }
            String medicine_name;
            if(!med_name.getText().toString().matches("")) {
                medicine_name = med_name.getText().toString();
            }
            else
            {
                //Toast.makeText(MedicationEdit.this,"Running",Toast.LENGTH_SHORT).show();
                medicine_name="Medication";
            }
            long calculate_period=pills_remain-notify_when-1;
            calculate_period*=num;
            if(no_of_pills==-1)
            {
                calculate_period*=2;
            }
            else {
                calculate_period /= no_of_pills;
            }
            Intent intent = new Intent(MedicationEdit.this, AlarmMed.class);
            intent.putExtra("MedicationName",medicine_name);
            PendingIntent p1 = PendingIntent.getBroadcast(getApplicationContext(), old_id, intent, 0);
            AlarmManager a = (AlarmManager) getSystemService(ALARM_SERVICE);
            a.cancel(p1);
            a.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + diff,num*1000, p1);
            // Toast.makeText(MedicationEdit.this,String.valueOf(calculate_period)+"::"+String.valueOf(diff)+":::"+String.valueOf(num),Toast.LENGTH_SHORT).show();
            Intent intent1=new Intent(MedicationEdit.this,AlarmRem.class);
            intent1.putExtra("MedicationName",medicine_name);
            PendingIntent p2=PendingIntent.getBroadcast(getApplicationContext(),old_remid,intent1,0);
            AlarmManager a_rem=(AlarmManager)getSystemService(ALARM_SERVICE);
            a_rem.cancel(p2);
            a_rem.set(AlarmManager.RTC,System.currentTimeMillis()+diff+calculate_period*1000,p2);
            String temp=medicine_name+"\nFrequency:"+frequency.getText()+"\nTime:"+a4+":"+a5+"```"+"id:"+old_id+"\nrem_id:"+old_remid+"\nMed_name:"+medicine_name;
            temp+="\nDosage:"+send_dosage+"\nFrequency:"+send_frequency+"\nNotify:"+send_notify+"\nRemaining:"+send_remaining;
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
            temp+="\nDate:"+a1+b2+b3;
            temp+="\nTime:"+b4+b5;
           // Toast.makeText(MedicationEdit.this,temp,Toast.LENGTH_SHORT).show();
            /*Homepage.medicine_array.set(index,temp);
            Homepage.medicine_adapter.notifyDataSetChanged();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Medication");
            HashMap<String ,String> hm = new HashMap<>();
            for(int loop=0;loop<Homepage.medicine_array.size();loop++)
                hm.put(String.valueOf(loop),Homepage.medicine_array.get(loop));
           */
            Homepage.temp_array.set(index,temp);
            //Toast.makeText(MedicationEdit.this,"Size is "+Homepage.temp_array.size(),Toast.LENGTH_SHORT).show();
            //Homepage.medicine_adapter.notifyDataSetChanged();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String user=mAuth.getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(user).child("Medication");
            HashMap<String ,String> hm = new HashMap<>();
            for(int loop=0;loop<Homepage.temp_array.size();loop++)
                hm.put(String.valueOf(loop),Homepage.temp_array.get(loop));
            myRef.setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MedicationEdit.this,"Running",Toast.LENGTH_SHORT).show();
                    if(task.isSuccessful())
                    {
                        Toast.makeText(MedicationEdit.this,"Successful",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MedicationEdit.this,"Not Successful",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Intent home=new Intent(MedicationEdit.this,Homepage.class);
            /*home.putExtra("MEDID",String.valueOf(old_id));
            home.putExtra("REMID",String.valueOf(old_remid));
            home.putExtra("medication_name",medicine_name);
            home.putExtra("year",a1);
            home.putExtra("month",a2);
            home.putExtra("day_of_month",a3);
            home.putExtra("hour_of_day",a4);
            home.putExtra("minute",a5);
            home.putExtra("send_dosage",send_dosage);
            home.putExtra("send_notify",send_notify);
            home.putExtra("send_frequency",send_frequency);
            home.putExtra("send_remaining",send_remaining);
            */
            startActivity(home);
        }
        else
        {
            Toast.makeText(this,"Select Future date",Toast.LENGTH_LONG).show();
        }
    }

    public void cancelFunction(View v)
    {
        Intent curr=getIntent();
        Bundle extras = curr.getExtras();
        if (extras != null) {
            //old_id = Integer.parseInt(extras.getString("MEDID"));
            index = extras.getInt("index");
            //old_remid=Integer.parseInt(extras.getString("REMID"));
            //medicine_name=extras.getString("MedicationName");
            //The key argument here must match that used in the other activity
        }
        Intent intent = new Intent(MedicationEdit.this, AlarmMed.class);
        intent.putExtra("MedicationName",medicine_name);
        PendingIntent p1 = PendingIntent.getBroadcast(getApplicationContext(), old_id, intent, 0);
        AlarmManager a = (AlarmManager) getSystemService(ALARM_SERVICE);
        a.cancel(p1);
        Intent intent1=new Intent(MedicationEdit.this,AlarmRem.class);
        intent1.putExtra("MedicationName",medicine_name);
        PendingIntent p2=PendingIntent.getBroadcast(getApplicationContext(),old_remid,intent1,0);
        AlarmManager a_rem=(AlarmManager)getSystemService(ALARM_SERVICE);
        a_rem.cancel(p2);
        /*Homepage.medicine_array.remove(index);
        if(Homepage.medicine_array.size()==1)
            Homepage.medicine_array.remove(0);
        Homepage.medicine_adapter.notifyDataSetChanged();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Medication");
        HashMap<String ,String> hm = new HashMap<>();
        for(int loop=0;loop<Homepage.medicine_array.size();loop++)
            hm.put(String.valueOf(loop),Homepage.medicine_array.get(loop));
        */
        Homepage.temp_array.remove(index);
        if(Homepage.temp_array.size()==1)
            Homepage.temp_array.remove(0);
        //Homepage.medicine_adapter.notifyDataSetChanged();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user=mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(user).child("Medication");
        HashMap<String ,String> hm = new HashMap<>();
        for(int loop=0;loop<Homepage.temp_array.size();loop++)
            hm.put(String.valueOf(loop),Homepage.temp_array.get(loop));


        myRef.setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MedicationEdit.this,"Running",Toast.LENGTH_SHORT).show();
                if(task.isSuccessful())
                {
                    Toast.makeText(MedicationEdit.this,"Successful",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MedicationEdit.this,"Not Successful",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent home=new Intent(MedicationEdit.this,Homepage.class);
        startActivity(home);

    }

    public int CalculateTimep()
    {
        if(timeperiod.equals("every hour"))
        {
            return 60*60;
        }
        else if(timeperiod.equals("daily"))
        {
            return 60*60*24;
        }
        else if(timeperiod.equals("once in two days"))
        {
            return 2*60*60*24;
        }
        else if(timeperiod.equals("once in three days"))
        {
            return 3*60*60*24;
        }
        else
        {
            return 7*60*60*24;
        }
        //{"every hour","daily","once in two days","once in three days","once in a week"};
    }
    //{"2 pill(s)","5 pill(s)","10 pill(s)","20 pill(s)",}
    public int CalculateNotifyWhen(String temp)
    {
        if(temp.equals("2 pill(s)"))
        {
            return 2;
        }
        else if(temp.equals("5 pill(s)"))
        {
            return 5;
        }
        else if(temp.equals("10 pill(s)"))
        {
            return 10;
        }
        else
        {
            return 20;
        }
    }
    //"0.5 Pill(s)","1 Pill(s)","2 Pill(s)"
    public int CalculatePills(String temp)
    {
        if(temp.equals("0.5 Pill(s)"))
        {
            return -1;
        }
        else if(temp.equals("1 Pill(s)"))
        {
            return 1;
        }
        else if(temp.equals("2 Pill(s)"))
        {
            return 2;
        }
        else
            return 5;

    }
}

