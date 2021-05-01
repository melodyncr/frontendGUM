package com.example.a499_android;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
                            Log.d(TAG, pair.getKey() + " = " + pair.getValue());
                            if(pair.getKey().toString().equals("Schedule")){ times_list = (ArrayList<String>) document.get("Schedule"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }

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
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.lime_green));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        index_to_update = 0;
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
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.lime_green));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        index_to_update = 1;
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
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.lime_green));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        index_to_update = 2;
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
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.lime_green));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        index_to_update = 3;
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
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.lime_green));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        index_to_update = 4;
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
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.lime_green));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.logo_green));
                        index_to_update = 5;
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

                    setNotifications(times_list);

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
                String currTime = times_list.get(i);
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("GUM", "GUM", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for GUM");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void setNotifications(ArrayList<String> list){
        createNotificationChannel();

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        Calendar calendar4 = Calendar.getInstance();
        Calendar calendar5 = Calendar.getInstance();
        Calendar calendar6 = Calendar.getInstance();

        //Log.d(TAG,list.get(0) + " time 1");

        int h1,h2,h3,h4,h5,h6;
        int min1,min2,min3,min4,min5,min6;
        long hours24InMilis = 1000 * 60 * 60 * 24;

        //set all hours and minutes convert them to military time
        h1= setHourOrMin(list.get(0),true);
        h2= setHourOrMin(list.get(1),true);
        h3= setHourOrMin(list.get(2),true);
        h4= setHourOrMin(list.get(3),true);
        h5= setHourOrMin(list.get(4),true);
        h6= setHourOrMin(list.get(5),true);
        min1 = setHourOrMin(list.get(0), false);
        min2 = setHourOrMin(list.get(1), false);
        min3 = setHourOrMin(list.get(2), false);
        min4 = setHourOrMin(list.get(3), false);
        min5 = setHourOrMin(list.get(4), false);
        min6 = setHourOrMin(list.get(5), false);

        calendar.set(Calendar.HOUR_OF_DAY,h1);
        calendar.set(Calendar.MINUTE,min1);
        calendar.set(Calendar.SECOND,0);

        calendar2.set(Calendar.HOUR_OF_DAY,h2);
        calendar2.set(Calendar.MINUTE,min2);
        calendar2.set(Calendar.SECOND,0);

        calendar3.set(Calendar.HOUR_OF_DAY,h3);
        calendar3.set(Calendar.MINUTE,min3);
        calendar3.set(Calendar.SECOND,0);


        calendar4.set(Calendar.HOUR_OF_DAY,h4);
        calendar4.set(Calendar.MINUTE,min4);

        calendar5.set(Calendar.HOUR_OF_DAY,h5);
        calendar5.set(Calendar.MINUTE,min5);

        calendar6.set(Calendar.HOUR_OF_DAY,h6);
        calendar6.set(Calendar.MINUTE,min6);


        Log.d(TAG, "\nTime 1 and 2." + calendar.getTime()  + calendar2.getTime());
        Log.d(TAG, "Time 3 and 4." + calendar3.getTime()  + calendar4.getTime());
        Log.d(TAG, "Time 5 and 6." + calendar5.getTime()  + calendar6.getTime());

        Intent intent = new Intent(UpdateSchedule.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateSchedule.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), hours24InMilis, pendingIntent);

        Intent intent2 = new Intent(UpdateSchedule.this, NotificationReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(UpdateSchedule.this, 200,intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), hours24InMilis, pendingIntent2);

        Intent intent3 = new Intent(UpdateSchedule.this, NotificationReceiver.class);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(UpdateSchedule.this, 300,intent3, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager3 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager3.setRepeating(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), hours24InMilis, pendingIntent3);

        Intent intent4 = new Intent(UpdateSchedule.this, NotificationReceiver.class);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(UpdateSchedule.this, 400,intent4, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager4 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager4.setRepeating(AlarmManager.RTC_WAKEUP, calendar4.getTimeInMillis(), hours24InMilis, pendingIntent4);

        Intent intent5 = new Intent(UpdateSchedule.this, NotificationReceiver.class);
        PendingIntent pendingIntent5 = PendingIntent.getBroadcast(UpdateSchedule.this, 500,intent5, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager5 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager5.setRepeating(AlarmManager.RTC_WAKEUP, calendar5.getTimeInMillis(), hours24InMilis, pendingIntent5);

        Intent intent6 = new Intent(UpdateSchedule.this, NotificationReceiver.class);
        PendingIntent pendingIntent6 = PendingIntent.getBroadcast(UpdateSchedule.this, 600,intent6, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager6 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager6.setRepeating(AlarmManager.RTC_WAKEUP, calendar6.getTimeInMillis(), hours24InMilis, pendingIntent6);
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
