package com.example.welcome.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView medicinelist;
    TextView text;
    static ArrayList<String> medicine_array=new ArrayList<>();
    static ArrayList<String> temp_array=new ArrayList<>();
    static ArrayAdapter medicine_adapter;
    String med_name="Medication";
    String med_id,rem_id;
    String value;
    int year,month,day_of_month,hour_of_day,minute;
    int send_dosage,send_frequency,send_remaining,send_notify;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        final ProgressDialog pd=new ProgressDialog(Homepage.this);
        pd.setTitle("Medicine");
        pd.setMessage("Fetching your medicines");
        //FirebaseAuth mAuth=new FirebaseAuth()
        pd.show();
        text=(TextView)findViewById(R.id.NoneText);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        medicine_array.clear();
        temp_array.clear();
        flag=0;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String user=mAuth.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //DatabaseReference myRef = database.getReference(user).child("Medication");

        DatabaseReference myRef = database.getReference(user).child("Appointments");
        //DatabaseReference myRef = database.getReference(user);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //String temp=dataSanpshot.getValue(String.class);
                String temp=dataSnapshot.child("abc").getValue(String.class);
                temp+="\n"+dataSnapshot.child("abd").getValue(String.class);
                //String temp=dataSnapshot.child("Medication").getValue(String.class);
                if(flag==0){
                    text.setVisibility(View.INVISIBLE);
                    flag=1;

                }
                temp_array.add(temp);
                StringTokenizer st=new StringTokenizer(temp,"```");
                if(st.hasMoreElements())
                    value=String.valueOf(st.nextElement());
                medicine_array.add(value);
                medicine_adapter.notifyDataSetChanged();
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
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            pd.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        medicinelist=(ListView)findViewById(R.id.MedicineList);
        medicine_adapter=new ArrayAdapter(this,R.layout.list_view_custom,R.id.customText,medicine_array);
        //medicine_adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,medicine_array);
        medicinelist.setAdapter(medicine_adapter);
        medicinelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    med_id=extras.getString("MEDID");
                    med_name=extras.getString("medication_name");
                    rem_id=extras.getString("REMID");
                    //Toast.makeText(Homepage.this,med_id+"---"+rem_id,Toast.LENGTH_LONG).show();
                    year=extras.getInt("year");
                    month=extras.getInt("month");
                    day_of_month=extras.getInt("day_of_month");
                    hour_of_day=extras.getInt("hour_of_day");
                    minute=extras.getInt("minute");
                    //Toast.makeText(Homepage.this,"Time is "+hour_of_day+":"+minute,Toast.LENGTH_SHORT).show();
                    send_dosage=extras.getInt("send_dosage");
                    send_frequency=extras.getInt("send_frequency");
                    send_notify=extras.getInt("send_notify");
                    send_remaining=extras.getInt("send_remaining");
                }
                if (position != 0) {
                    Intent i = new Intent(Homepage.this, MedicationEdit.class);
                    //Toast.makeText(Homepage.this,med_id+"--"+rem_id,Toast.LENGTH_LONG).show();

                    i.putExtra("index", position);
                    //i.putExtra("App_type",appointmenttype);
                    i.putExtra("MEDID",med_id);
                    i.putExtra("MedicationName",med_name);
                    i.putExtra("REMID",rem_id);
                    i.putExtra("year",year);
                    i.putExtra("month",month);
                    i.putExtra("day_of_month",day_of_month);
                   // Toast.makeText(Homepage.this,"Time is "+hour_of_day+":"+minute,Toast.LENGTH_SHORT).show();
                    i.putExtra("hour_of_day",hour_of_day);
                    i.putExtra("minute",minute);
                    i.putExtra("send_dosage",send_dosage);
                    i.putExtra("send_notify",send_notify);
                    i.putExtra("send_frequency",send_frequency);
                    i.putExtra("send_remaining",send_remaining);
                    i.putExtra("Details",temp_array.get(position));
                    startActivity(i);
                }
            }
            });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homepage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent n=new Intent(getApplicationContext(),Medication.class);
            startActivity(n);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id==R.id.signout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(Homepage.this,MainActivity.class);
            startActivity(i);
        }
        else if(id==R.id.fitness)
        {
            Intent i=new Intent(Homepage.this,Fitness.class);
            startActivity(i);
        }
        else if(id==R.id.appointments)
        {
            Intent i=new Intent(Homepage.this,Appointments.class);
            startActivity(i);
        }
        else if(id==R.id.medication)
        {
            Intent i=new Intent(Homepage.this,Medication.class);
            startActivity(i);

        }
        else if(id==R.id.retailer)
        {
            startActivity(new Intent(Homepage.this,Retailer.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
