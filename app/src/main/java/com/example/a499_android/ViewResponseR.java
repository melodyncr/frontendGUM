package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ViewResponseR extends AppCompatActivity {
    Button backBtn;
    Button nextBtn;
    private ArrayList<String> responseList = GetResponseListQuery.listSelected_responses;
    private ArrayList<String> questionsList = GetResponseListQuery.listSelected_questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_response_2);
        backBtn = findViewById(R.id.backBtnChartR);
        nextBtn = findViewById(R.id.nextBtnChartR);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GetResponseListQuery.index_charts == 0){
                    Snackbar.make(findViewById(android.R.id.content),  "This is the first question of the Survey!", Snackbar.LENGTH_LONG).show();
                }else {
                    GetResponseListQuery.index_charts--;
                    char sub_str_check = questionsList.get(GetResponseListQuery.index_charts).charAt(0);
                    Intent intent;
                    if (sub_str_check == 'R') {
                        intent = new Intent(ViewResponseR.this, ViewResponseR.class);
                    } else {
                        intent = new Intent(ViewResponseR.this, ViewResponses.class);
                    }
                    startActivity(intent);
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GetResponseListQuery.index_charts == responseList.size()-1){
                    Snackbar.make(findViewById(android.R.id.content),  "This is the last question of the Survey!", Snackbar.LENGTH_LONG).show();
                }else {
                    GetResponseListQuery.index_charts++;
                    char sub_str_check = questionsList.get(GetResponseListQuery.index_charts).charAt(0);
                    Intent intent;
                    if (sub_str_check == 'R') {
                        intent = new Intent(ViewResponseR.this, ViewResponseR.class);
                    } else {
                        intent = new Intent(ViewResponseR.this, ViewResponses.class);
                    }
                    startActivity(intent);
                }
            }
        });


    }
}
