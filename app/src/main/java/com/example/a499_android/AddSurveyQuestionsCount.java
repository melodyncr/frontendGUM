package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class AddSurveyQuestionsCount extends AppCompatActivity {
    public static int questionCount;
    public static int count = 0;
    EditText num_questions;
    Button beginSurvey;
    String TAG = "Add Survey Questions Count";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_survey_questions_count);
        num_questions = findViewById(R.id.numOfQuestions);
        beginSurvey = findViewById(R.id.beginSurveyBtn);

        beginSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num_questions.getText().toString().equals("")){
                    Snackbar.make(findViewById(android.R.id.content),  "Enter the number of questions before proceeding!", Snackbar.LENGTH_LONG).show();
                }
                try {
                    questionCount = Integer.parseInt(num_questions.getText().toString());
                    Log.d(TAG, "count " + questionCount);
                    startActivity(new Intent(AddSurveyQuestionsCount.this, AddSurveyQuestions.class));
                }catch(Exception e){
                    Snackbar.make(findViewById(android.R.id.content),  "Enter a valid number!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
