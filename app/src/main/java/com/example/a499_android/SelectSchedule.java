package com.example.a499_android;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;



public class SelectSchedule extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Select Schedule";
    TimePicker picker;
    Button btnSelectTime,btnUpdateSchedule, clearBtn;
    TextView schedule;
    ArrayList<String> times = new ArrayList<>();
    public static final String EXTRA = "SelectSchedule EXTRA";
    public List<SelectSchedule> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_schedule);
        picker=(TimePicker)findViewById(R.id.timePicker1);
        schedule=(TextView)findViewById(R.id.schedule_list);
        clearBtn = findViewById(R.id.clearSchedule);
        picker.setIs24HourView(false);
        btnSelectTime=(Button)findViewById(R.id.selectTimeBtn);
        btnUpdateSchedule= findViewById(R.id.confirmSchedule);


        DocumentReference docRef = db.collection("Users").document("raul_676");

        // Will upload/update schedule when
        btnUpdateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (times.size() == 7) {
                    ArrayList<String> new_schedule = returnSchedule(times);
                    Log.d(TAG, new_schedule.toString());
                    Object schedule_obj = new_schedule;
                    docRef.update("Schedule", schedule_obj );

                    Toast.makeText(SelectSchedule.this, "Schedule is  Updated!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SelectSchedule.this, "Schedule is not full", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Will push time str into array list to then be updated.
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (times.size() == 7) {
                    Toast.makeText(SelectSchedule.this, "List is full", Toast.LENGTH_SHORT).show();
                } else {
                    int hour, minute;
                    String am_pm;
                    if (Build.VERSION.SDK_INT >= 23) {
                        hour = picker.getHour();
                        minute = picker.getMinute();
                    } else {
                        hour = picker.getCurrentHour();
                        minute = picker.getCurrentMinute();
                    }
                    if (hour > 12) {
                        am_pm = "PM";
                        hour = hour - 12;
                    } else if(hour == 0){
                        hour = 12;
                        am_pm = "AM";
                    }else if(hour == 12){
                        am_pm = "PM";
                        hour = 12;
                    }else{
                        am_pm = "AM";
                    }
                    if (times.isEmpty()) {
                        times.add("Schedule for the week.");
                    }
                    times.add("" + hour + ":" + minute + " " + am_pm);
                    String time = "";
                    int count = 0;
                    for (String s : times) {
                        if (count == 0) {
                            time += s + "\n";
                        } else {
                            time += "Workout " + count + " " + s + "\n";
                        }
                        count++;
                    }
                    schedule.setText("" + time);

                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                times.clear();
                schedule.setText("");
            }
        });
    }

    public ArrayList<String> returnSchedule(ArrayList<String> list){
        ArrayList<String> updated_list  = new ArrayList<>();
        for(String s : list){
            if (s.equals("Schedule for the week")) {//do not add this
            }else{
                updated_list.add(s);
            }
        }
        return updated_list;
    }
    // Intent Factory
    public static Intent getIntent(Context context, String val){
        Intent intent = new Intent(context, SelectSchedule.class);
        intent.putExtra(EXTRA, val);
        return intent;
    }
}


