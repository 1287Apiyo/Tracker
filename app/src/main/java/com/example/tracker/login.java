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
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpTextView, forgotPasswordTextView;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        emailEditText = findViewById(R.id.login_email_edit_text);
        passwordEditText = findViewById(R.id.login_password_edit_text);
        loginButton = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.dont_have_account_textview);
        forgotPasswordTextView = findViewById(R.id.forgot_password_textview);

        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, redirect to the AddBalanceActivity
            String username = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : currentUser.getEmail();
            redirectToAddBalanceActivity(username); // Pass username here
            return;
        }

        // Set up button click listeners
        loginButton.setOnClickListener(v -> handleLogin());
        signUpTextView.setOnClickListener(v -> handleSignUp());
        forgotPasswordTextView.setOnClickListener(v -> handleForgotPassword());
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Please enter your email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Please enter your password");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String username = user != null ? user.getDisplayName() : email; // Retrieve username
                        redirectToAddBalanceActivity(username); // Pass the username here
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void redirectToAddBalanceActivity(String username) {
        Intent intent = new Intent(login.this, AddBalanceActivity.class);
        intent.putExtra("username", username); // Pass the username
        startActivity(intent);
        finish(); // Close the login activity
    }

    private void handleSignUp() {
        // Navigate to the Sign Up activity
        Intent intent = new Intent(login.this, signup.class);
        startActivity(intent);
    }

    private void handleForgotPassword() {
        // Navigate to the Forgot Password activity
        Intent intent = new Intent(login.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
