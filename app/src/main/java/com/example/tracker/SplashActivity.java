package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // Duration of splash screen
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> {
            // Check if user is logged in
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                // User is logged in, redirect to AddBalanceActivity
                Intent intent = new Intent(SplashActivity.this, AddBalanceActivity.class);
                startActivity(intent);
            } else {
                // User is not logged in, redirect to LoginActivity
                Intent intent = new Intent(SplashActivity.this, login.class);
                startActivity(intent);
            }
            finish(); // Close SplashActivity
        }, SPLASH_DISPLAY_LENGTH);
    }
}
