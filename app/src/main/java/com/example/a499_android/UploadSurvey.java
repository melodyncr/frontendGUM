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



import static com.example.a499_android.LoginActivity.loggedUserName;
import static com.example.a499_android.DetermineQuestionType.responseList;
import static com.example.a499_android.DetermineQuestionType.getResponseList;
public class UploadSurvey extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Select Schedule";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_survey);


        Object response_list_obj = responseList;
        Object updateCountObj = getResponseList;
        DocumentReference docRef = db.collection("Surveys").document(DetermineQuestionType.SURVEYR);
        Log.d(TAG, response_list_obj.toString()  + "    " + updateCountObj.toString());
        docRef.update(loggedUserName, response_list_obj);
        docRef.update(DetermineQuestionType.SURVEY_COUNT, updateCountObj);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    responseList.clear();
                    getResponseList.clear();
                    Log.d(TAG, response_list_obj.toString() + " " + getResponseList.toString() + "cleared list is success.");
                    Toast.makeText(UploadSurvey.this, "Survey is uploaded!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadSurvey.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(UploadSurvey.this, "Survey is is not uploaded!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadSurvey.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
