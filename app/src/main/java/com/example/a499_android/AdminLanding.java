package com.example.a499_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminLanding extends AppCompatActivity {

    CardView addEditTidbits, viewResponsesBtn, addSurveyBtn,addVideosBtn;
    ActionBar actionBar;
    public static ArrayList<String> w_survey_count_list = new ArrayList<>();
    public static ArrayList<String> w_survey_questions_list = new ArrayList<>();
    public static ArrayList<String> w_survey_responses_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);
        clearLists();
        actionBar = getSupportActionBar();
        actionBar.setTitle("Admin Landing Page");
        actionBar.setDisplayHomeAsUpEnabled(true);
        wiredUp();

        addEditTidbits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLanding.this, Tidbits.class));
            }
        });

        viewResponsesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLanding.this, LoadPreviousSurvey.class));
            }
        });

        addSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLanding.this, AddSurveyQuestionsCount.class ));
            }
        });

        addVideosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLanding.this, AddVideos.class ));

            }
        });

    }

    private void wiredUp() {
        addEditTidbits = findViewById(R.id.addEditTidbits);
        viewResponsesBtn = findViewById(R.id.viewResponsesBtn);
        addSurveyBtn = findViewById(R.id.addSurveyBtn);
        addVideosBtn = findViewById(R.id.addVideosBtn);
    }
    private void clearLists(){
        LoadPreviousSurvey.w_survey_list_names.clear();
        LoadPreviousSurvey.previous_times_list.clear();
        GetResponseListQuery.listSelected_questions.clear();
        GetResponseListQuery.listSelected_questions_c.clear();
        GetResponseListQuery.listSelected_responses.clear();
        SelectASurvey.survey_list_names.clear();
        SelectASurvey.past_survey= false;
        GetResponseListQuery.index_charts = 0;
        AddSurveyQuestionsCount.count =0;
        w_survey_questions_list.clear();
        w_survey_responses_list.clear();
        w_survey_count_list.clear();
    }
}