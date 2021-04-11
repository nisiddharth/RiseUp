package com.npdevs.riseup.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.npdevs.riseup.FrontActivity;
import com.npdevs.riseup.R;
import com.npdevs.riseup.activity.RequestActivity;

import java.util.Map;

public class NotificationService extends FirebaseMessagingService {
    public static String CHANNEL_ID = "#8794";
    public Context appContext;
    public SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Setting up the references
        appContext = this.getApplicationContext();
        if (remoteMessage.getData().get("type").equals("alert")) {
            alertNotification(remoteMessage);
        } else if (remoteMessage.getData().get("type").equals("invite")) {
            inviteNotification(remoteMessage);
        }
    }

    public void inviteNotification(RemoteMessage message) {
        Intent intent = new Intent(appContext, RequestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0);
        Map<String, String> data = message.getData();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Invite")
                .setContentText(data.get("message"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(appContext);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(100, builder.build());
    }

    public void alertNotification(RemoteMessage message) {
        Intent intent = new Intent(appContext, FrontActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0);

        //Setting title as the box name

        Map<String, String> data = message.getData();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Friend Alert")
                .setContentText(data.get("message"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(appContext);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(100, builder.build());
    }
}
