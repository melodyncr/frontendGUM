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
import java.util.HashMap;
import java.util.List;



public class SelectSchedule extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String TAG = "Select Schedule";
    TimePicker picker;
    Button btnSelectTime,btnUpdateSchedule, clearBtn;
    TextView schedule;
    ArrayList<String> timeList_string = new ArrayList<>();
    HashMap<Integer,Integer> timesList_int = new HashMap<Integer, Integer>();
    public static final String EXTRA = "SelectSchedule EXTRA";

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
                if (timeList_string.size() == 7) {
                    ArrayList<String> new_schedule = returnSchedule(timeList_string);
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
                if (timeList_string.size() == 7) {
                    Toast.makeText(SelectSchedule.this, "List is full", Toast.LENGTH_SHORT).show();
                } else {
                    int mil_hour, hour, minute;
                    String am_pm;
                    boolean less_than_10 = false;
                    String min_less_than_ten="";
                    if (Build.VERSION.SDK_INT >= 23) {
                        hour = picker.getHour();
                        minute = picker.getMinute();
                        mil_hour = picker.getHour();
                    } else {
                        hour = picker.getCurrentHour();
                        minute = picker.getCurrentMinute();
                        mil_hour = picker.getCurrentHour();
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
                    if (timeList_string.isEmpty()) {
                        timeList_string.add("Schedule for the week.");
                    }
                    boolean add_time = validSchedule(mil_hour,minute,timesList_int);
                    if(!add_time){
                        Toast.makeText(SelectSchedule.this, "This time is not an hour between all others!", Toast.LENGTH_SHORT).show();
                    }else {
                        Log.d(TAG, "time inserted"+ mil_hour + ": " + minute );
                        timesList_int.put(mil_hour,minute);
                        if(minute <10){
                            min_less_than_ten = "0"+minute;
                            less_than_10= true;
                        }
                        if(less_than_10){
                            timeList_string.add(""+ hour + ":"+ min_less_than_ten+ " "+ am_pm);
                        }else {
                            timeList_string.add("" + hour + ":" + minute + " " + am_pm);
                        }
                            String time = "";
                            int count = 0;
                            for (String s : timeList_string) {
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
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeList_string.clear();
                timesList_int.clear();
                schedule.setText("");
            }
        });
    }

    public ArrayList<String> returnSchedule(ArrayList<String> list){
        list.remove(list.get(0));
        return list;
    }
    public boolean validSchedule(int hour, int minute, HashMap<Integer,Integer>list){
        if(list.size() ==0){
            return true;
        }else{
            int index = list.size()-1;
            Object obj_hr = list.keySet().toArray()[index];
            Object obj_min= list.values().toArray()[index];
            int pre_hour = Integer.parseInt(obj_hr.toString());
            int pre_min = Integer.parseInt(obj_min.toString());
            Log.d(TAG, "Previous time"+ pre_hour + ": " + pre_min);
            Log.d(TAG, "Current time"+ hour + ": " + minute );
            int answer1 = hour-pre_hour;
            int answer2= minute-pre_min;
            Log.d(TAG, ""+ answer1 + " " + answer2 );
            if(hour-pre_hour > 3){
                return false;// over three hour gap between workouts
            }else if(hour - pre_hour == 3){
                if(minute - pre_min  <= 0) {
                    return true;
                }else{
                    return false;
                }
            }else if(hour - pre_hour == 1){
                if(minute - pre_min  <= 0) {
                    return false;
                }else{
                    return true;
                }
            }
            else if(hour-pre_hour <=0){
                return false;// hours not in between schedule
            }
        }
        return true;
    }
    // Intent Factory
    public static Intent getIntent(Context context, String val){
        Intent intent = new Intent(context, SelectSchedule.class);
        intent.putExtra(EXTRA, val);
        return intent;
    }
}


