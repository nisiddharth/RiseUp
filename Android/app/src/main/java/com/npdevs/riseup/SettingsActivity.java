package com.npdevs.riseup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;   
import android.widget.CompoundButton;
import android.widget.Switch;

import com.npdevs.riseup.captureservice.SettingsUtil;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch switchMood = findViewById(R.id.switchMood);
        switchMood.setChecked(SettingsUtil.isCaptureServiceRunning(this));
        switchMood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    SettingsUtil.startCaptureService(SettingsActivity.this);
                } else {
                    SettingsUtil.stopCaptureService(SettingsActivity.this);
                }
            }
        });
    }
}