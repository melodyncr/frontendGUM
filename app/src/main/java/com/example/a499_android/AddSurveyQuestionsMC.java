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

import java.util.ArrayList;

public class AddSurveyQuestionsMC  extends AppCompatActivity {
    Button addAnswer, nextQuestion;
    EditText answersText;
    TextView list_answers;
    String TAG = "Add Survey MC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_survey_mc_question);
        addAnswer = findViewById(R.id.addResponseBtn);
        nextQuestion = findViewById(R.id.submitBtnA);
        answersText = findViewById(R.id.addAnswersEditTxt);
        list_answers = findViewById(R.id.listAnswers);
        ArrayList<String> answersList = new ArrayList<>();
        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answersText.getText().toString().equals("")){
                    Toast.makeText(AddSurveyQuestionsMC.this, "Add some text!", Toast.LENGTH_SHORT).show();
                }else {
                    if (answersList.size() < 4) {
                        answersList.add(answersText.getText().toString());
                        String text = "";
                        for (String s : answersList) {
                            text = text + s + " \n";
                        }
                        list_answers.setText(text);
                    }else{
                        Toast.makeText(AddSurveyQuestionsMC.this, "List is full!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answersList.size() >=2 && answersList.size() <=4){
                    String responses = returnStr(answersList);
                    if(answersList.size() == 2){
                        AddSurveyQuestionsCount.count++;
                        AdminLanding.w_survey_responses_list.add(responses);
                        AdminLanding.w_survey_count_list.add("0,0,");
                        if(AddSurveyQuestionsCount.count == AddSurveyQuestionsCount.questionCount) {
                            startActivity(new Intent(AddSurveyQuestionsMC.this, PreviewNewSurvey.class));
                        }else{
                            startActivity(new Intent(AddSurveyQuestionsMC.this, AddSurveyQuestions.class));
                        }
                    }else if(answersList.size()==3){
                        AddSurveyQuestionsCount.count++;
                        AdminLanding.w_survey_responses_list.add(responses);
                        AdminLanding.w_survey_count_list.add("0,0,0,");
                        if(AddSurveyQuestionsCount.count == AddSurveyQuestionsCount.questionCount){
                            startActivity(new Intent(AddSurveyQuestionsMC.this, PreviewNewSurvey.class));
                        }else{
                            startActivity(new Intent(AddSurveyQuestionsMC.this, AddSurveyQuestions.class));
                        }
                    }else{
                        AddSurveyQuestionsCount.count++;
                        AdminLanding.w_survey_responses_list.add(responses);
                        AdminLanding.w_survey_count_list.add("0,0,0,0,");
                        if(AddSurveyQuestionsCount.count == AddSurveyQuestionsCount.questionCount){
                            startActivity(new Intent(AddSurveyQuestionsMC.this, PreviewNewSurvey.class));
                        }else{
                            startActivity(new Intent(AddSurveyQuestionsMC.this, AddSurveyQuestions.class));
                        }

                    }
                }
            }
        });

    }

    public String returnStr(ArrayList<String> list){
        String str = "";
        for(int i =0; i < list.size(); i++){
            str = str + list.get(i) + ",";
        }
        Log.d(TAG, str);
        return str;
    }
}
