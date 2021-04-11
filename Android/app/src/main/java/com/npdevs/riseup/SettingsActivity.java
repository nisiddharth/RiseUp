package com.npdevs.riseup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.npdevs.riseup.captureservice.SettingsUtil;
import com.npdevs.riseup.databinding.ActivitySettingsBinding;
import com.npdevs.riseup.utils.SessionCtrl;
import com.npdevs.riseup.utils.SharedPrefs;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    SharedPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Settings");
        actionBar.setDisplayHomeAsUpEnabled(true);

        prefs = new SharedPrefs(this);

        binding.switchMood.setChecked(SettingsUtil.isCaptureServiceRunning(this));
        binding.switchMood.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SettingsUtil.startCaptureService(SettingsActivity.this);
                } else {
                    SettingsUtil.stopCaptureService(SettingsActivity.this);
                }
            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void logout() {
        prefs.clearData();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

        new SessionCtrl(this).onLogout();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}