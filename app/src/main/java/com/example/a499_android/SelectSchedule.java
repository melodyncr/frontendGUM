package com.example.a499_android;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
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

        makeNotificationChannel();

        DocumentReference docRef = db.collection("Users").document(UploadSurvey.F_S_USERNAME);

        // Will upload/update schedule when
        btnUpdateSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeList_string.size() == 7) {
                    ArrayList<String> new_schedule = returnSchedule(timeList_string);
                    Log.d(TAG, new_schedule.toString());
                    Object schedule_obj = new_schedule;
                    docRef.update("Schedule", schedule_obj ).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            setNotifications(timeList_string);
                            int code = 100;
                            for(int i = 0; i < timeList_string.size(); i++){
                                createNotification(timeList_string, i, code);
                                code = code + 100;
                            }

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SelectSchedule.this);
                            builder1.setMessage("If you haven't already once you're logged in, check out the GUM Website. It is located using the toolbar on the top right.");
                            builder1.setCancelable(true);
                            builder1.setPositiveButton(
                                    "Got it!",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            Toast.makeText(SelectSchedule.this, "Schedule is  Updated! Now please login!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SelectSchedule.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    });
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
                        Toast.makeText(SelectSchedule.this, "Times must be spaced out by at least 1 hour!", Toast.LENGTH_SHORT).show();
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

    // check if every scheudled workout is an hour apart
    public boolean validSchedule(int hour, int minute, HashMap<Integer,Integer>list){
        if(list.size() == 0){
            return true;
        }
        int index = list.size()-1;
        Object obj_hr = list.keySet().toArray()[index];
        Object obj_min= list.values().toArray()[index];
        int pre_hour = Integer.parseInt(obj_hr.toString());
        int pre_min = Integer.parseInt(obj_min.toString());

        Log.d(TAG, "Previous time"+ pre_hour + ": " + pre_min);
        Log.d(TAG, "Current time"+ hour + ": " + minute );

        if(Math.abs(hour - pre_hour) > 1){
            return true;
        }else if(Math.abs(hour - pre_hour) == 1){
            if(minute - pre_min < 0) {
                return false;
                } else {
                    return true;
                }
        }else{ return false; }
    }

    private void makeNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("GUM", "GUM", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel for GUM");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void createNotification(ArrayList<String> list, int index, int notifID){
        int h1;
        int min1;
        long hours24InMilis = 1000 * 60 * 60 * 24;
        UpdateSchedule.notificationID = notifID;

        //set all hours and minutes convert them to military time
        h1= setHourOrMin(list.get(index),true);
        min1 = setHourOrMin(list.get(index), false);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,h1);
        calendar.set(Calendar.MINUTE,min1);

        Intent intent = new Intent(SelectSchedule.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SelectSchedule.this, UpdateSchedule.notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), hours24InMilis, pendingIntent);
    }

//    public void setNotifications(ArrayList<String> list){
//
//        Calendar calendar = Calendar.getInstance();
//        Calendar calendar2 = Calendar.getInstance();
//        Calendar calendar3 = Calendar.getInstance();
//        Calendar calendar4 = Calendar.getInstance();
//        Calendar calendar5 = Calendar.getInstance();
//        Calendar calendar6 = Calendar.getInstance();
//
//        //Log.d(TAG,list.get(0) + " time 1");
//
//        int h1,h2,h3,h4,h5,h6;
//        int min1,min2,min3,min4,min5,min6;
//        long hours24InMilis = 1000 * 60 * 60 * 24;
//
//        //set all hours and minutes convert them to military time
//        h1= setHourOrMin(list.get(0),true);
//        h2= setHourOrMin(list.get(1),true);
//        h3= setHourOrMin(list.get(2),true);
//        h4= setHourOrMin(list.get(3),true);
//        h5= setHourOrMin(list.get(4),true);
//        h6= setHourOrMin(list.get(5),true);
//        min1 = setHourOrMin(list.get(0), false);
//        min2 = setHourOrMin(list.get(1), false);
//        min3 = setHourOrMin(list.get(2), false);
//        min4 = setHourOrMin(list.get(3), false);
//        min5 = setHourOrMin(list.get(4), false);
//        min6 = setHourOrMin(list.get(5), false);
//
//        calendar.set(Calendar.HOUR_OF_DAY,h1);
//        calendar.set(Calendar.MINUTE,min1);
//        calendar.set(Calendar.SECOND,0);
//
//        calendar2.set(Calendar.HOUR_OF_DAY,h2);
//        calendar2.set(Calendar.MINUTE,min2);
//        calendar2.set(Calendar.SECOND,0);
//
//        calendar3.set(Calendar.HOUR_OF_DAY,h3);
//        calendar3.set(Calendar.MINUTE,min3);
//        calendar3.set(Calendar.SECOND,0);
//
//
//        calendar4.set(Calendar.HOUR_OF_DAY,h4);
//        calendar4.set(Calendar.MINUTE,min4);
//
//        calendar5.set(Calendar.HOUR_OF_DAY,h5);
//        calendar5.set(Calendar.MINUTE,min5);
//
//        calendar6.set(Calendar.HOUR_OF_DAY,h6);
//        calendar6.set(Calendar.MINUTE,min6);
//
//
//        Log.d(TAG, "\nTime 1 and 2." + calendar.getTime()  + calendar2.getTime());
//        Log.d(TAG, "Time 3 and 4." + calendar3.getTime()  + calendar4.getTime());
//        Log.d(TAG, "Time 5 and 6." + calendar5.getTime()  + calendar6.getTime());
//
//        UpdateSchedule.notificationID = 100;
//        Intent intent = new Intent(SelectSchedule.this, NotificationReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(SelectSchedule.this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), hours24InMilis, pendingIntent);
//
//        UpdateSchedule.notificationID = 200;
//        Intent intent2 = new Intent(SelectSchedule.this, NotificationReceiver.class);
//        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(SelectSchedule.this, 200,intent2, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager2.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), hours24InMilis, pendingIntent2);
//
//        UpdateSchedule.notificationID = 300;
//        Intent intent3 = new Intent(SelectSchedule.this, NotificationReceiver.class);
//        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(SelectSchedule.this, 300,intent3, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager3 = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager3.setRepeating(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), hours24InMilis, pendingIntent3);
//
//        UpdateSchedule.notificationID = 400;
//        Intent intent4 = new Intent(SelectSchedule.this, NotificationReceiver.class);
//        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(SelectSchedule.this, 400,intent4, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager4 = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager4.setRepeating(AlarmManager.RTC_WAKEUP, calendar4.getTimeInMillis(), hours24InMilis, pendingIntent4);
//
//        UpdateSchedule.notificationID = 500;
//        Intent intent5 = new Intent(SelectSchedule.this, NotificationReceiver.class);
//        PendingIntent pendingIntent5 = PendingIntent.getBroadcast(SelectSchedule.this, 500,intent5, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager5 = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager5.setRepeating(AlarmManager.RTC_WAKEUP, calendar5.getTimeInMillis(), hours24InMilis, pendingIntent5);
//
//        UpdateSchedule.notificationID = 600;
//        Intent intent6 = new Intent(SelectSchedule.this, NotificationReceiver.class);
//        PendingIntent pendingIntent6 = PendingIntent.getBroadcast(SelectSchedule.this, 600,intent6, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager6 = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager6.setRepeating(AlarmManager.RTC_WAKEUP, calendar6.getTimeInMillis(), hours24InMilis, pendingIntent6);
//    }

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

    // Intent Factory
    public static Intent getIntent(Context context, String val){
        Intent intent = new Intent(context, SelectSchedule.class);
        intent.putExtra(EXTRA, val);
        return intent;
    }
}


