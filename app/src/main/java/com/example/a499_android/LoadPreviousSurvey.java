package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class LoadPreviousSurvey extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String TAG = "Load Previous Survey";
    public static ArrayList<String> w_survey_list_names = new ArrayList<>();
    public static ArrayList<String> previous_times_list = new ArrayList<>();

    DocumentReference wSurveyTotal, times;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_responses);
        wSurveyTotal = db.collection("Surveys").document("PastWSurveyR");
        times = db.collection("Surveys").document("PastSurveyTimes");
        getWSurvey();
    }

    void getWSurvey(){
        wSurveyTotal.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            w_survey_list_names.add(pair.getKey().toString());
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        //Log.d(TAG, w_survey_list_names.toString());
                        getTimes();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return surveyList;
    }
    void getTimes(){
        times.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Log.d(TAG,data.toString() + " times size " + data.size());
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            if (pair.getKey().toString().equals("times")) {
                                previous_times_list = (ArrayList<String>) document.get("times");
                            }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        Log.d(TAG, w_survey_list_names.toString());
                        Intent intent = new Intent(LoadPreviousSurvey.this, SelectASurvey.class);
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return timesList;
    }
}
