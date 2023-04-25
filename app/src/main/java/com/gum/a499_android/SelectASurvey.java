package com.gum.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SelectASurvey extends AppCompatActivity {
    public static ArrayList<String> survey_list_names = LoadPreviousSurvey.w_survey_list_names;
    Button fSurveyBtn, wSurveyBtn;

    //first four are for previous surveys
    public static String document_responses_str = "";
    public static String document_questions_str = "";
    public static String document_questions_answers = "";
    public static String field_name_response = "";
    public static String field_name_questions = "";
    public static String field_name_questions_list= "";
    public static String document_response = "";
    public static boolean past_survey = false;

    public String TAG = "Select A Survey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_a_survey);
        fSurveyBtn = findViewById(R.id.firstSurveyBtn);
        wSurveyBtn = findViewById(R.id.weeklySurveyBtn);

        fSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                F_or_W("F");
            }
        });
        wSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                F_or_W("W");
            }
        });

        RecyclerView rv = findViewById(R.id.old_survey_names_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new SelectASurvey.Adapter());
    }
    // is this first or the weekly survey we will determine this so we can use the correct names to reference the right collection name
    public void F_or_W(String f_or_w){
        if(f_or_w.equals("F")){
            document_responses_str = "FSurveyQ";
            field_name_response = "f_survey_count";
            field_name_questions = "f_survey_q";
            field_name_questions_list = "f_survey_qc";
            document_response = "FSurveyR";
        }else{
            document_responses_str = "WSurveyQ";
            field_name_response = "w_survey_count";
            field_name_questions = "w_survey_q";
            field_name_questions_list = "w_survey_qc";
            document_response = "WSurveyR";
        }
        Intent intent = new Intent(SelectASurvey.this, GetResponseListQuery.class);
        startActivity(intent);
    }
    private class Adapter extends RecyclerView.Adapter<ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SelectASurvey.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.bind(survey_list_names.get(position), position);
        }

        @Override
        public int getItemCount() {
            return survey_list_names.size();
        }

    }
    // recycler view of previous survey, if selected we will use this survey to see the data
    private class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.survey, parent, false));
        }

        public void bind(String f, int position) {
            TextView item = itemView.findViewById(R.id.item_id);
            item.setText(f + "\n" + LoadPreviousSurvey.previous_times_list.get(position));

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    document_responses_str = "PastWSurveyR";
                    document_questions_str = "PastWSurveyQ";
                    document_questions_answers = "PastWSurveyQL";
                    field_name_response = f;
                    document_response = "";
                    past_survey = true;
                    Intent intent = new Intent(SelectASurvey.this, GetResponseListQuery.class);
                    startActivity(intent);
                    Snackbar.make(findViewById(android.R.id.content),f + "selected!", Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

}
