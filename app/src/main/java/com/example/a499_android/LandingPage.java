package com.example.a499_android;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a499_android.utility.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import static com.example.a499_android.LoginActivity.loggedUserName;

public class LandingPage extends AppCompatActivity {

    public static final String EXTRA = "LandingPage EXTRA";
    private static final String TAG = "Current User Data";
    public static final String WSURVEYQ = "WSurveyQ";
    public static final String WSURVEYR = "WSurveyR";
    public static final String TYPE_SURVEY = "Weekly Survey";
    public static final String W_SURVEY_COUNT = "w_survey_count";
    public static final String W_SURVEY_Q = "w_survey_q";
    public static final String W_SURVEY_QC = "w_survey_qc";
    public static String fitnessLevel = "";
    public ArrayList<String> workoutList = new ArrayList<>();
    DocumentReference docRef;
    private boolean isAdmin = false;
    private boolean is_admin = true;
    MenuItem toAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Welcome...");
//        actionBar.setDisplayShowTitleEnabled(false);
        Button editSchedule = findViewById(R.id.editScheduleBtn);
        Button changeAvatar = findViewById(R.id.changeAvatarBtn);
        //Button logoutUser = findViewById(R.id.logoutBtn);
        Button startSurveyBtn = findViewById(R.id.startSurveyBtn);
        Button msgAdmin = findViewById(R.id.messageAdmin);
        // Start Exercise
        Button startExerciseBtn = findViewById(R.id.startActivityBtn);
        Button viewVideos = findViewById(R.id.videosBtn);
        TextView displayedPoints = findViewById(R.id.pointDisplay);
//        TextView displayedUsername = findViewById(R.id.usernameDisplay);

        // NOTE: user info read from db will be hardcoded until login activity is done
        String uName = SaveSharedPreference.getUserName(LandingPage.this);
        // Access a Cloud Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //This originally used currentUserName
        DocumentReference userNameRef = db.collection("Users").document(uName);
        docRef = db.collection("Users").document(uName);

        // Read User Info
        getUserInfo(new FirestoreCallback() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document.exists()) {
                    Log.d(TAG, "Found User Data");
                    Toast.makeText(LandingPage.this, "Successfully Found User Data", Toast.LENGTH_SHORT).show();
                    displayedPoints.setText(document.getData().get("Points").toString());
                    actionBar.setTitle("Welcome, " + uName);
                    if (document.getData().get("IsAdmin") == null) {
                        Log.d(TAG, "welcome user my nameodsfjhklfddf;jfdk");
                        is_admin = false;
                        isAdmin = true;
                        invalidateOptionsMenu();
                    }
                } else {
                    Toast.makeText(LandingPage.this, "Unable to Load User Data", Toast.LENGTH_SHORT).show();
                }
            }
        }, userNameRef);

        editSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, UpdateSchedule.class);
                startActivity(intent);
            }
        });

        changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPage.this, SelectAvatar.class);
                startActivity(intent);
            }
        });

        startSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LandingPage.this, DetermineQuestionType.class);
                startActivity(intent);
            }
        });

        // Start Activity
        startExerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LandingPage.this, SelectWorkout.class);
                startActivity(intent);
            }
        });

        viewVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingPage.this, VideoDemonstrations.class);
                startActivity(intent);
            }
        });

        msgAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_admin){
                    Log.d(TAG, "admin");
                    Intent intent = new Intent(LandingPage.this, AdminMsgList.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(LandingPage.this, MessageAdmin.class);
                    startActivity(intent);
                }
            }
        });

    }

    // Intent Factory
    public static Intent getIntent(Context context, String val){
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(EXTRA, val);
        return intent;
    }

    //maybe retrieve schedule and pass it through intent first
    public void startEditScheduleActivity(View view){
        Intent intent = SelectSchedule.getIntent(this, "");
        startActivity(intent);
    }

    private void getUserInfo(FirestoreCallback firestoreCallback, DocumentReference currentUserName) {
        currentUserName.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        firestoreCallback.onSuccess(document);
                        Log.d(TAG, currentUserName.getId() + " username");
                        loggedUserName = currentUserName.getId();
                        CreateAccount.first_survey = false;

                        Map<String, Object> data = document.getData();// map to get workoutlist data
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            Log.d(TAG, pair.getKey() + " = " + pair.getValue());
                            if(pair.getKey().toString().equals("Schedule")){ workoutList = (ArrayList<String>) document.get("Schedule"); }
                            if(pair.getKey().toString().equals("FitnessLvl")){ fitnessLevel = (String) document.get("FitnessLvl");}

                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        setNotifications(workoutList);
                        Log.d(TAG, "workout list: " + workoutList.toString());
                        Log.d(TAG, "Fitness Level: " + workoutList.toString());
                    }
                } else {
                    Log.d(TAG, "ERROR: ", task.getException());
                }
            }
        });
    }

    private interface FirestoreCallback {
        void onSuccess(DocumentSnapshot document);
    }

    void setNotifications(ArrayList<String> list){
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        Calendar calendar4 = Calendar.getInstance();
        Calendar calendar5 = Calendar.getInstance();
        Calendar calendar6 = Calendar.getInstance();

        //Log.d(TAG,list.get(0) + " time 1");

        int h1,h2,h3,h4,h5,h6;
        int min1,min2,min3,min4,min5,min6;
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

        Intent intent = new Intent(LandingPage.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(LandingPage.this, 100,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Intent intent2 = new Intent(LandingPage.this, NotificationReceiver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(LandingPage.this, 200,intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager2.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);

        Intent intent3 = new Intent(LandingPage.this, NotificationReceiver.class);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(LandingPage.this, 300,intent3, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager3 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager3.set(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent3);

        Intent intent4 = new Intent(LandingPage.this, NotificationReceiver.class);
        PendingIntent pendingIntent4 = PendingIntent.getBroadcast(LandingPage.this, 400,intent4, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager4 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager4.set(AlarmManager.RTC_WAKEUP, calendar4.getTimeInMillis(), pendingIntent4);

        Intent intent5 = new Intent(LandingPage.this, NotificationReceiver.class);
        PendingIntent pendingIntent5 = PendingIntent.getBroadcast(LandingPage.this, 500,intent5, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager5 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager5.set(AlarmManager.RTC_WAKEUP, calendar5.getTimeInMillis(), pendingIntent5);

        Intent intent6 = new Intent(LandingPage.this, NotificationReceiver.class);
        PendingIntent pendingIntent6 = PendingIntent.getBroadcast(LandingPage.this, 600,intent6, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager6 = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager6.set(AlarmManager.RTC_WAKEUP, calendar6.getTimeInMillis(), pendingIntent6);


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

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    /**
     * The isAdmin if block is weird...
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landing_page, menu);

        if (isAdmin) {
            menu.findItem(R.id.to_admin).setVisible(false);
        } else {
            menu.findItem(R.id.to_admin).setVisible(true);
        }
        invalidateOptionsMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_item:
                SaveSharedPreference.clearUserName(LandingPage.this); //clears preference of username and anything else in there
                Intent toMainActivityIntent = new Intent(LandingPage.this, MainActivity.class);
                startActivity(toMainActivityIntent);
                break;
            case R.id.to_admin:
                startActivity(new Intent(LandingPage.this, AdminLanding.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
