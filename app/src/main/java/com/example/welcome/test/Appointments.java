package com.example.welcome.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Appointments extends AppCompatActivity {
    ListView appointmentlv;
    static ArrayList<String> al=new ArrayList<>();
    static ArrayList<String> temp_array=new ArrayList<>();
    static ArrayAdapter aa;
    String value,appointmenttype="Medication";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        appointmentlv=(ListView)findViewById(R.id.AppointmentListView);
        //aa=new ArrayAdapter(this,android.R.layout.simple_list_item_1,al);
        aa=new ArrayAdapter(this,R.layout.list_view_custom,R.id.customText,al);
        appointmentlv.setAdapter(aa);
        al.clear();
        temp_array.clear();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user=mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(user).child("Appointments");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String temp=dataSnapshot.getValue(String.class);
                temp_array.add(temp);
                StringTokenizer st=new StringTokenizer(temp,"```");
                if(st.hasMoreElements())
                    value=String.valueOf(st.nextElement());
                StringTokenizer st1=new StringTokenizer(value,"\n");
                String val="";
                if(st1.hasMoreElements())
                    val+=String.valueOf(st1.nextElement());
                if(st1.hasMoreElements())
                {
                    String val1=String.valueOf(st1.nextElement());
                    val+="\n"+val1.substring(0,7)+":"+val1.substring(7);
                }
                if(st1.hasMoreElements())
                {
                    String val1=String.valueOf(st1.nextElement());
                    val+="\n"+val1.substring(0,5)+val1.substring(11)+"-"+val1.substring(9,11)+"-"+val1.substring(5,9);
                }
                al.add(val);
                aa.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        appointmentlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent intent=getIntent();
                Bundle extras=intent.getExtras();
                value="0";
                if (extras != null) {
                    value = extras.getString("APPID");
                    appointmenttype=extras.getString("App_type");
                    //The key argument here must match that used in the other activity
                }*/
                if(position!=0) {
                    Intent i = new Intent(Appointments.this, Appointment2.class);
                    //i.putExtra("rem_details",position);
                    //i.putExtra("App_type",appointmenttype);
                    //i.putExtra("APPID",value);
                    i.putExtra("index",position);
                    i.putExtra("Details",temp_array.get(position));
                    startActivity(i);
                }
            }
        });

    }

    public void setAppointment(View v)
    {
        String appointmenttype="";
        if(v.getId()==R.id.GeneralCheckup)
        {
            appointmenttype="General Checkup";
        }
        else if(v.getId()==R.id.BloodTest)
        {
            appointmenttype="Blood Test";
        }
        else if(v.getId()==R.id.SugarTest)
        {
            appointmenttype="Sugar Test";
        }
        else if(v.getId()==R.id.Others)
        {
            appointmenttype="Others";
        }
        Intent i=new Intent(this,Appointments1.class);
        i.putExtra("App_type",appointmenttype);
        startActivity(i);
    }
}
