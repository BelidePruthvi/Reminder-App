package com.example.welcome.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.StringTokenizer;

public class TestMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String details,details1;
    double d1,d2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent curr = getIntent();
        Bundle extras = curr.getExtras();
        if (extras != null) {
            details = extras.getString("details");
        }
        // Toast.makeText(TestMaps.this, details, Toast.LENGTH_SHORT).show();
        StringTokenizer st = new StringTokenizer(details, "```");
        if (st.hasMoreElements())
            st.nextElement();
        if (st.hasMoreElements()) {
            details1 = String.valueOf(st.nextElement());
            Toast.makeText(TestMaps.this, details1, Toast.LENGTH_SHORT).show();
            StringTokenizer st1=new StringTokenizer(details1,"\n");
            if(st1.hasMoreElements())
            {
                String var=String.valueOf(st1.nextElement());
                d1=Double.parseDouble(var);
            }
            if(st1.hasMoreElements())
            {
                String var=String.valueOf(st1.nextElement());
                d2=Double.parseDouble(var);
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng shop = new LatLng(d1, d2);
        mMap.addMarker(new MarkerOptions().position(shop).title("This is the Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shop,13));
        mMap = googleMap;

    }
}

