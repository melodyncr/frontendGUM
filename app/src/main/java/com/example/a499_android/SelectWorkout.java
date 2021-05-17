package com.example.a499_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a499_android.utility.PopupRecyclerViewAdapter;

import java.util.HashMap;

public class SelectWorkout extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static String selectedWorkout = "none";
    public static String difficulty = "none";
    String data[];
    public static boolean interval = false;
    Spinner spinner;
    public String level = "";
    public static long time_mil = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_workout);
        ActionBar actionBar = getSupportActionBar();
        Resources res = getResources();

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        spinner = findViewById(R.id.spinnerTime);
        spinner.setOnItemSelectedListener(this);
        String[] levels = new String[]{"Select a Time(Default 20 seconds)","30 seconds", "1 minute","2 minutes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.INVISIBLE);

        Button startBtn = findViewById(R.id.start_workout);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedWorkout.equals("none")){
                    Toast.makeText(SelectWorkout.this, "Please Select an Exercise", Toast.LENGTH_SHORT).show();
                }else if(interval){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SelectWorkout.this);
                    builder1.setTitle("Before you begin:");
                    builder1.setMessage("This is an interval exercise so it requires constant movement throughout the interval. If GUM doesn't detect motion you will be politely asked to move and a vibration will occur as well. Also since wer are testing this, these don't count for longevity points yet, these workouts are for your own benefit!");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Got it!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Intent intent = new Intent(SelectWorkout.this, StartExerciseM.class);
                                    startActivity(intent);
                                }
                            });
                    builder1.setNegativeButton("No thanks maybe later!.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }else {
                    Intent intent = new Intent(SelectWorkout.this, StartExercise.class);
                    startActivity(intent);
                }
            }
        });
        Button easyBtn = findViewById(R.id.easy_button);
        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty = "easy";
                interval = false;
                spinner.setVisibility(View.INVISIBLE);
                data = res.getStringArray(R.array.easy_workouts);
                showPopup();
            }
        });
        Button mediumBtn = findViewById(R.id.medium_button);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty = "medium";
                interval = false;
                spinner.setVisibility(View.INVISIBLE);
                data = res.getStringArray(R.array.medium_workouts);
                showPopup();
            }
        });
        Button hardBtn = findViewById(R.id.hard_button);
        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty = "hard";
                interval = false;
                spinner.setVisibility(View.INVISIBLE);
                data = res.getStringArray(R.array.hard_workouts);
                showPopup();
             }
        });

        Button intervalBtn = findViewById(R.id.interval_button);
        intervalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interval = true;
                spinner.setVisibility(View.VISIBLE);
                data = res.getStringArray(R.array.easy_workouts);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        level = parent.getItemAtPosition(position).toString();
        Log.d("String", level);
        if(level.equals("Select a Time(Default 20 seconds)")) {
            time_mil = 20000;
        }else {
            if (level.equals("30 seconds")) {
                time_mil = 30000;
            }
            if (level.equals("1 minute")) {
                time_mil = 60000;
            }
            if (level.equals("2 minutes")) {
                time_mil = 120000;
            }
        }
        Log.d("String", ""+ time_mil);

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
