package com.abhigyan.user.hertzmusicplayer.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.transition.Fade;
import android.support.transition.TransitionInflater;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.abhigyan.user.hertzmusicplayer.R;

import java.io.IOException;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }, 2000);

    }
}
