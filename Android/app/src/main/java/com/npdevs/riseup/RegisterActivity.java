package com.npdevs.riseup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.npdevs.riseup.api.responseModels.auth.SignUpResponse;
import com.npdevs.riseup.api.retrofit.RetrofitClient;
import com.npdevs.riseup.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void toggleProgress() {
        if (binding.progressBar.getVisibility() == View.VISIBLE)
            binding.progressBar.setVisibility(View.INVISIBLE);
        else
            binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void register() {
        toggleProgress();

        Map<String, String> body = new HashMap<>();
        body.put("name", binding.nameText.getText().toString());
        body.put("email", binding.emailText.getText().toString());
        body.put("password", binding.passwordText.getText().toString());
        body.put("phone", binding.phoneText.getText().toString());

        RetrofitClient.getClient().signUp(body).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    SignUpResponse errorResponse = gson.fromJson(response.errorBody().charStream(), SignUpResponse.class);
                    Toast.makeText(RegisterActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    toggleProgress();
                    return;
                }

                Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                toggleProgress();
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}