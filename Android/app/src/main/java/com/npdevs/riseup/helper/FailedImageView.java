package com.npdevs.riseup.helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.npdevs.riseup.R;

public class FailedImageView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed_image_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //  Bitmap temp = getIntent().getParcelableExtra("FAILED_IMAGE");
        //   ImageView imageView = findViewById(R.id.failedImageView);
        //   imageView.setImageBitmap(temp);

        //  setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}