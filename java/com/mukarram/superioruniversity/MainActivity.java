package com.mukarram.superioruniversity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    ImageView logo;

    Animation topAnimantion,bottomAnimation,middleAnimation,sideAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        logo=findViewById(R.id.logo);


        //Animation Calls
        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        sideAnimation= AnimationUtils.loadAnimation(this, R.anim.side_slide);

        //-----------Setting Animations to the elements of Splash Screen

        logo.setAnimation(sideAnimation);


        //Splash Screen Code to call new Activity after some time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //  Calling new Activity
                Intent intent = new Intent(MainActivity.this, Portal.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}