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
public class DetermineQuestionType extends AppCompatActivity {

    public static String TAG = "Determine Questions";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference surveyQuestions = db.collection("Surveys").document("FSurveyQ");
    DocumentReference surveyTotal = db.collection("Surveys").document("FSurveyR");
    public static ArrayList<String> weeklyQuestionsList  = new ArrayList<>();
    public static ArrayList<String> choicesList = new ArrayList<>();
    public static ArrayList<String> responseList = new ArrayList<>();
    public static ArrayList<String> getResponseList = new ArrayList<>();
    public static  int question_count = 0;
    public static  int response_count=0;
    public static String TYPE_SURVEY  = "First Survey";
    public static String documentColumn = "FSurveyR";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_questions);
        getSurveyTotal();
        init_firebase();
        question_count =0;
        response_count =0;
    }

    void getSurveyTotal(){
        surveyTotal.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals("f_survey_count")){ getResponseList = (ArrayList<String>) document.get("f_survey_count"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        Log.d(TAG, getResponseList.toString());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return getResponseList;
    }


    void init_firebase(){
        surveyQuestions.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals("f_survey_q")){ weeklyQuestionsList = (ArrayList<String>) document.get("f_survey_q"); }
                            if(pair.getKey().toString().equals("f_survey_qc")){ choicesList = (ArrayList<String>) document.get("f_survey_qc"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        Log.d(TAG, weeklyQuestionsList.toString() + "\n" + choicesList.toString());
                        boolean multiChoiceQ = determineQuestion(weeklyQuestionsList.get(0).charAt(0));
                        Intent intent;
                        if(multiChoiceQ){
                            intent = new Intent(DetermineQuestionType.this, SurveyMultiC.class);
                        }else{
                            intent = new Intent(DetermineQuestionType.this, SurveyResponse.class);
                        }
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return weeklyQuestionsList;
    }

    public static boolean determineQuestion(char type){
        if(type == 'M'){
            return true;
        }else{
            return false;
        }
    }
}
