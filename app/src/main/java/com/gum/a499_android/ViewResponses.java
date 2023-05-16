package com.gum.a499_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ViewResponses extends AppCompatActivity {

    private ArrayList<String> responseList = GetResponseListQuery.listSelected_responses;
    private ArrayList<String> questionsList = GetResponseListQuery.listSelected_questions;
    private ArrayList<String> questionsListC = GetResponseListQuery.listSelected_questions_c;
    Button backBtn, nextBtn, closeBtn, a1, a2, a3, a4;
    public String TAG = "View Response";
    TextView txtView, txtView2, txtView3, txtView4, windowTxt;
    ArrayList<TextView> txtViewList = new ArrayList<>();
    private Handler hdlr = new Handler();
    private int max_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_responses);
        backBtn = findViewById(R.id.backBtnChart);
        nextBtn = findViewById(R.id.nextBtnChart);
        a1 = findViewById(R.id.question1Btn);
        a2 = findViewById(R.id.question2Btn);
        a3 = findViewById(R.id.question3Btn);
        a4 = findViewById(R.id.question4Btn);
        a3.setVisibility(View.INVISIBLE);
        a4.setVisibility(View.INVISIBLE);

        // set text lists,
        txtView = findViewById(R.id.textView1P);
        txtView2 = findViewById(R.id.textView2P);
        txtView3 = findViewById(R.id.textView3P);
        txtView4 = findViewById(R.id.textView4P);
        txtView3.setVisibility(View.INVISIBLE);
        txtView4.setVisibility(View.INVISIBLE);
        txtViewList.add(txtView);
        txtViewList.add(txtView2);


        //get integers from responselist str, then convert into a float list
        ArrayList<Integer> responseListInt = returnInts(responseList.get(GetResponseListQuery.index_charts));
        ArrayList<Float> responseListFloat = new ArrayList<>();
        for(int i = 0; i< responseListInt.size();i++){
            float int_val = responseListInt.get(i);
            responseListFloat.add(int_val);
        }
        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();

        for(int i =0; i < responseListFloat.size(); i++){
            entries.add(new BarEntry(responseListFloat.get(i), i));
        }
        String sub_str = questionsList.get(GetResponseListQuery.index_charts).substring(1);
        BarDataSet bardataset = new BarDataSet(entries, sub_str);
        ArrayList<String> questListC_index = returnStrings(questionsListC.get(GetResponseListQuery.index_charts));
        ArrayList<String> labels = new ArrayList<String>();
        for(int j = 0; j < questListC_index.size();j++){
            int j_add = j+1;
            labels.add("A"+ j_add);
        }
        if(responseListInt.size()==3){
            txtView3.setVisibility(View.VISIBLE);
            txtViewList.add(txtView3);
            a3.setVisibility(View.VISIBLE);
        }else if(responseListInt.size() == 4){
            txtView3.setVisibility(View.VISIBLE);
            txtViewList.add(txtView3);
            txtView4.setVisibility(View.VISIBLE);
            txtViewList.add(txtView4);
            a3.setVisibility(View.VISIBLE);
            a4.setVisibility(View.VISIBLE);
        }

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.setDescription("");  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(5000);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GetResponseListQuery.index_charts == responseList.size()-1){
                    Snackbar.make(findViewById(android.R.id.content),  "This is the last question of the Survey!", Snackbar.LENGTH_LONG).show();
                }else {
                    GetResponseListQuery.index_charts++;
                    char sub_str_check = questionsList.get(GetResponseListQuery.index_charts).charAt(0);
                    Intent intent;
                    if(sub_str_check == 'R'){
                        intent = new Intent(ViewResponses.this, ViewResponseR.class);
                    }else if(sub_str_check == 'O'){
                        intent = new Intent(ViewResponses.this, ViewResponseO.class);
                    }
                    else {
                        intent = new Intent(ViewResponses.this, ViewResponses.class);
                    }
                    startActivity(intent);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GetResponseListQuery.index_charts == 0){
                    Snackbar.make(findViewById(android.R.id.content),  "This is the First question of the Survey!", Snackbar.LENGTH_LONG).show();
                }else {
                    GetResponseListQuery.index_charts--;
                    char sub_str_check = questionsList.get(GetResponseListQuery.index_charts).charAt(0);
                    Intent intent;
                    if(sub_str_check == 'R'){
                        intent = new Intent(ViewResponses.this, ViewResponseR.class);
                    }else if(sub_str_check == 'O'){
                        intent = new Intent(ViewResponses.this, ViewResponseO.class);
                    }
                    else {
                        intent = new Intent(ViewResponses.this, ViewResponses.class);
                    }
                    startActivity(intent);
                }
            }
        });

        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v,questListC_index.get(0) );
            }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v,questListC_index.get(1) );
            }
        });
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v,questListC_index.get(2) );
            }
        });
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v,questListC_index.get(3) );
            }
        });

        for(int k =0; k < txtViewList.size();k++){
            setPercentage(responseListInt.get(k),txtViewList.get(k),0);
        }
    }

    private ArrayList<Integer> returnInts(String choices){
        ArrayList<Integer> responseListInt = new ArrayList<>();
        int last_comma = 0;
        String substr = "";
        for (int i = 0; i < choices.length(); i++) {
            if (choices.charAt(i) == ',') {
                substr = choices.substring(last_comma, i);
                last_comma = i + 1;
                responseListInt.add(Integer.parseInt(substr));
                max_num = Integer.parseInt(substr) + max_num;
            }
        }
        Log.d(TAG, max_num + " max number");
        return responseListInt;
    }
    private ArrayList<String> returnStrings(String choices){
        ArrayList<String> questionsList = new ArrayList<>();
        int last_comma = 0;
        String substr = "";
        for (int i = 0; i < choices.length(); i++) {
            if (choices.charAt(i) == ',') {
                substr = choices.substring(last_comma, i);
                last_comma = i + 1;
                questionsList.add((substr));
            }
        }
        return questionsList;
    }

    private void setPercentage( int max_count, TextView txtView_temp,int zero){
        Log.d(TAG, max_count + " count and max " + max_num);
        new Thread(new Runnable() {
            public void run() {
                int z = zero;
                while (z < max_count) {
                    z++;
                    //double n = max_count;
                    double d = max_num;
                    double p = (z/d) * 100.0;
                    String percent_long = Double.toString(p);
                    String percent = percent_long.substring(0,3);
                    // Update the progress bar and display the current value in text view
                    hdlr.post(new Runnable() {
                        public void run() {
                            txtView_temp.setText(percent+"%");
                        }
                    });
                    try {
                        // Sleep for 100 milliseconds to show the progress slowly.
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Log.d(TAG, "finished " + max_count);
    }

    // window will show the response text the user selected
    public void onButtonShowPopupWindowClick(View view, String question) {
        final WindowManager w = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final Display d = w.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.view_responses_window, null);

        int width = (int) (dm.widthPixels * 0.8);
        int height = (int) (dm.heightPixels * 0.5);
        Log.d("Width and height...", width + " : " + height);
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        closeBtn = popupView.findViewById(R.id.closeWindowQuestion);
        windowTxt = popupView.findViewById(R.id.questionTextSelected);
        windowTxt.setText(question);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click", "Close works");
                popupWindow.dismiss();
            }
        });
    }
}
