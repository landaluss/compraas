package com.chema.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private Timer timer;
    private ProgressBar progressBar;
    private int i = 0;
    private boolean sesionPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        prefs = getSharedPreferences("Preferences" , Context.MODE_PRIVATE);
        sesionPrefs = prefs.getBoolean("sesion",
                false);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        final long intervalo = 15;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (i < 100){
                    progressBar.setProgress(i);
                    i++;
                }else{
                    timer.cancel();

                    if(sesionPrefs){
                        Intent intent = new Intent(SplashScreenActivity.this , PrincipalActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this , MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        },0,intervalo);

    }

    private String getUserMailPrefs(){ return prefs.getString("correo" , "");}
    private String getUserPasslPrefs(){ return prefs.getString("clave" , "");}
}
