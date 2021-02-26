package com.example.a499_android;

import android.app.AppComponentFactory;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SelectSchedule extends AppCompatActivity {
    TimePicker picker;
    Button btnGet;
    TextView tvw;
    TextView schedule;
    ArrayList<String> times = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_schedule);
        tvw=(TextView)findViewById(R.id.time_view1);
        picker=(TimePicker)findViewById(R.id.timePicker1);
        schedule=(TextView)findViewById(R.id.schedule_list);
        picker.setIs24HourView(false);
        btnGet=(Button)findViewById(R.id.selectTimeBtn);


        btnGet.setOnClickListener(new View.OnClickListener() {
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
                    } else {
                        am_pm = "AM";
                    }
                    if (times.isEmpty()) {
                        times.add("Schedule for the week");
                    }
                    tvw.setText("Selected Date: " + hour + ":" + minute + " " + am_pm);
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


    }

}
