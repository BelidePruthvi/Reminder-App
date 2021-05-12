package com.example.welcome.test;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Fitness1 extends AppCompatActivity {

    TextView timerTextView;
    SeekBar timerSeekBar;
    Boolean counterIsActive = false;
    Button goButton;
    CountDownTimer countDownTimer;
    String value;
    int time;

    public void resetTimer() {
        timerTextView.setText("1:00");
        timerSeekBar.setProgress(60);
        timerSeekBar.setEnabled(true);
        countDownTimer.cancel();
        goButton.setText("GO!");
        counterIsActive = false;
    }

    public void buttonClicked(View view) {

        if (counterIsActive) {

            resetTimer();

        } else {

            counterIsActive = true;
            timerSeekBar.setEnabled(false);
            goButton.setText("STOP!");
            time=timerSeekBar.getProgress();
            //Toast.makeText(Fitness1.this,String.valueOf(time),Toast.LENGTH_SHORT).show();
            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {

                @Override
                public void onTick(long l) {
                    updateTimer((int) l / 1000);
                }

                @Override
                public void onFinish() {
                    MediaPlayer mplayer = MediaPlayer.create(getApplicationContext(), R.raw.audio);
                    mplayer.start();
                    resetTimer();
                    Intent intent=getIntent();
                    Bundle extras=intent.getExtras();
                    if (extras != null) {
                        value = extras.getString("type");
                    }
                    int m=time/60;
                    int sec=time-(m*60);
                    String value_to_be_sent=value+" for "+m+"Minutes and "+sec+"Seconds";
                    if(Fitness.temp_fit.size()==0) {
                        Fitness.temp_fit.add("Report:");
                    }
                    //Toast.makeText(Fitness1.this,"size is"+Fitness.temp_fit.size(),Toast.LENGTH_SHORT).show();
                    Fitness.temp_fit.add(value_to_be_sent);
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    String user=mAuth.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(user).child("Fitness");
                    HashMap<String ,String> hm = new HashMap<>();

                    for(int loop=0;loop<Fitness.temp_fit.size();loop++)
                        hm.put(String.valueOf(loop),Fitness.temp_fit.get(loop));
                    myRef.setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Fitness1.this,"Running",Toast.LENGTH_SHORT).show();
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Fitness1.this,"Successful",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Fitness1.this,"Not Successful",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    startActivity(new Intent(Fitness1.this,Fitness.class));


                }
            }.start();
        }
    }

    public void updateTimer(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);

        String temp = Integer.toString(seconds);

        if (seconds <= 9) {
            temp = "0" + temp;
        }

        timerTextView.setText(Integer.toString(minutes) + ":" + temp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness1);


        timerSeekBar = findViewById(R.id.timerSeekBar);
        timerTextView = findViewById(R.id.countdownTextView);
        goButton = findViewById(R.id.goButton);
        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(60);
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateTimer(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
