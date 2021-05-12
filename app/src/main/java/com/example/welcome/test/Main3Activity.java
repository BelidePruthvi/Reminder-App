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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main3Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText memail;
    private TextInputEditText mpassword;
    private Button signin;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sign In");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        memail=(TextInputEditText)findViewById(R.id.email1);
        mpassword=(TextInputEditText)findViewById(R.id.pass1);
        signin=(Button)findViewById(R.id.register1);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(memail.getText().toString(),mpassword.getText().toString());
            }
        });

    }
    public void signIn(String email,String password)
    {
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Fields required",Toast.LENGTH_SHORT).show();
            return;
        }
        pd=new ProgressDialog(Main3Activity.this);
        pd.setTitle("Sign In");
        pd.setMessage("Signing in...");
        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Main3Activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.cancel();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            pd.cancel();
                            Toast.makeText(Main3Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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
