package com.mukarram.superioruniversity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class Portal extends AppCompatActivity {
    private ImageView logo;
    Animation topAnimantion,bottomAnimation,middleAnimation,sideAnimation;



private  Button sp,tp,vp,ap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);


        sp=findViewById(R.id.student_portal);
        vp=findViewById(R.id.visitor_portal);
        tp=findViewById(R.id.teacher_portal);
        ap=findViewById(R.id.admin_portal);







        //for student
        sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Portal.this, Std_Login.class);
                startActivity(intent);
                finish();
            }
        });





        //for animation

        logo=findViewById(R.id.logos);

        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        sideAnimation= AnimationUtils.loadAnimation(this, R.anim.side_slide);


        //-----------Setting Animations to the elements of Splash Screen

        logo.setAnimation(topAnimantion);
        sp.setAnimation(sideAnimation);
        ap.setAnimation(sideAnimation);
        vp.setAnimation(sideAnimation);
        tp.setAnimation(sideAnimation);

    }


}