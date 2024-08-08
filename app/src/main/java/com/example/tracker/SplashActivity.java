package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent intent;
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                // User is logged in, navigate to BalanceActivity
                intent = new Intent(SplashActivity.this, AddBalanceActivity.class);
            } else {
                // No user is logged in, navigate to Login activity
                intent = new Intent(SplashActivity.this, login.class);
            }

            startActivity(intent);
            finish(); // Close the SplashScreenActivity
        }, SPLASH_DISPLAY_LENGTH);
    }
}
