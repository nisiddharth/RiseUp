package com.npdevs.riseup.captureservice;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class SettingsUtil {
    private void stopCaptureService(Context context) {
        Intent serviceIntent = new Intent(context, CaptureService.class);
        context.stopService(serviceIntent);
    }

    private void startCaptureService(Context context) {
        Intent serviceIntent = new Intent(context, CaptureService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}
