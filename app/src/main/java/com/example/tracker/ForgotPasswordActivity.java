package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private TextView backToLoginTextView;
    private FirebaseAuth mAuth;
    private static final String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        emailEditText = findViewById(R.id.email_edit_text);
        resetPasswordButton = findViewById(R.id.reset_password_button);
        backToLoginTextView = findViewById(R.id.back_to_login_textview);

        // Set up button click listeners
        resetPasswordButton.setOnClickListener(v -> handlePasswordReset());
        backToLoginTextView.setOnClickListener(v -> navigateToLogin());
    }

    private void handlePasswordReset() {
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Please enter your email");
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                        Toast.makeText(ForgotPasswordActivity.this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w(TAG, "sendPasswordResetEmail:failure", task.getException());
                        Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToLogin() {
        // Navigate back to Login activity
        Intent intent = new Intent(ForgotPasswordActivity.this, login.class);
        startActivity(intent);
        finish(); // Close the ForgotPasswordActivity
    }
}
