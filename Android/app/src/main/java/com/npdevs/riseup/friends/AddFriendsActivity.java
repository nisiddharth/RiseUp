package com.npdevs.riseup.friends;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.npdevs.riseup.R;

import java.util.ArrayList;
import java.util.List;

public class AddFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Friends");

        ArrayList<String> family = new ArrayList<>();

    }
}