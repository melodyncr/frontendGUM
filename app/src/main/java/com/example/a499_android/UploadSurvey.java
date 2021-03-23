package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.util.HashMap;
import java.util.Map;

import static com.example.a499_android.DetermineQuestionType.levelList;
import static com.example.a499_android.LoginActivity.loggedUserName;
import static com.example.a499_android.DetermineQuestionType.responseList;
import static com.example.a499_android.DetermineQuestionType.getResponseList;
public class UploadSurvey extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Select Schedule";
    public static String F_S_USERNAME = loggedUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_survey);


        Object response_list_obj = responseList;
        Object updateCountObj = getResponseList;
        DocumentReference docRef = db.collection("Surveys").document(DetermineQuestionType.SURVEYR);
        DocumentReference docDetermineLevel = db.collection("Users").document(loggedUserName);
        Log.d(TAG, response_list_obj.toString()  + "    " + updateCountObj.toString());
        docRef.update(loggedUserName, response_list_obj);
        docRef.update(DetermineQuestionType.SURVEY_COUNT, updateCountObj);

        if(CreateAccount.first_survey){
            int advance_lvl = Integer.parseInt(DetermineQuestionType.levelList.get(0));
            int intermediate_lvl = Integer.parseInt(DetermineQuestionType.levelList.get(1));
            String level = "";
            if(DetermineQuestionType.survey_score >= advance_lvl){
                level = "Advance";
            }else if(DetermineQuestionType.survey_score >= intermediate_lvl){
                level = "Intermediate";
            }else{
                level = "Beginner";
            }
            Object score_obj = DetermineQuestionType.survey_score;
            db.collection("Users").document(loggedUserName).update("Score",score_obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "Score is updated!");
                }
            });
            Log.d(TAG, level);
            Object level_obj = level;
            db.collection("Users").document(loggedUserName).update("FitnessLvl",level_obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "Level is determined you are ready!");

                }
            });
        }
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    responseList.clear();
                    getResponseList.clear();
                    levelList.clear();
                    DetermineQuestionType.weeklyQuestionsList.clear();
                    DetermineQuestionType.fitnessLevelScoreList.clear();
                    DetermineQuestionType.choicesList.clear();
                    if(CreateAccount.first_survey){
                        Intent intent = new Intent(UploadSurvey.this, SelectSchedule.class);
                        Toast.makeText(UploadSurvey.this, "Lets select your workout schedule!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }else {
                        Toast.makeText(UploadSurvey.this, "Survey is uploaded!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadSurvey.this, MainActivity.class);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(UploadSurvey.this, "Survey is is not uploaded!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadSurvey.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
