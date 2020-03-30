package com.example.hocapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    private ImageView imageSplashScreen;           //splash screen gorseli
    private static int SPLASH_SCREEN_TİME=4000;    //splash screen gecis suresi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageSplashScreen=findViewById(R.id.imageSplashScreen);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.animation);  //animasyonu nesneler ile etkilesime sokuyoruz.
        imageSplashScreen.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(SplashScreen.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_TİME);
    }
}
