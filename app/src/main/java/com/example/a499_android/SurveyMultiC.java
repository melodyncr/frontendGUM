package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.a499_android.DetermineQuestionType.weeklyQuestionsList;
import static com.example.a499_android.DetermineQuestionType.choicesList;
import static com.example.a499_android.DetermineQuestionType.responseList;
import static com.example.a499_android.DetermineQuestionType.getResponseList;
import static com.example.a499_android.DetermineQuestionType.response_count;
import static com.example.a499_android.DetermineQuestionType.question_count;
public class SurveyMultiC extends AppCompatActivity {
    ArrayList<String> tempChoiceList = new ArrayList<>();
    TextView typeSurvey, questionText;
    RadioGroup radioGroup;
    RadioButton b1, b2, b3, b4;
    Button  submitBtn, previousBtn;
    public String TAG = "SubmitMC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_multiple_choice);
        setChoices(choicesList.get(question_count));
        Log.d(TAG, tempChoiceList.toString());
        setObjects();
        radioGroup.clearCheck();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question_count >= weeklyQuestionsList.size() - 1) {
                    boolean checked = isChecked();
                    if(checked){
                        Intent intent = new Intent(SurveyMultiC.this, UploadSurvey.class);
                        startActivity(intent);
                        Log.d(TAG, "Finsihed survey answers " + responseList);
                    }else {
                        //do nothing, since a button wasn't checked
                    }
                } else {
                    response_count++;
                    //question_count++;
                    boolean checked = isChecked();
                    question_count++;
                    if(checked) {
                        Intent intent;
                        if (weeklyQuestionsList.get(question_count).charAt(0) == 'M') {
                            intent = new Intent(SurveyMultiC.this, SurveyMultiC.class);
                        }
                        else if (weeklyQuestionsList.get(question_count).charAt(0) == 'O') {
                            intent = new Intent(SurveyMultiC.this, SurveyOneTen.class);
                        } else {
                            intent = new Intent(SurveyMultiC.this, SurveyResponse.class);
                        }
                        startActivity(intent);
                    }else{
                        //do nothing, since a button wasn't checked
                    }
                }
            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question_count == 0) {
                    Toast.makeText(SurveyMultiC.this, "No previous questions", Toast.LENGTH_SHORT).show();
                } else {
                    question_count--;
                    response_count--;
                    responseList.remove(response_count);
                    Intent intent;
                    if (weeklyQuestionsList.get(question_count).charAt(0) == 'M') {
                        intent = new Intent(SurveyMultiC.this, SurveyMultiC.class);
                    }
                    else if (weeklyQuestionsList.get(question_count).charAt(0) == 'O') {
                        intent = new Intent(SurveyMultiC.this, SurveyOneTen.class);
                    }
                    else {
                        intent = new Intent(SurveyMultiC.this, SurveyResponse.class);
                    }
                    startActivity(intent);
                }
            }
        });
    }

    void setChoices(String choices) {
        int last_comma = 0;
        String substr = "";
        for (int i = 0; i < choices.length(); i++) {
            if (choices.charAt(i) == ',') {
                substr = choices.substring(last_comma, i);
                last_comma = i+1;
                tempChoiceList.add(substr);
            }
        }
    }

    void setObjects() {
        typeSurvey = findViewById(R.id.typeSurvey);
        questionText = findViewById(R.id.questionText);
        radioGroup = findViewById(R.id.radioQuestionsGroup);
        submitBtn = findViewById(R.id.submitBtn);
        previousBtn = findViewById(R.id.previousBtn);
        typeSurvey.setText(""+DetermineQuestionType.T_TYPE_SURVEY);
        b1 = findViewById(R.id.question1);
        b2 = findViewById(R.id.question2);
        b3 = findViewById(R.id.question3);
        b4 = findViewById(R.id.question4);

        String question = weeklyQuestionsList.get(question_count).substring(1);
        questionText.setText(question);
        if (question_count >= weeklyQuestionsList.size() - 1) {
            submitBtn.setText("Submit Survey");
        }
        Log.d(TAG, "choices >.." + tempChoiceList.toString() + "\n" + tempChoiceList.size());
        b1.setText(""+tempChoiceList.get(0));
        b2.setText(""+tempChoiceList.get(1));
        b3.setVisibility(View.INVISIBLE);
        b4.setVisibility(View.INVISIBLE);
        if(tempChoiceList.size() == 3){
            b3.setText(""+tempChoiceList.get(2));
            b3.setVisibility(View.VISIBLE);
        }else if(tempChoiceList.size()== 4){
            b3.setText(""+tempChoiceList.get(2));
            b4.setText(""+tempChoiceList.get(3));
            b3.setVisibility(View.VISIBLE);
            b4.setVisibility(View.VISIBLE);
        }
    }

    boolean isChecked() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(SurveyMultiC.this, "No answer has been selected", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
            responseList.add(radioButton.getText().toString());
            int i =0;
            for(String s : tempChoiceList){
                if(s.equals(radioButton.getText().toString())){
                    break;
                }
                i++;
            }
            if(CreateAccount.first_survey){
                determineScore(DetermineQuestionType.fitnessLevelScoreList.get(question_count), i);
            }
            recordAnswer(i, getResponseList.get(question_count));
            return true;
        }
    }

    void recordAnswer(int index,String choices){
        int last_comma = 0;
        String substr = "";
        Log.d(TAG, choices + "choices");
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < choices.length(); i++) {
            if (choices.charAt(i) == ',') {
                substr = choices.substring(last_comma, i);
                last_comma = i + 1;
                list.add(Integer.parseInt(substr));
            }
        }
            int value = list.get(index) +1;
            Log.d(TAG,"value  "+value);
            list.set(index,value);
            String newCount ="";
            for(int j =0; j < list.size();j++){
                newCount += list.get(j).toString() + ",";
            }
            getResponseList.set(question_count, newCount );
    }
    void determineScore(String score_str, int index){
        int last_comma = 0;
        String substr = "";
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < score_str.length(); i++) {
            if (score_str.charAt(i) == ',') {
                substr = score_str.substring(last_comma, i);
                last_comma = i + 1;
                list.add(Integer.parseInt(substr));
            }
        }
        int temp_score = list.get(index);
        DetermineQuestionType.survey_score = DetermineQuestionType.survey_score + temp_score;
        Log.d(TAG, ""+ DetermineQuestionType.survey_score + " right now...");
    }
}
