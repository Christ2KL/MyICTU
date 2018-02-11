package com.tekaware.christ.myictu;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar myProgressBar;
    private int myProgressBarStatus;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        myProgressBar = (ProgressBar) findViewById(R.id.myProgressBar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        int SPLASH_TIME_OUT = 4250;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashScreenActivity.this, Login.class);
                startActivity(homeIntent);
                finish();

            }
        }, SPLASH_TIME_OUT);

        myProgressBarStatus = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (myProgressBarStatus < 2000){
                    myProgressBarStatus += 1;
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            myProgressBar.setProgress(myProgressBarStatus);
                        }
                    });
                    try {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException e ){
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }
}
