package com.example.a499_android;

import android.content.Context;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LandingPage extends AppCompatActivity {

    public static final String EXTRA = "LandingPage EXTRA";
    private static final String TAG = "Current User Data";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button editSchedule = findViewById(R.id.button6);
        TextView displayedPoints = findViewById(R.id.pointDisplay);
        TextView displayedUsername = findViewById(R.id.usernameDisplay);

        // NOTE: user info read from db will be hardcoded until login activity is done
        String currentUserName = "ExampleUserName";

        // Access a Cloud Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userNameRef = db.collection("Users").document(currentUserName);

        // Read User Info
        getUserInfo(new FirestoreCallback() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document.exists()) {
                    Log.d(TAG, "Found User Data");
                    Toast.makeText(LandingPage.this, "Successfully Found User Data", Toast.LENGTH_SHORT).show();
                    displayedPoints.setText(document.getData().get("Points").toString());
                    displayedUsername.setText(currentUserName);
                } else {
                    Toast.makeText(LandingPage.this, "Unable to Load User Data", Toast.LENGTH_SHORT).show();
                }
            }
        }, userNameRef);

        editSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, UpdateSchedule.class);
                startActivity(intent);
            }
        });
    }

    // Intent Factory
    public static Intent getIntent(Context context, String val){
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(EXTRA, val);
        return intent;
    }

    //maybe retrieve schedule and pass it through intent first
    public void startEditScheduleActivity(View view){
        Intent intent = SelectSchedule.getIntent(this, "");
        startActivity(intent);
    }

    public void startAnimationTestActivity(View view){
        Intent intent = AnimationTest.getIntent(this, "");
        startActivity(intent);
    }

    private void getUserInfo(FirestoreCallback firestoreCallback, DocumentReference currentUserName) {
        currentUserName.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        firestoreCallback.onSuccess(document);
                    }
                } else {
                    Log.d(TAG, "ERROR: ", task.getException());
                }
            }
        });
    }

    private interface FirestoreCallback {
        void onSuccess(DocumentSnapshot document);
    }

}
