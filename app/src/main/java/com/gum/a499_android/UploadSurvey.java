package com.gum.a499_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.gum.a499_android.DetermineQuestionType.levelList;
import static com.gum.a499_android.DetermineQuestionType.responseList;
import static com.gum.a499_android.DetermineQuestionType.getResponseList;
public class UploadSurvey extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Select Schedule";
    public static String F_S_USERNAME = LoginActivity.loggedUserName;
    private ArrayList<String> userList = new ArrayList<>();
    private ArrayList<String> surveyComplete = new ArrayList<>();
    private String level_user = "";
    DocumentReference usersSurveyDone = db.collection("Surveys").document("WSurveyUsers");

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


        Log.d(TAG, response_list_obj.toString() + "    " + updateCountObj.toString());
        docDetermineLevel.update(LoginActivity.loggedUserName, response_list_obj);
        docRef.update(DetermineQuestionType.SURVEY_COUNT, updateCountObj);

        if (CreateAccount.first_survey) {
            // Create document of messages to new User to Mark
            Map<String, Object> newUser = new HashMap<>();
            List<String> msg = new ArrayList<String>() {{
                add("Hello, this message box is a place where you can interact with Mark if you have any questions regarding GUM!");
            }};
            newUser.put("messages", msg);
            Log.d(TAG, String.valueOf(newUser));

            collectionReference.document(LoginActivity.loggedUserName.toLowerCase()).set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                Map.Entry pair = (Map.Entry) it.next();
                                if (pair.getKey().toString().equals("user_names")) {
                                    userList = (ArrayList<String>) document.get("user_names");
                                }
                                it.remove(); // avoids a ConcurrentModificationException
                            }
                            userList.add(LoginActivity.loggedUserName);
                            Log.d(TAG, userList.toString());
                            Object obj = userList;
                            userDoc.update("user_names", obj);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            // determines level
            int advance_lvl = Integer.parseInt(DetermineQuestionType.levelList.get(0));
            int intermediate_lvl = Integer.parseInt(DetermineQuestionType.levelList.get(1));
            String level = "";
            if (DetermineQuestionType.survey_score >= advance_lvl) {
                level = "Advance";
                level_user = "Vigorous";
            } else if (DetermineQuestionType.survey_score >= intermediate_lvl) {
                level = "Intermediate";
                level_user = "Moderate";
            } else {
                level = "Beginner";
                level_user = "Easy";
            }
            Object score_obj = DetermineQuestionType.survey_score;
            db.collection("Users").document(LoginActivity.loggedUserName).update("Score", score_obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                }
            });
            Log.d(TAG, level);
            Object level_obj = level;
            db.collection("Users").document(LoginActivity.loggedUserName).update("FitnessLvl", level_obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
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
                    if (CreateAccount.first_survey) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadSurvey.this);
                        builder.setTitle("Level Determined");
                        builder.setMessage("You've been place in level " + level_user + ". If you feel you want a change you can change your level whenever using the toolbar.");
                        builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(UploadSurvey.this, SelectSchedule.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alert11 = builder.create();
                        alert11.show();
                    }if(!CreateAccount.first_survey) {
                        usersDoneSurvey();
                    }
                }
            }
        });

    }

    // adds user in a done survey to make sure they've completed the survey and prevent them from adding more data
    private void usersDoneSurvey(){
        usersSurveyDone.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Log.d(TAG, data.toString());
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            if (pair.getKey().toString().equals("users_finished")) {
                                surveyComplete = (ArrayList<String>) document.get("users_finished");
                            }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        surveyComplete.add(LoginActivity.loggedUserName);
                        Object sur_obj = surveyComplete;
                        usersSurveyDone.update("users_finished", sur_obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UploadSurvey.this, "Survey is uploaded, Thanks for responding!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UploadSurvey.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        });
    }
}
