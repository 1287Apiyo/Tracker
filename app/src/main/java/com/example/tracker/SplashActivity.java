package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // Duration of splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // Always redirect to login activity
            Intent intent = new Intent(SplashActivity.this, login.class);
            startActivity(intent);
            finish(); // Close SplashActivity
        }, SPLASH_DISPLAY_LENGTH);
    }
}
