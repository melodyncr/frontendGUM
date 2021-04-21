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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.a499_android.DetermineQuestionType.levelList;
import static com.example.a499_android.LoginActivity.loggedUserName;
import static com.example.a499_android.DetermineQuestionType.responseList;
import static com.example.a499_android.DetermineQuestionType.getResponseList;
public class UploadSurvey extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Select Schedule";
    public static String F_S_USERNAME = loggedUserName;
    private ArrayList<String> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_survey);


        Object response_list_obj = responseList;
        Object updateCountObj = getResponseList;
        DocumentReference docRef = db.collection("Surveys").document(DetermineQuestionType.SURVEYQ);
        DocumentReference docDetermineLevel = db.collection("Surveys").document(DetermineQuestionType.SURVEYR);
        DocumentReference userDoc = db.collection("Users_List").document("List");
        CollectionReference collectionReference = db.collection("Messages");


        Log.d(TAG, response_list_obj.toString()  + "    " + updateCountObj.toString());
        docDetermineLevel.update(loggedUserName, response_list_obj);
        docRef.update(DetermineQuestionType.SURVEY_COUNT, updateCountObj);

        if(CreateAccount.first_survey){
            // Create document of messages to new User to Mark
            Map<String, Object> newUser = new HashMap<>();
            List<String> msg = new ArrayList<String>(){{
                add("Hello, this message box is a place where you can interact with Mark if you have any questions regarding GUM!");
            }};
            newUser.put("messages", msg);
            Log.d(TAG, String.valueOf(newUser));

            collectionReference.document(loggedUserName.toLowerCase()).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
            //adds user in the list of users
            userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> data = document.getData();
                            Iterator it = data.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry)it.next();
                                if(pair.getKey().toString().equals("user_names")){ userList = (ArrayList<String>) document.get("user_names"); }
                                it.remove(); // avoids a ConcurrentModificationException
                            }
                            userList.add(loggedUserName);
                            Log.d(TAG, userList.toString());
                            Object obj = userList;
                            userDoc.update("user_names",obj);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

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
