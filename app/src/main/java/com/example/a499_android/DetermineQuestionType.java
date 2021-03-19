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
    public static String SURVEYQ = "";
    public static String SURVEYR = "";
    public static String T_TYPE_SURVEY = "";
    public static String SURVEY_COUNT = "";
    public static String SURVEY_Q = "";
    public static String SURVEY_QC = "";
    DocumentReference surveyQuestions;
    DocumentReference surveyTotal;
    public static ArrayList<String> weeklyQuestionsList  = new ArrayList<>();
    public static ArrayList<String> choicesList = new ArrayList<>();
    public static ArrayList<String> responseList = new ArrayList<>();
    public static ArrayList<String> getResponseList = new ArrayList<>();
    public static  int question_count = 0;
    public static  int response_count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_questions);
        boolean f_or_w = first_or_weekly(CreateAccount.first_survey);
        if(f_or_w){
            //first survey will be shown
            SURVEYQ = CreateAccount.FSURVEYQ;
            SURVEYR = CreateAccount.FSURVEYR;
            T_TYPE_SURVEY = CreateAccount.TYPE_SURVEY;
            SURVEY_COUNT = CreateAccount.F_SURVEY_COUNT;
            SURVEY_Q = CreateAccount.F_SURVEY_Q;
            SURVEY_QC = CreateAccount.F_SURVEY_QC;
        }else{
            //second survey
            SURVEYQ = LandingPage.WSURVEYQ;
            SURVEYR = LandingPage.WSURVEYR;
            T_TYPE_SURVEY = LandingPage.TYPE_SURVEY;
            SURVEY_COUNT = LandingPage.W_SURVEY_COUNT;
            SURVEY_Q = LandingPage.W_SURVEY_Q;
            SURVEY_QC = LandingPage.W_SURVEY_QC;
        }
        surveyQuestions = db.collection("Surveys").document(SURVEYQ);
        surveyTotal = db.collection("Surveys").document(SURVEYR);
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
                            if(pair.getKey().toString().equals(SURVEY_COUNT)){ getResponseList = (ArrayList<String>) document.get(SURVEY_COUNT); }
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
                            if(pair.getKey().toString().equals(SURVEY_Q)){ weeklyQuestionsList = (ArrayList<String>) document.get(SURVEY_Q); }
                            if(pair.getKey().toString().equals(SURVEY_QC)){ choicesList = (ArrayList<String>) document.get(SURVEY_QC); }
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
    public boolean first_or_weekly(boolean f_or_w){
        if (f_or_w) {
            return true;// is first survey
        }else{
            return false;// is weekly survey
        }
    }
}
