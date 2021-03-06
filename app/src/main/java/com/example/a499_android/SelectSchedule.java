package com.example.a499_android;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class SelectSchedule extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private final String TAG = "Select Schedule";

    TimePicker picker;
    Button btnSelectTime;
    Button btnUpdateSchedule;
    TextView tvw;
    TextView schedule;
    ArrayList<String> times = new ArrayList<>();
    public static final String EXTRA = "SelectSchedule EXTRA";
    public List<SelectSchedule> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_schedule);
        tvw=(TextView)findViewById(R.id.time_view1);
        picker=(TimePicker)findViewById(R.id.timePicker1);
        schedule=(TextView)findViewById(R.id.schedule_list);
        picker.setIs24HourView(false);
        btnSelectTime=(Button)findViewById(R.id.selectTimeBtn);
        btnUpdateSchedule= findViewById(R.id.confirmSchedule);


        DocumentReference docRef = db.collection("Users").document("raul_676");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Map<String, Object> data = document.getData();
                        String password;
                        int points;
                        ArrayList<String> group  = new ArrayList<>();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Log.d(TAG, pair.getKey() + " = " + pair.getValue());
                            if(pair.getKey().toString().equals("Password")){password = pair.getValue().toString();}
                            if(pair.getKey().toString().equals("Points")){points = Integer.parseInt(pair.getValue().toString());}
                            if(pair.getKey().toString().equals("Schedule")){ group = (ArrayList<String>) document.get("Schedule"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        Log.d(TAG, group.get(0));
                        Toast.makeText(SelectSchedule.this, document.getData().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
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


