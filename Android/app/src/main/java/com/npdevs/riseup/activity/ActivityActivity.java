package com.npdevs.riseup.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.npdevs.riseup.R;

public class ActivityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Activities");

        DatabaseActivityHelper databaseActivityHelper = new DatabaseActivityHelper(this);
        Cursor data = databaseActivityHelper.getProductiveData();
        EditText editText = findViewById(R.id.editTextTextMultiLine);
        String activities = "";
        int count = 1;
        while (data.moveToNext()) {
            activities += count + ". \t" + data.getString(1) + '\n';
        }
        editText.setText(activities);
    }
}