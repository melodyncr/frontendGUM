package com.example.a499_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a499_android.utility.SaveSharedPreference;
import com.example.a499_android.utility.TidbitsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.a499_android.LoginActivity.loggedUserName;

public class FinishedExerciseActivity extends AppCompatActivity {
    DocumentReference docRef;
    private final String TAG = "Workout Complete";
    CollectionReference tidbits;
    List<String> tidBitsList;
    List<String> tidbitsIdList;
    private long pointTotal;
    public static boolean fromNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_exercise);

        TextView userPoints = findViewById(R.id.finishedExerciseTotalPts);
        TextView earnedMessage = findViewById(R.id.textView4);
        earnedMessage.setVisibility(View.INVISIBLE);

        // NOTE: user info read from db will be hardcoded until login activity is done
        String uName = SaveSharedPreference.getUserName(FinishedExerciseActivity.this);
        // Access a Cloud Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //This originally used currentUserName
        DocumentReference userNameRef = db.collection("Users").document(uName);
        docRef = db.collection("Users").document(uName);

        // Read User Info
        getUserInfo(new FinishedExerciseActivity.FirestoreCallback() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document.exists()) {
                    if(fromNotification){
                        //add points
                        pointTotal = (long) document.getData().get("Points");
                        pointTotal = pointTotal + 1;
                        docRef.update("Points", pointTotal);
                        userPoints.setText("Total Points: " + document.getData().get("Points").toString());
                        earnedMessage.setText("You earned " + document.getData().get("Points").toString() + "point");
                        earnedMessage.setVisibility(View.VISIBLE);
                        fromNotification = false;
                    } else{ userPoints.setText("Total Points: " + document.getData().get("Points").toString()); }
                } else {
                    Toast.makeText(FinishedExerciseActivity.this, "Unable to Load User Data", Toast.LENGTH_SHORT).show();
                }
            }
        }, userNameRef);



        TextView tidbit = findViewById(R.id.tidbit);
        tidbits = db.collection("Tidbits");

        getTidbits(new FirestoreCallback2() {
            @Override
            public void onSuccess(List<String> tbIds, List<String> tidbits) {
                if(tidbits.size() >= 0) {
                    tidBitsList = tidbits;
                    tidbitsIdList = tbIds;
                    Log.d("In onCreate, IDs: ", String.valueOf(tidbitsIdList));
                    Log.d("In onCreate, Values: ", String.valueOf(tidBitsList));

                    // define the range
                    int max = tidBitsList.size()-1;
                    int min = 1;
                    int range = max - min + 1;
                    int rand = (int)(Math.random() * range) + min;
                    tidbit.setText(tidBitsList.get(rand));

                } else {
                    Log.d(TAG, "Something happened!");
                }
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectWorkout.selectedWorkout = "none";
                Intent intent = new Intent(FinishedExerciseActivity.this, LandingPage.class);
                startActivity(intent);
            }
        });

    }

    private void getUserInfo(FinishedExerciseActivity.FirestoreCallback firestoreCallback, DocumentReference currentUserName) {
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

    private void getTidbits(FinishedExerciseActivity.FirestoreCallback2 firestoreCallback) {
        tidbits.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> listOfTidbits = new ArrayList<String>();
                List<String> listOfIds = new ArrayList<String>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String tidbitVal = document.getString("Tidbit");
                        listOfIds.add(document.getId());
                        listOfTidbits.add(tidbitVal);
                    }
                    firestoreCallback.onSuccess(listOfIds, listOfTidbits);
                } else {
                    Log.d(TAG, "=====================");
                }
            }
        });
    }

    private interface FirestoreCallback2 {
        void onSuccess(List<String> tidbits, List<String> tbIdList);
    }

    private interface FirestoreCallback {
        void onSuccess(DocumentSnapshot document);
    }
}