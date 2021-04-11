package com.npdevs.riseup.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.npdevs.riseup.R;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add activity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addBtn = findViewById(R.id.button);
        RadioGroup group = findViewById(R.id.radioGrp);
        TextInputEditText editText = findViewById(R.id.editTextTextPersonName);
        addBtn.setOnClickListener(v -> {
            int id = group.getCheckedRadioButtonId();
            String type = "";
            if (id == R.id.learning)
                type = "learning";
            else if (id == R.id.productive)
                type = "productive";
            else
                type = "leisure";
            DatabaseActivityHelper databaseActivityHelper = new DatabaseActivityHelper(this);
            databaseActivityHelper.insertData(editText.getText().toString(), type);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}