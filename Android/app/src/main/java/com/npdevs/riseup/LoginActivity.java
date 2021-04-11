package com.npdevs.riseup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.npdevs.riseup.api.responseModels.auth.SignInResponse;
import com.npdevs.riseup.api.retrofit.RetrofitClient;
import com.npdevs.riseup.databinding.ActivityLoginBinding;
import com.npdevs.riseup.utils.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    SharedPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = new SharedPrefs(this);
        ActionBar toolbar = getSupportActionBar();
        toolbar.setHomeAsUpIndicator(null);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void toggleProgress() {
        if (binding.progressBar.getVisibility() == View.VISIBLE)
            binding.progressBar.setVisibility(View.INVISIBLE);
        else
            binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void signIn() {
        toggleProgress();
        Map<String, String> body = new HashMap<>();
        body.put("email", binding.emailtext.getText().toString());
        body.put("password", binding.passwordtext.getText().toString());
        RetrofitClient.getClient().signIn(body).enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                toggleProgress();
                if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    SignInResponse errorResponse = gson.fromJson(response.errorBody().charStream(), SignInResponse.class);
                    Toast.makeText(LoginActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                //Saving token in shared preferences
                prefs.saveToken("Bearer " + response.body().getToken());

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {
                toggleProgress();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}