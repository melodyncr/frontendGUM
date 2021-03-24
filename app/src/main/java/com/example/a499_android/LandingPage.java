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

import com.example.a499_android.utility.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.a499_android.LoginActivity.loggedUserName;

public class LandingPage extends AppCompatActivity {

    public static final String EXTRA = "LandingPage EXTRA";
    private static final String TAG = "Current User Data";
    public static final String WSURVEYQ = "WSurveyQ";
    public static final String WSURVEYR = "WSurveyR";
    public static final String TYPE_SURVEY = "Weekly Survey";
    public static final String W_SURVEY_COUNT = "w_survey_count";
    public static final String W_SURVEY_Q = "w_survey_q";
    public static final String W_SURVEY_QC = "w_survey_qc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button editSchedule = findViewById(R.id.editScheduleBtn);
        Button changeAvatar = findViewById(R.id.changeAvatarBtn);
        Button logoutUser = findViewById(R.id.logoutBtn);
        Button startSurveyBtn = findViewById(R.id.startSurveyBtn);

        // Start Exercise
        Button startExerciseBtn = findViewById(R.id.startActivityBtn);

        TextView displayedPoints = findViewById(R.id.pointDisplay);
        TextView displayedUsername = findViewById(R.id.usernameDisplay);

        // NOTE: user info read from db will be hardcoded until login activity is done
        String currentUserName = LoginActivity.loggedUserName;
        String uName = SaveSharedPreference.getUserName(LandingPage.this);
        // Access a Cloud Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //This originally used currentUserName
        DocumentReference userNameRef = db.collection("Users").document(uName);

        // Read User Info
        getUserInfo(new FirestoreCallback() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document.exists()) {
                    Log.d(TAG, "Found User Data");
                    Toast.makeText(LandingPage.this, "Successfully Found User Data", Toast.LENGTH_SHORT).show();
                    displayedPoints.setText(document.getData().get("Points").toString());
                    displayedUsername.setText(uName);//This originally used currentUserName
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

        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveSharedPreference.clearUserName(LandingPage.this); //clears preference of username and anything else in there
                Intent toMainActivityIntent = new Intent(LandingPage.this, MainActivity.class);
                startActivity(toMainActivityIntent);
            }
        });

        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPage.this, SelectAvatar.class);
                startActivity(intent);
            }
        });

        startSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LandingPage.this, DetermineQuestionType.class);
                startActivity(intent);
            }
        });

        // Start Activity
        startExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LandingPage.this, StartExercise.class);
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
                        Log.d(TAG, currentUserName.getId() + " username");
                        loggedUserName = currentUserName.getId();
                        CreateAccount.first_survey = false;
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
