package com.example.welcome.test;

import android.app.ProgressDialog;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class RetailerList extends AppCompatActivity {
    ListView lv;
    static ArrayList<String>retail_array=new ArrayList<>();
    static ArrayList<String>r_array=new ArrayList<>();
    static ArrayAdapter retail_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_list);
        lv=(ListView)findViewById(R.id.retail_list);
        retail_adapter=new ArrayAdapter(this,R.layout.list_view_custom,R.id.customText,retail_array);
        lv.setAdapter(retail_adapter);
        final ProgressDialog pd=new ProgressDialog(RetailerList.this);
        pd.setTitle("Retailers");
        pd.setMessage("Loading please wait...");
        pd.show();
        retail_array.clear();
        r_array.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Retailers");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String temp=dataSnapshot.getValue(String.class);
                r_array.add(temp);
                StringTokenizer st=new StringTokenizer(temp,"```");
                String var="";
                if(st.hasMoreElements()) {
                    var = String.valueOf(st.nextElement());
                    retail_array.add(var);
                    retail_adapter.notifyDataSetChanged();
                }
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(RetailerList.this,TestMaps.class);
                //Toast.makeText(RetailerList.this,r_array.get(position),Toast.LENGTH_SHORT).show();
                i.putExtra("details",r_array.get(position));
                startActivity(i);
            }
        });

    }
}
