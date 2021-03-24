package com.example.a499_android;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class UpdateSchedule extends AppCompatActivity {
    private final String TAG = "Update Schedule";
    Button updateTimeBtn, wBtn1, wBtn2, wBtn3, wBtn4, wBtn5, wBtn6;
    TextView currentTime, updateTime;
    TimePicker picker;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> times_list  = new ArrayList<>();
    int index_to_update;// this index will indicate which time will be changed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule);
        setButtons();

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
                        currentTime.setText(times_list.get(0));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.soft_yellow));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.teal_700));
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
                        currentTime.setText(times_list.get(1));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.soft_yellow));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.teal_700));
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
                        currentTime.setText(times_list.get(2));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.soft_yellow));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.teal_700));
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
                        currentTime.setText(times_list.get(3));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.soft_yellow));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.teal_700));
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
                        currentTime.setText(times_list.get(4));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.soft_yellow));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.teal_700));
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
                        currentTime.setText(times_list.get(5));
                        wBtn6.setBackgroundColor(getResources().getColor(R.color.soft_yellow));
                        wBtn2.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn3.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn4.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn5.setBackgroundColor(getResources().getColor(R.color.teal_700));
                        wBtn1.setBackgroundColor(getResources().getColor(R.color.teal_700));
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
                    int hour, minute;
                    String min_less_than_ten="";
                    String time = "";
                    boolean less_than_10 = false;
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
                    times_list.set(index_to_update, time);
                    Log.d(TAG, times_list.get(index_to_update));
                    Object schedule_obj = times_list;
                    docRef.update("Schedule", schedule_obj );
                    updateTime.setText("" + time);
            }
        });
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
        currentTime = findViewById(R.id.current_time);
        updateTime = findViewById(R.id.selected_time);
    }

}
