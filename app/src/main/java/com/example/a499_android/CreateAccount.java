package com.example.a499_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    private TextView usernameField, passwordField, emailField;
    private Button createAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        wiredUp();

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = usernameField.getText().toString();
                String enteredPassword = passwordField.getText().toString();
                String enteredEmail = emailField.getText().toString();
                if (checkUsernameValidity(enteredUsername) && checkPasswordValidity(
                        enteredPassword) && checkEmailValidity(enteredEmail)) {
                    /**
                     * Add to database. Warnings will come from the functions themselves.
                     */

                    Toast.makeText(CreateAccount.this, "Your account has been successfully created",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void wiredUp() {
        usernameField = findViewById(R.id.newUsernameText);
        passwordField = findViewById(R.id.newPasswordText);
        emailField = findViewById(R.id.newUserEmailText);
        createAccountBtn = findViewById(R.id.createAccountBtn);
    }

    private boolean checkUsernameValidity(String enteredUsername) {
        /**
         * Create code for querying through firestore DB for enteredUsername
         */
        if (enteredUsername.length() < 5) {
            Toast.makeText(CreateAccount.this, "This username is too short",
                    Toast.LENGTH_SHORT)
                    .show();

            return false;
        }

        return true;
    }

    private boolean checkPasswordValidity(String enteredPassword) {
        if (enteredPassword.length() < 5) {
            Toast.makeText(CreateAccount.this, "This password is too short",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    private boolean checkEmailValidity(String enteredEmail) {
        /**
         * Code to check if email is in database
         */

        return true;
    }
}