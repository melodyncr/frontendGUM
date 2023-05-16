package com.gum.a499_android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class UpdateSchedule extends AppCompatActivity {
    private final String TAG = "Update Schedule";
    Button updateTimeBtn, wBtn1, wBtn2, wBtn3, wBtn4, wBtn5, wBtn6;
    Button selectedTimeButton;
    TimePicker picker;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> times_list  = new ArrayList<>();
    int index_to_update;// this index will indicate which time will be changed;

    public static int notificationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule);
        setButtons();

        updateTimeBtn.setVisibility(View.INVISIBLE);

        DocumentReference docRef = db.collection("Users").document(LoginActivity.loggedUserName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals("Schedule")){ times_list = (ArrayList<String>) document.get("Schedule"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        // Gets the value of selected time and setss the buttons to that selected time
                        wBtn1.setText(times_list.get(0));
                        wBtn2.setText(times_list.get(1));
                        wBtn3.setText(times_list.get(2));
                        wBtn4.setText(times_list.get(3));
                        wBtn5.setText(times_list.get(4));
                        wBtn6.setText(times_list.get(5));

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //All these buttons when pressed will be highlighted to indicate which button time will be changed
        wBtn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        selectedTimeButton = wBtn1;
                        updateTimeBtn.setVisibility(View.VISIBLE);
                        highlightButtons(1);
                        index_to_update = 0;
                        notificationID = 100;
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    Log.d(TAG, "Data not loaded such document");
                    return false;
                }
            }
        });
        wBtn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        selectedTimeButton = wBtn2;
                        updateTimeBtn.setVisibility(View.VISIBLE);
                        highlightButtons(2);
                        index_to_update = 1;
                        notificationID = 200;
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    Log.d(TAG, "Data not loaded such document");
                    return false;
                }
            }
        });
        wBtn3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        selectedTimeButton = wBtn3;
                        updateTimeBtn.setVisibility(View.VISIBLE);
                        highlightButtons(3);
                        index_to_update = 2;
                        notificationID = 300;
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    Log.d(TAG, "Data not loaded such document");
                    return false;
                }
            }
        });
        wBtn4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        selectedTimeButton = wBtn4;
                        updateTimeBtn.setVisibility(View.VISIBLE);
                        highlightButtons(4);
                        index_to_update = 3;
                        notificationID = 400;
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    Log.d(TAG, "Data not loaded such document");
                    return false;
                }
            }
        });
        wBtn5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        selectedTimeButton = wBtn5;
                        updateTimeBtn.setVisibility(View.VISIBLE);
                        highlightButtons(5);
                        index_to_update = 4;
                        notificationID = 500;
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    Log.d(TAG, "Data not loaded such document");
                    return false;
                }
            }
        });
        wBtn6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        selectedTimeButton = wBtn6;
                        updateTimeBtn.setVisibility(View.VISIBLE);
                        highlightButtons(6);
                        index_to_update = 5;
                        notificationID = 600;
                        return true;
                    }
                    return false;
                } catch (Exception e) {
                    Log.d(TAG, "Data not loaded such document");
                    return false;
                }
            }
        });

        updateTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int mil_hour, hour, minute;
                    String am_pm;
                    boolean less_than_10 = false;
                    String min_less_than_ten="";
                    String time = "";

                    if (Build.VERSION.SDK_INT >= 23) {
                        hour = picker.getHour();
                        minute = picker.getMinute();
                    } else {
                        hour = picker.getCurrentHour();
                        minute = picker.getCurrentMinute();
                    }
                    if (hour > 12) {
                        am_pm = "PM";
                        Log.d(TAG, ""+ hour);
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
                    if(minute <10){
                        min_less_than_ten = "0"+minute;
                        less_than_10= true;
                    }
                    if(less_than_10){
                         time = ""+ hour + ":"+ min_less_than_ten+ " "+ am_pm;
                    }else {
                         time = "" + hour + ":" + minute + " " + am_pm;
                    }
                    Log.d(TAG, "The time is " + time);
                boolean validTime = verifyTime(time);
                if(!validTime){
                    Toast.makeText(UpdateSchedule.this, "Times must be spaced out by at least 1 hour!", Toast.LENGTH_SHORT).show();
                }else {
                    times_list.set(index_to_update, time);

                    Collections.sort(times_list, new Comparator<String>() {
                        @Override   // override sort method to compare time strings as dates to consider am/pm
                        public int compare(String o1, String o2) {
                            try {
                                return new SimpleDateFormat("hh:mm a").parse(o1).compareTo(new SimpleDateFormat("hh:mm a").parse(o2));
                            } catch (ParseException e) {
                                return 0;
                            }
                        }
                    });

                    Object schedule_obj = times_list;
                    docRef.update("Schedule", schedule_obj);

                    wBtn1.setText(times_list.get(0));
                    wBtn2.setText(times_list.get(1));
                    wBtn3.setText(times_list.get(2));
                    wBtn4.setText(times_list.get(3));
                    wBtn5.setText(times_list.get(4));
                    wBtn6.setText(times_list.get(5));

                    //Recreate all notifications/alarms in the newly sorted order
                    for(int i = 0; i < times_list.size(); i++){
                        updateNotification(times_list, i);
                    }

                }
            }
        });
    }

    public boolean verifyTime(String time){
        int hour, curr_hour;
        int mins, curr_mins;
        String am_pm, curr_am_pm;

        if (time.length() < 8)  {   // set current selected hour
            hour = Integer.parseInt(time.substring(0, 1));
            mins = Integer.parseInt(time.substring(2, 4));
            am_pm = time.substring(5);
        } else {
            hour = Integer.parseInt(time.substring(0, 2));
            mins = Integer.parseInt(time.substring(3, 5));
            am_pm = time.substring(6);
        }

        for(int i = 0; i < 6; i++){
            if(i == index_to_update){
                continue;
            }else{
                String currTime = "";

                if(times_list.size() != 0) {
                    currTime = times_list.get(i);
                }

                if (currTime.length() < 8)  {   // set current selected hour
                    curr_hour = Integer.parseInt(currTime.substring(0, 1));
                    curr_mins = Integer.parseInt(currTime.substring(2, 4));
                    curr_am_pm = currTime.substring(5);
                } else {
                    curr_hour = Integer.parseInt(currTime.substring(0, 2));
                    curr_mins = Integer.parseInt(currTime.substring(3, 5));
                    curr_am_pm = currTime.substring(6);
                }

                   if( !compareTimes(hour, mins, am_pm, curr_hour, curr_mins, curr_am_pm) ){
                       return false;
                   }
            }
        }
        return true;
    }

    boolean compareTimes(int hour, int mins, String am_pm, int curr_hour, int curr_mins, String curr_am_pm){
        if( (Math.abs(hour - curr_hour)) > 1 )   {
            return true;
        }else if(Math.abs(hour - curr_hour) == 1){
            if( (am_pm.equals("AM") && curr_am_pm.equals("PM")) || (am_pm.equals("PM") && curr_am_pm.equals("AM")) ) {  // if over 12 hours apart
                return true;
            }
            else if(hour > curr_hour){
                if(mins - curr_mins >= 0) {
                    return true;
                }
            }else{
                if(curr_mins - mins >= 0){
                    return true;
                }
            }
        }else if(hour == curr_hour){
            if( (am_pm.equals("AM") && curr_am_pm.equals("PM")) || (am_pm.equals("PM") && curr_am_pm.equals("AM")) ) {  // if over 12 hours apart
                return true;
            }
        }
        return false;
    }

    private void setButtons(){
        updateTimeBtn = findViewById(R.id.updateTimeBtn);
        picker = findViewById(R.id.updateTimePicker);
        wBtn1 = findViewById(R.id.workout1_btn);
        wBtn2 = findViewById(R.id.workout2_btn);
        wBtn3 = findViewById(R.id.workout3_btn);
        wBtn4 = findViewById(R.id.workout4_btn);
        wBtn5 = findViewById(R.id.workout5_btn);
        wBtn6 = findViewById(R.id.workout6_btn);
    }

    private void highlightButtons(int option){
        if(option == 1){
            wBtn1.setBackgroundColor(getResources().getColor(R.color.lime_green));
        } else {
            wBtn1.setBackgroundColor(getResources().getColor(R.color.logo_green));
        }
        if(option == 2){
            wBtn2.setBackgroundColor(getResources().getColor(R.color.lime_green));
        } else {
            wBtn2.setBackgroundColor(getResources().getColor(R.color.logo_green));
        }
        if(option == 3){
            wBtn3.setBackgroundColor(getResources().getColor(R.color.lime_green));
        } else {
            wBtn3.setBackgroundColor(getResources().getColor(R.color.logo_green));
        }
        if(option == 4){
            wBtn4.setBackgroundColor(getResources().getColor(R.color.lime_green));
        } else {
            wBtn4.setBackgroundColor(getResources().getColor(R.color.logo_green));
        }
        if(option == 5){
            wBtn5.setBackgroundColor(getResources().getColor(R.color.lime_green));
        } else {
            wBtn5.setBackgroundColor(getResources().getColor(R.color.logo_green));
        }
        if(option == 6){
            wBtn6.setBackgroundColor(getResources().getColor(R.color.lime_green));
        } else {
            wBtn6.setBackgroundColor(getResources().getColor(R.color.logo_green));
        }
    }

    public void updateNotification(ArrayList<String> list, int index){
        int h1;
        int min1;
        long hours24InMilis = 1000 * 60 * 60 * 24;

        //set all hours and minutes convert them to military time
        h1= setHourOrMin(list.get(index),true);
        min1 = setHourOrMin(list.get(index), false);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,h1);
        calendar.set(Calendar.MINUTE,min1);

        //if the time being updated is before the current time then add a day
        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(UpdateSchedule.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateSchedule.this, notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), hours24InMilis, pendingIntent);
    }

    int setHourOrMin(String time_str, boolean hour_or_min){
        int time;
        int start = time_str.length()-2;
        String am_or_pm = time_str.substring(start);
        if(hour_or_min){//this is an hour
            if(time_str.charAt(1) == ':'){
                time = Integer.parseInt(time_str.substring(0,1));
            }else{
                time = Integer.parseInt(time_str.substring(0,2));
            }
            if(am_or_pm.equals("PM") && time != 12){
                time = time + 12;
            }
            if (am_or_pm.equals("AM") && time == 12) {
                time = time - 12;
            }
        }else{
            if(time_str.charAt(1) == ':'){
                time = Integer.parseInt(time_str.substring(2,4));
            }else{
                time = Integer.parseInt(time_str.substring(3,5));
            }
        }
        return time;
    }

}
