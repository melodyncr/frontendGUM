package com.example.a499_android;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {
    public String TAG = "Notification Receiver";
    @Override
    public void onReceive(Context context, Intent intent){


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context, LandingPage.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //Log.d(TAG, "starting receiver..");

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyLemubit")
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentText("Notification title for 100")
                .setAutoCancel(true);
        notificationManager.notify(100,builder.build());



        NotificationManager notificationManager2 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent2 = new Intent(context, LandingPage.class);
        repeating_intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//>?
        //Log.d(TAG, "starting receiver..");

        PendingIntent pendingIntent2 = PendingIntent.getActivity(context,200,repeating_intent2,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context,"notifyLemubit2")
                .setContentIntent(pendingIntent2)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentText("Notification title for 200")
                .setAutoCancel(true);
        notificationManager2.notify(200,builder2.build());

        ///////////

        NotificationManager notificationManager3 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent3 = new Intent(context, LandingPage.class);
        repeating_intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//>?
        //Log.d(TAG, "starting receiver..");

        PendingIntent pendingIntent3 = PendingIntent.getActivity(context,300,repeating_intent3,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder3 = new NotificationCompat.Builder(context,"notifyLemubit3")
                .setContentIntent(pendingIntent3)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentText("Notification title for 300")
                .setAutoCancel(true);
        notificationManager3.notify(300,builder3.build());

      /////

        NotificationManager notificationManager4 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent4 = new Intent(context, LandingPage.class);
        repeating_intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//>?
        //Log.d(TAG, "starting receiver..");

        PendingIntent pendingIntent4 = PendingIntent.getActivity(context,400,repeating_intent4,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder4 = new NotificationCompat.Builder(context,"notifyLemubit4")
                .setContentIntent(pendingIntent4)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentText("Notification title for 400")
                .setAutoCancel(true);
        notificationManager4.notify(400,builder4.build());

        ////

        NotificationManager notificationManager5 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent5 = new Intent(context, LandingPage.class);
        repeating_intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//>?
        //Log.d(TAG, "starting receiver..");

        PendingIntent pendingIntent5 = PendingIntent.getActivity(context,500,repeating_intent5,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder5 = new NotificationCompat.Builder(context,"notifyLemubit5")
                .setContentIntent(pendingIntent5)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentText("Notification title for 500")
                .setAutoCancel(true);
        notificationManager5.notify(500,builder5.build());

        ////

        NotificationManager notificationManager6 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent6 = new Intent(context, LandingPage.class);
        repeating_intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//>?
        //Log.d(TAG, "starting receiver..");

        PendingIntent pendingIntent6 = PendingIntent.getActivity(context,600,repeating_intent2,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder6 = new NotificationCompat.Builder(context,"notifyLemubit6")
                .setContentIntent(pendingIntent6)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentText("Notification title for 600")
                .setAutoCancel(true);
        notificationManager6.notify(600,builder6.build());

    }
}
