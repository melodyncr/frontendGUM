package com.example.a499_android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {
    public String TAG = "Notification Receiver";

    @Override
    public void onReceive(Context context, Intent intent){
    String username = LoginActivity.loggedUserName;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent(context, LandingPage.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d(TAG, "starting receiver.. time: " + Calendar.getInstance().getTime());

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"GUM")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo_no_text)
                .setContentText("Time to Get Up and Move")
                .setAutoCancel(true);
        notificationManager.notify(UpdateSchedule.notificationID, builder.build());

        FinishedExerciseActivity.fromNotification = true;
    }
}
