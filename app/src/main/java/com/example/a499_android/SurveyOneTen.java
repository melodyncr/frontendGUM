package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.a499_android.DetermineQuestionType.choicesList;
import static com.example.a499_android.DetermineQuestionType.getResponseList;
import static com.example.a499_android.DetermineQuestionType.question_count;
import static com.example.a499_android.DetermineQuestionType.responseList;
import static com.example.a499_android.DetermineQuestionType.response_count;
import static com.example.a499_android.DetermineQuestionType.weeklyQuestionsList;

public class SurveyOneTen extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static String TAG = "Weekly Survey";
    TextView typeSurvey, questionText;
    Button backBtn, submitBtn;
    public String item = "";
    ArrayList<String> tempChoiceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_one_ten);
        setChoices(choicesList.get(question_count));


        Spinner spinner = (Spinner) findViewById(R.id.spinner1);

        spinner.setOnItemSelectedListener(this);
        String[] numbers;
        String seven_or_ten = weeklyQuestionsList.get(question_count).substring(1,3);
        Log.d(TAG, seven_or_ten);
        if(seven_or_ten.equals("10")){
            numbers = new String[]{"Select a Level","1", "2", "3","4","5","6","7","8","9","10"};
        }else{
            numbers = new String[]{"Select a Level","1", "2", "3","4","5","6","7"};
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setObjects();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item.equals("Select a Level")){
                    Toast.makeText(SurveyOneTen.this, "Please select a level.", Toast.LENGTH_SHORT).show();
                }else {
                    if (question_count >= weeklyQuestionsList.size() - 1) {
                        responseList.add(item);
                        isChecked();
                        Intent intent = new Intent(SurveyOneTen.this, UploadSurvey.class);
                        startActivity(intent);
                    } else {
                        Log.d(TAG,  "response text"+ item);
                        responseList.add(item);
                        isChecked();
                        question_count++;
                        response_count++;
                        Intent intent;
                        if (weeklyQuestionsList.get(question_count).charAt(0) == 'R') {
                            intent = new Intent(SurveyOneTen.this, SurveyResponse.class);
                        } else if (weeklyQuestionsList.get(question_count).charAt(0) == 'O'){
                            intent = new Intent(SurveyOneTen.this, SurveyOneTen.class);
                        } else {
                            intent = new Intent(SurveyOneTen.this, SurveyMultiC.class);
                        }
                        startActivity(intent);
                    }
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(question_count == 0){
                    Toast.makeText(SurveyOneTen.this, "No previous questions", Toast.LENGTH_SHORT).show();
                }else{
                    question_count--;
                    response_count--;
                    responseList.remove(response_count);
                    Intent intent;
                    if(weeklyQuestionsList.get(question_count).charAt(0) == 'R'){
                        intent = new Intent(SurveyOneTen.this, SurveyResponse.class);
                    }else if (weeklyQuestionsList.get(question_count).charAt(0) == 'O'){
                        intent = new Intent(SurveyOneTen.this, SurveyOneTen.class);
                    }
                    else{
                        intent = new Intent(SurveyOneTen.this, SurveyMultiC.class);
                    }
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
         item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    void setObjects(){
        typeSurvey = findViewById(R.id.typeSurvey);
        questionText = findViewById(R.id.questionText);
        backBtn = findViewById(R.id.previousBtn);
        submitBtn = findViewById(R.id.submitBtn);
        if(question_count ==0 && CreateAccount.first_survey){
            typeSurvey.setText("Thank you so much for helping us gather information on how the GUM program is working for you. This is not a test, there are no right or wrong answers.");
        }else if(question_count ==0){
            typeSurvey.setText("Thank you so much for continuing to help us gather information on how well the GUM program works for you. There are no right or wrong answers.");
        }
        else{
            typeSurvey.setText("" + DetermineQuestionType.T_TYPE_SURVEY);
        }
        String question = weeklyQuestionsList.get(question_count).substring(3);
        questionText.setText(question);
        if(question_count >= weeklyQuestionsList.size()-1){
            submitBtn.setText("Submit Survey");
        }
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

    void isChecked() {

            //responseList.add(radioButton.getText().toString());
            int i = Integer.parseInt(item) -1;
            if(CreateAccount.first_survey){
                determineScore(DetermineQuestionType.fitnessLevelScoreList.get(question_count), i);
            }
            recordAnswer(i, getResponseList.get(question_count));
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
