package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ViewResponseO extends AppCompatActivity {

    TextView txtView, txtView2, txtView3, txtView4, txtView5,txtView6,txtView7,txtView8,txtView9,txtView10,responseTxt;
    Button backBtn;
    Button nextBtn;
    private Handler hdlr = new Handler();
    private int max_num = 0;
    private String TAG = "View Response O";
    private ArrayList<String> responseList = GetResponseListQuery.listSelected_responses;
    private ArrayList<String> questionsList = GetResponseListQuery.listSelected_questions;
    ArrayList<TextView> txtViewList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_response_scale);
        backBtn = findViewById(R.id.backBtnChartO);
        nextBtn = findViewById(R.id.nextBtnChartO);
        ArrayList<Integer> responseListInt = returnInts(responseList.get(GetResponseListQuery.index_charts));
        txtView = findViewById(R.id.textView1O);
        txtView2 = findViewById(R.id.textView2O);
        txtView3 = findViewById(R.id.textView3O);
        txtView4 = findViewById(R.id.textView4O);
        txtView5 = findViewById(R.id.textView5O);
        txtView6 = findViewById(R.id.textView6O);
        txtView7 = findViewById(R.id.textView7O);
        txtView8 = findViewById(R.id.textView8O);
        txtView9 = findViewById(R.id.textView9O);
        txtView10 = findViewById(R.id.textView10O);
        String sub_str = questionsList.get(GetResponseListQuery.index_charts).substring(3);
        responseTxt = findViewById(R.id.responseTextO);
        responseTxt.setText(sub_str);
        txtView8.setVisibility(View.INVISIBLE);
        txtView9.setVisibility(View.INVISIBLE);
        txtView10.setVisibility(View.INVISIBLE);

        txtViewList.add(txtView);
        txtViewList.add(txtView2);
        txtViewList.add(txtView3);
        txtViewList.add(txtView4);
        txtViewList.add(txtView5);
        txtViewList.add(txtView6);
        txtViewList.add(txtView7);

        if(responseListInt.size()==10){
            txtViewList.add(txtView8);
            txtViewList.add(txtView9);
            txtViewList.add(txtView10);
            txtView8.setVisibility(View.VISIBLE);
            txtView9.setVisibility(View.VISIBLE);
            txtView10.setVisibility(View.VISIBLE);
        }

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
                        intent = new Intent(ViewResponseO.this, ViewResponseR.class);
                    } else if(sub_str_check == 'O'){
                        intent = new Intent(ViewResponseO.this, ViewResponseO.class);
                    }
                    else {
                        intent = new Intent(ViewResponseO.this, ViewResponses.class);
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
                        intent = new Intent(ViewResponseO.this, ViewResponseR.class);
                    } else if(sub_str_check == 'O'){
                        intent = new Intent(ViewResponseO.this, ViewResponseO.class);
                    }else {
                        intent = new Intent(ViewResponseO.this, ViewResponses.class);
                    }
                    startActivity(intent);
                }
            }
        });
        for(int k =0; k < txtViewList.size();k++){
            setPercentage(responseListInt.get(k),txtViewList.get(k),k+1,0);
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

    private void setPercentage( int max_count, TextView txtView_temp,int option,int zero){
        Log.d(TAG, max_count + " count and max " + max_num);
        new Thread(new Runnable() {
            public void run() {
                int z = zero;
                while (z < max_count) {
                    z++;
                    //double n = max_count;
                    double d = max_num;
                    double p = (z/ d) * 100.0;
                    String percent_long = Double.toString(p);
                    String percent = percent_long.substring(0,3);
                    // Update the progress bar and display the current value in text view
                    hdlr.post(new Runnable() {
                        public void run() {
                            txtView_temp.setText(percent+"%\n"+option);
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


}
