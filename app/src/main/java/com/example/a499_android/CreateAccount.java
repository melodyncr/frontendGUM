package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private final String TAG = "Create Account";

    public static final String FSURVEYQ = "FSurveyQ";
    public static final String FSURVEYR = "FSurveyR";
    public static final String TYPE_SURVEY = "First Survey";
    public static final String F_SURVEY_COUNT = "f_survey_count";
    public static final String F_SURVEY_Q = "f_survey_q";
    public static final String F_SURVEY_QC = "f_survey_qc";
    public static boolean first_survey = false;


    private TextView usernameField, passwordField;
    private Button createAccountBtn, createAccToMain;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users = db.collection("Users");
    DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        wiredUp();

        createAccToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMainIntent = new Intent(CreateAccount.this, MainActivity.class);
                startActivity(backToMainIntent);
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Creating account");
                String enteredUsername = usernameField.getText().toString().toLowerCase();
                String enteredPassword = passwordField.getText().toString();
                if(checkUsernameLength(enteredUsername) == false) {
                    Log.d(TAG, "Checking username length...");
                    return;
                }
                Log.d(TAG, "Name meets requirements");
                if(!checkPasswordValidity(enteredPassword)) {
                    Log.d(TAG, "Checking password length...");
                    return;
                }

                userDocRef = users.document(enteredUsername);

                Log.d("Content of Reference", String.valueOf(userDocRef));
                checkUsernameValidity(new FirestoreCallback() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        Log.d(TAG, "We are in onSuccess!");
                        if (document.exists()) {
                            Log.d(TAG, "This account name exists...");
                            Toast.makeText(CreateAccount.this,
                                    "Uh oh that username is taken! Enter another",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "username not found!");
                            /**
                             * Add to database. Warnings will come from the functions themselves.
                             */
                            Map<String, Object> newUser = new HashMap<>();
                            newUser.put("Password", enteredPassword);
                            newUser.put("Points", 0);
                            newUser.put("Score", 0);
                            newUser.put("AvatarUrl", "dead.png");
                            List<String> listOfAvatars = new ArrayList<String>(){{
                                add("dead.png");
                            }};
                            newUser.put("UnlockedAvatars", listOfAvatars);

                            Log.d(TAG, String.valueOf(newUser));

                            users.document(enteredUsername.toLowerCase()).set(newUser)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written! Line!!!!!!");
                                            Toast.makeText(CreateAccount.this,
                                                    "Your account has been successfully created",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                            first_survey = true;
                                            LoginActivity.loggedUserName = enteredUsername.toLowerCase();
                                            Intent toLoginIntent = new Intent(CreateAccount.this, DetermineQuestionType.class);
                                            startActivity(toLoginIntent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                            Toast.makeText(CreateAccount.this,
                                                    "Something went wrong with the creating account " +
                                                            "process",
                                                    Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                        }
                    }
                });

                Intent loginIntent = new Intent(CreateAccount.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
    }

    private void wiredUp() {
        usernameField = findViewById(R.id.newUsernameText);
        passwordField = findViewById(R.id.newPasswordText);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        createAccToMain = findViewById(R.id.createAccToMain);
    }

    private boolean checkUsernameLength(String enteredUsername) {
        if (enteredUsername.length() < 5) {
            Toast.makeText(CreateAccount.this, "This username is too short",
                    Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        return true;
    }

    private void checkUsernameValidity(FirestoreCallback firestoreCallback) {
        /**
         * Create code for querying through firestore DB for enteredUsername
         */
        Log.d(TAG, "Starting");

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        firestoreCallback.onSuccess(document);
                    } else {
                        Log.d(TAG, "No such document, This Username is available");
                        firestoreCallback.onSuccess(document);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
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

    private interface FirestoreCallback {
        void onSuccess(DocumentSnapshot document);
    }
}