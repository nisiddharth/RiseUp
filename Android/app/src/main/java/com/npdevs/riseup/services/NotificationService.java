package com.npdevs.riseup.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.npdevs.riseup.R;
import com.npdevs.riseup.api.responseModels.user.SaveTokenResponse;
import com.npdevs.riseup.api.retrofit.RetrofitClient;
import com.npdevs.riseup.utils.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService extends FirebaseMessagingService {
    public static String CHANNEL_ID = "#8795";
    public Context appContext;
    public SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Setting up the references
        appContext = this.getApplicationContext();
        //Retrieving the shared preferences instance
        this.sharedPreferences = getSharedPreferences("BOX_NAMES", Context.MODE_PRIVATE);
        if(remoteMessage.getData().get("type").equals("alert")){
            alertNotification(remoteMessage);
        }
    }

    public void alertNotification(RemoteMessage message){
        //Intent intent = new Intent(appContext, Reminder.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 0, intent, 0);

        //Setting title as the box name

        Map<String, String> data = message.getData();
        String title = data.get(sharedPreferences.getString(data.get("boxname"),"No Title"));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext, CHANNEL_ID)
                //.setSmallIcon(R.drawable.logo)
                .setContentTitle("Friend Alert")
                .setContentText(data.get("message"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(appContext);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(100, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Context context = this.getApplicationContext();
        SharedPrefs prefs = new SharedPrefs(context);

        //Saving device token
        Map<String, String> body = new HashMap<>();
        body.put("token", s);
        RetrofitClient.getClient().saveDeviceToken(prefs.getToken(),body).enqueue(new Callback<SaveTokenResponse>() {
            @Override
            public void onResponse(Call<SaveTokenResponse> call, Response<SaveTokenResponse> response) {
                if(response.isSuccessful())
                    Log.v("Token Saved", s);
            }

            @Override
            public void onFailure(Call<SaveTokenResponse> call, Throwable t) {

            }
        });
    }
}
