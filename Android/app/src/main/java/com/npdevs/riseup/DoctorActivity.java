package com.npdevs.riseup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class DoctorActivity extends AppCompatActivity {

    private CardView apollo,practo,tataHealth,curefit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        apollo = findViewById(R.id.apollo);
        practo = findViewById(R.id.practo);
        tataHealth = findViewById(R.id.tatahealth);
        curefit = findViewById(R.id.curefit);

        apollo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apollo247.com/specialties/psychiatry"));
                startActivity(intent);
            }
        });

        practo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.practo.com/consult/ask-a-psychiatrist-online"));
                startActivity(intent);
            }
        });

        curefit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cure.fit/care/doctor-consultation/psychiatrist"));
                startActivity(intent);
            }
        });

        tataHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tatahealth.com/online-doctor-consultation/psychiatrist"));
                startActivity(intent);
            }
        });
    }
}