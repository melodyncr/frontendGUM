package com.example.a499_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CreateAccount extends AppCompatActivity {

    private TextView usernameField, passwordField;
    private Button createAccountBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        wiredUp();

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredUsername = usernameField.getText().toString();
                String enteredPassword = passwordField.getText().toString();
                if (checkUsernameValidity(enteredUsername) && checkPasswordValidity(
                        enteredPassword)) {
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
        createAccountBtn = findViewById(R.id.createAccountBtn);
    }

    private boolean checkUsernameValidity(String enteredUsername) {
        /**
         * Create code for querying through firestore DB for enteredUsername
         */
        final boolean[] usernameAvailable = {true};
        String USERTAG = "User documents";
        Log.d("Entered Username: ", enteredUsername);

        DocumentReference userDocRef = users.document(enteredUsername);

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(USERTAG, "DocumentSnapshot data: " + document.getData());
                        Log.d("Warning", "Uh oh username is taken");
                        Toast.makeText(CreateAccount.this,
                                "This username is taken! Please enter a different username",
                                Toast.LENGTH_SHORT)
                                .show();
                        usernameAvailable[0] = false;
                    } else {
                        Log.d(USERTAG, "No such document, This Username is available");
                    }
                } else {
                    Log.d(USERTAG, "get failed with ", task.getException());
                }
            }
        });

        /**
         * If there was a user with entered username found already;
         */

        if(!usernameAvailable[0]) return false;

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
}