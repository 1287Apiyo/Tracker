package com.example.tracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText;
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
        usernameEditText = findViewById(R.id.login_username_edit_text);
        emailEditText = findViewById(R.id.login_email_edit_text);
        passwordEditText = findViewById(R.id.login_password_edit_text);
        loginButton = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.dont_have_account_textview);
        forgotPasswordTextView = findViewById(R.id.forgot_password_textview);

        // Set up button click listeners
        loginButton.setOnClickListener(v -> handleLogin());

        signUpTextView.setOnClickListener(v -> handleSignUp());

        forgotPasswordTextView.setOnClickListener(v -> handleForgotPassword());
    }

    private void handleLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Please enter your email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Please enter your password");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("Please enter your username");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        saveUsernameToPreferences(username);

                        // Always navigate to AddBalanceActivity
                        redirectToAddBalanceActivity();
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUsernameToPreferences(String username) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    private void redirectToAddBalanceActivity() {
        // Navigate to AddBalanceActivity
        Intent intent = new Intent(login.this, AddBalanceActivity.class);
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
