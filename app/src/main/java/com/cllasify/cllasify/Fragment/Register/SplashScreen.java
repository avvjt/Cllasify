package com.cllasify.cllasify.Fragment.Register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cllasify.cllasify.Fragment.Feed.Dashboard;
import com.cllasify.cllasify.R;

public class SplashScreen extends AppCompatActivity {

    public static int SPLASH_TIMER = 2000;

    //Variables
    TextView splashTxt;

    //Animation
    Animation splash_animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);


        //Animation id
        splash_animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);

        //Hooks
        splashTxt = findViewById(R.id.splash_txt);

        // Set animation on elements
        splashTxt.setAnimation(splash_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this, Dashboard.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_TIMER);

        }
    }