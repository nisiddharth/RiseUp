package com.npdevs.riseup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.npdevs.riseup.utils.SharedPrefs;

public class MainActivity extends AppCompatActivity {
    private SharedPrefs prefs;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new SharedPrefs(this);
        SharedPreferences sharedPreferences = getSharedPreferences("EmotionData", MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        //Checking if token exists or not
        if (prefs.isSignedIn()) {
            intent = new Intent(this, FrontActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }
}