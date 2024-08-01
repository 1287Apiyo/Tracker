package com.example.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpTextView, forgotPasswordTextView;
    private FirebaseAuth mAuth;

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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleForgotPassword();
            }
        });
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

        // Firebase authentication logic
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Navigate to the Home activity
            Intent intent = new Intent(login.this, Home.class);
            startActivity(intent);
            finish();
        }
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
