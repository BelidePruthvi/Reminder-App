package com.example.welcome.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Retailer extends AppCompatActivity {
    EditText nam,store,loc,phno;
    Button register;
    LocationManager locationManager;
    LocationListener locationListener;
    TextView list;
    int flag;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer);
        nam=(EditText)findViewById(R.id.nameEdit);
        store=(EditText)findViewById(R.id.storeEdit);
        loc=(EditText)findViewById(R.id.locEdit);
        phno=(EditText)findViewById(R.id.phoneEdit);
        register=(Button)findViewById(R.id.register_retailer);
        list=(TextView)findViewById(R.id.listretailers);
        flag=0;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nam.getText().toString().matches("") || store.getText().toString().matches("")) {
                    Toast.makeText(Retailer.this, "Fill the Fields", Toast.LENGTH_SHORT).show();
                } else {
                    locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            //Log.i("Location",location.toString());
                            if(flag==0)
                            {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Retailers");
                            String temp="Name : "+nam.getText().toString()+"\nStore : "+store.getText().toString()+"\nLocation : "+loc.getText().toString();
                            temp+="\nPhone Number : "+phno.getText().toString();
                            temp+="```"+String.valueOf(location.getLatitude());
                            temp+="\n"+String.valueOf(location.getLongitude());
                            RetailerList.r_array.add(temp);
                            HashMap<String ,String> hm = new HashMap<>();
                            for(int loop=0;loop<RetailerList.r_array.size();loop++)
                                hm.put(String.valueOf(loop),RetailerList.r_array.get(loop));
                            myRef.setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        //Toast.makeText(Retailer.this,"Successful",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Retailer.this,RetailerList.class));
                                    }
                                    else
                                    {
                                        Toast.makeText(Retailer.this,"Failure",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        flag=1;
                        }}

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    };

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Retailer.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    }
                }
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Retailer.this,RetailerList.class));
            }
        });

    }
}
