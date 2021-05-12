package com.example.welcome.test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Fitness extends AppCompatActivity {

    static ArrayList<String > fit_al=new ArrayList<>();
    ListView fitnesslv;
    static ArrayList<String> temp_fit=new ArrayList<>();
    static ArrayAdapter fit_aa;
    TextView f1,f2,f3,f4;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
        fitnesslv=(ListView)findViewById(R.id.fitnesslist);
        f1=(TextView)findViewById(R.id.Cycling);
        f2=(TextView)findViewById(R.id.Exercise);
        f3=(TextView)findViewById(R.id.Physiotherapy);
        f4=(TextView)findViewById(R.id.Walking);
        fit_aa=new ArrayAdapter(this,R.layout.list_view_custom,R.id.customText,fit_al);
        fitnesslv.setAdapter(fit_aa);
        fit_al.clear();
        temp_fit.clear();
        fit_aa.notifyDataSetChanged();
        mAuth = FirebaseAuth.getInstance();
        String user=mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(user).child("Fitness");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String temp=dataSnapshot.getValue(String.class);
                fit_al.add(temp);
                temp_fit.add(temp);
                fit_aa.notifyDataSetChanged();
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
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value_sent="Cycling";
                Intent i=new Intent(getApplicationContext(),Fitness1.class);
                i.putExtra("type",value_sent);
                startActivity(i);
            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value_sent="Exercise";
                Intent i=new Intent(getApplicationContext(),Fitness1.class);
                i.putExtra("type",value_sent);
                startActivity(i);
            }
        });
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value_sent="Physiotherapy";
                Intent i=new Intent(getApplicationContext(),Fitness1.class);
                i.putExtra("type",value_sent);
                startActivity(i);
            }
        });
        f4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value_sent="Walking";
                Intent i=new Intent(getApplicationContext(),Fitness1.class);
                i.putExtra("type",value_sent);
                startActivity(i);
            }
        });
    }
    /*
    @Override
    protected void onStart() {
        fit_aa=new ArrayAdapter(this,R.layout.list_view_custom,R.id.customText,fit_al);
        fitnesslv.setAdapter(fit_aa);

        super.onStart();
    }*/
    /*
    public void timerFunction(View view)
    {
        int id=view.getId();
        String value_sent="Fitness";
        if(id==R.id.Exercise)
        {
            value_sent="Exercise";
        }
        else if(R.id.Others==id)
        {
            value_sent="Others";
        }
        else if(R.id.Cycling==id)
        {
            value_sent="Cycling";
        }
        else if(R.id.Physiotherapy==id)
        {
            value_sent="Physiotherapy";
        }
        Intent i=new Intent(this,Fitness1.class);
        i.putExtra("type",value_sent);
        startActivity(i);


    }*/
}
