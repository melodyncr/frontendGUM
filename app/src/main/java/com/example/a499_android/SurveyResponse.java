package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.a499_android.DetermineQuestionType.question_count;
import static com.example.a499_android.DetermineQuestionType.weeklyQuestionsList;
import static com.example.a499_android.DetermineQuestionType.responseList;
import static com.example.a499_android.DetermineQuestionType.response_count;



public class SurveyResponse extends AppCompatActivity {
    public static String TAG = "Weekly Survey";
    TextView typeSurvey, questionText;
    EditText responseText;
    Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        setObjects();


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(responseText.getText().toString().equals("")){
                    Toast.makeText(SurveyResponse.this, "Please enter some text", Toast.LENGTH_SHORT).show();
                }else {
                    if (question_count >= weeklyQuestionsList.size() - 1) {
                        responseList.add(responseText.getText().toString());
                        Intent intent = new Intent(SurveyResponse.this, UploadSurvey.class);
                        startActivity(intent);
                    } else {
                        Log.d(TAG,  "response text"+ responseText.getText().toString());
                        responseList.add(responseText.getText().toString());
                        question_count++;
                        response_count++;
                        Intent intent;
                        if (weeklyQuestionsList.get(question_count).charAt(0) == 'R') {
                            intent = new Intent(SurveyResponse.this, SurveyResponse.class);
                        } else if(weeklyQuestionsList.get(question_count).charAt(0) == 'O'){
                            intent = new Intent(SurveyResponse.this, SurveyOneTen.class);
                        }
                        else {
                            intent = new Intent(SurveyResponse.this, SurveyMultiC.class);
                        }
                        startActivity(intent);
                    }
                }
            }
        });

    }
    
    void setObjects(){
        typeSurvey = findViewById(R.id.typeSurvey);
        questionText = findViewById(R.id.questionText);
        responseText = findViewById(R.id.responseText);
        submitBtn = findViewById(R.id.submitBtn);
        if(question_count ==0 && CreateAccount.first_survey){
            typeSurvey.setText("Thank you so much for helping us gather information on how the GUM program is working for you. This is not a test, there are no right or wrong answers.");
        }else if(question_count ==0){
            typeSurvey.setText("Thank you so much for continuing to help us gather information on how well the GUM program works for you. There are no right or wrong answers.");
        }
        else{
            typeSurvey.setText("" + DetermineQuestionType.T_TYPE_SURVEY);
        }
        String question = weeklyQuestionsList.get(question_count).substring(1);
        questionText.setText(question);
        if(question_count >= weeklyQuestionsList.size()-1){
            submitBtn.setText("Submit Survey");
        }
    }
}
