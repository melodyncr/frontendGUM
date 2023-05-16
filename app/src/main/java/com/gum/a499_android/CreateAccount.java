package com.gum.a499_android;

import android.content.Intent;
import android.graphics.Color;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {
    // Hi there
    private final String TAG = "Create Account";

    // these lists are the paths for each document for firebase, when we create a new user we will set the first_survey boolean variable to true to
    // tell the app it's a first survey and will be uploading all their results to the first survey collections
    public static final String FSURVEYQ = "FSurveyQ";
    public static final String FSURVEYR = "FSurveyR";
    public static final String TYPE_SURVEY = "Onboarding Survey";
    public static final String F_SURVEY_COUNT = "f_survey_count";
    public static final String F_SURVEY_Q = "f_survey_q";
    public static final String F_SURVEY_QC = "f_survey_qc";
    public static boolean first_survey = false;


    private TextView usernameField, passwordField,redPassword, redUser;
    private Button createAccountBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference users = db.collection("Users");
    DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        wiredUp();


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
                            //userDocRef.
                            //String [] temp_time = {"9:00 AM", "11:00 AM", "1:00 AM"
                                   // , "3:00 PM", "5:00 PM", "7:00 PM"};
                            newUser.put("Password", enteredPassword);
                            newUser.put("Points", 0);
                            newUser.put("Score", 0);
                            newUser.put("FitnessLvl"," ");
                            newUser.put("Groups", Arrays.asList());
                            newUser.put("AvatarUrl", "orange_avatar.png");
                            newUser.put("Schedule", Arrays.asList("9:00 AM", "10:00 AM", "11:00 AM"
                                    , "2:00 PM", "3:00 PM", "4:00 PM"));
                            List<String> listOfAvatars = new ArrayList<String>(){{
                                add("orange_avatar.png");
                            }};
                            newUser.put("UnlockedAvatars", listOfAvatars);

                            Log.d(TAG, String.valueOf(newUser));
                            Log.d(TAG, "Adding user to document");

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
                                            Intent toLoginIntent = new Intent(CreateAccount.this, IntroVideo.class);
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
            }
        });
    }

    private void wiredUp() {
        usernameField = findViewById(R.id.newUsernameText);
        passwordField = findViewById(R.id.newPasswordText);
        createAccountBtn = findViewById(R.id.createAccountBtn);
        redPassword = findViewById(R.id.redTextPassword);
        redUser = findViewById(R.id.redTextUser);
    }

    private boolean checkUsernameLength(String enteredUsername) {
        if (enteredUsername.length() < 5) {
            redUser.setText("Username is not 5 characters long.");
            return false;
        }
        redUser.setText("Username is valid");
        redUser.setTextColor(Color.GREEN);
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
            redPassword.setText("Password is not 5 characters long.");
            return false;
        }
        redPassword.setTextColor(Color.GREEN);
        redPassword.setText("Password is valid");
        return true;
    }

    private interface FirestoreCallback {
        void onSuccess(DocumentSnapshot document);
    }

    public void backToSignUp(View view) {
        startActivity(new Intent(CreateAccount.this, MainActivity.class));
    }

}