package com.npdevs.riseup.friends;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.npdevs.riseup.api.responseModels.Response;
import com.npdevs.riseup.api.retrofit.RetrofitClient;
import com.npdevs.riseup.databinding.ActivityAddFriendsBinding;
import com.npdevs.riseup.utils.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class AddFriendsActivity extends AppCompatActivity {
    ActivityAddFriendsBinding binding;
    private SharedPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = new SharedPrefs(this);

        binding.addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        toggleProgress();
        Map<String, String> body = new HashMap<>();
        body.put("identifier", binding.identifier.getText().toString());
        RetrofitClient.getClient().addFriend(prefs.getToken(), body).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                toggleProgress();
                if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    Response errorResponse = gson.fromJson(response.errorBody().charStream(), Response.class);
                    Toast.makeText(AddFriendsActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(AddFriendsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void toggleProgress() {
        if (binding.progressBar.getVisibility() == View.VISIBLE)
            binding.progressBar.setVisibility(View.INVISIBLE);
        else
            binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}