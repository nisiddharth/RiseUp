package com.npdevs.riseup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.npdevs.riseup.utils.SharedPrefs;

public class MainActivity extends AppCompatActivity {
    private SharedPrefs prefs;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new SharedPrefs(this);

        //Checking if token exists or not
        if (prefs.isSignedIn()) {
            intent = new Intent(this, FrontActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}