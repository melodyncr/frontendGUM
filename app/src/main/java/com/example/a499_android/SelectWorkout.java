package com.example.a499_android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a499_android.utility.PopupRecyclerViewAdapter;

import java.util.ArrayList;

public class SelectWorkout extends AppCompatActivity{

    public static String selectedWorkout = "none";
//    ArrayList<String> data = new ArrayList<>();
    String data[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_workout);
        ActionBar actionBar = getSupportActionBar();

        Resources res = getResources();

        Button startBtn = findViewById(R.id.start_workout);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedWorkout.equals("none")){
                    Toast.makeText(SelectWorkout.this, "Please Select an Exercise", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(SelectWorkout.this, StartExercise.class);
                    startActivity(intent);
                }
            }
        });
        Button easyBtn = findViewById(R.id.easy_button);
        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = res.getStringArray(R.array.easy_workouts);
                showPopup();
            }
        });
        Button mediumBtn = findViewById(R.id.medium_button);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = res.getStringArray(R.array.medium_workouts);
                showPopup();
            }
        });
        Button hardBtn = findViewById(R.id.hard_button);
        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = res.getStringArray(R.array.hard_workouts);
                showPopup();
             }
        });

    } // End of onCreate()

    public void showPopup(){
        final View popupView = LayoutInflater.from(SelectWorkout.this).inflate(R.layout.recycler_popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btn = (Button) popupView.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        RecyclerView recyclerView = (RecyclerView) popupView.findViewById(R.id.rv_recycler_view2);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PopupRecyclerViewAdapter adapter = new PopupRecyclerViewAdapter(SelectWorkout.this, data, popupWindow);
        recyclerView.setAdapter(adapter);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

}
