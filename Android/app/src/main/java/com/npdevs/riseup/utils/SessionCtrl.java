package com.npdevs.riseup.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.npdevs.riseup.api.responseModels.user.SaveTokenResponse;
import com.npdevs.riseup.api.retrofit.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionCtrl {
    Context context;
    SharedPrefs prefs;

    public SessionCtrl(Context context) {
        this.context = context;
        prefs = new SharedPrefs(context);
    }

    public void onApplicationStart() {
        new PermissionCtrl(context).askAllPermissions();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        //Saving device token
                        Map<String, String> body = new HashMap<>();
                        body.put("token", token);
                        RetrofitClient.getClient().saveDeviceToken(prefs.getToken(), body).enqueue(new Callback<SaveTokenResponse>() {
                            @Override
                            public void onResponse(Call<SaveTokenResponse> call, Response<SaveTokenResponse> response) {
                                if (response.isSuccessful())
                                    Log.v("Token Saved", token);
                            }

                            @Override
                            public void onFailure(Call<SaveTokenResponse> call, Throwable t) {

                            }
                        });
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "FirebaseNotificationChannel";
            String description = "NoDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("#8794", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void onLogout() {
        //Removing token
        RetrofitClient.getClient().removeDeviceToken(prefs.getToken()).enqueue(new Callback<com.npdevs.riseup.api.responseModels.Response>() {
            @Override
            public void onResponse(Call<com.npdevs.riseup.api.responseModels.Response> call, Response<com.npdevs.riseup.api.responseModels.Response> response) {

            }

            @Override
            public void onFailure(Call<com.npdevs.riseup.api.responseModels.Response> call, Throwable t) {

            }
        });
    }
}
