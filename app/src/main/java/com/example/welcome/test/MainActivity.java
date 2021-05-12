package com.example.welcome.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText memail;
    private TextInputEditText mpassword;
    private Button register;
    private Button signin;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        register=(Button)findViewById(R.id.register1);
        memail=(TextInputEditText)findViewById(R.id.email1);
        mpassword=(TextInputEditText)findViewById(R.id.pass1);
        signin=(Button)findViewById(R.id.signin1);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(memail.getText().toString(),mpassword.getText().toString());
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Main3Activity.class);
                startActivity(i);
            }
        });
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void createAccount(String email,String password)
    {
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Fields required",Toast.LENGTH_SHORT).show();
            return;
        }
        pd=new ProgressDialog(MainActivity.this);
        pd.setTitle("Account");
        pd.setMessage("Creating your account");
        pd.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.cancel();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            pd.cancel();
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }
    public void updateUI(FirebaseUser user)
    {
        if(user!=null)
        {
            Intent i=new Intent(this,Homepage.class);
            startActivity(i);
        }

    }

}
