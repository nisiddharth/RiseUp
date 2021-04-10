package com.npdevs.riseup.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.npdevs.riseup.R;
import com.npdevs.riseup.api.responseModels.Response;
import com.npdevs.riseup.api.responseModels.user.GetFriendsResponse;
import com.npdevs.riseup.api.retrofit.RetrofitClient;
import com.npdevs.riseup.databinding.ActivityRequestBinding;
import com.npdevs.riseup.databinding.RecyclerRequestsBinding;
import com.npdevs.riseup.datamodels.UserMeta;
import com.npdevs.riseup.utils.SharedPrefs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestActivity extends AppCompatActivity {
    ActivityRequestBinding binding;
    List<UserMeta> requests;
    SharedPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = new SharedPrefs(this);

        loadData();
    }

    private void loadData() {
        RetrofitClient.getClient().getAllRequest(prefs.getToken()).enqueue(new Callback<GetFriendsResponse>() {
            @Override
            public void onResponse(Call<GetFriendsResponse> call, retrofit2.Response<GetFriendsResponse> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    Response errorResponse = gson.fromJson(response.errorBody().charStream(), Response.class);
                    Toast.makeText(RequestActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                requests = response.body().getFriends();
                setRecycler();
            }

            @Override
            public void onFailure(Call<GetFriendsResponse> call, Throwable t) {

            }
        });
    }

    private void setRecycler() {
        binding.requestRecycler.setLayoutManager(new LinearLayoutManager(this));
        RequestRecycler adapter = new RequestRecycler();
        binding.requestRecycler.setAdapter(adapter);
    }

    private class RequestRecycler extends RecyclerView.Adapter<RequestRecycler.ViewHolder> {
        RecyclerRequestsBinding binding;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            binding = RecyclerRequestsBinding.inflate(getLayoutInflater(), parent, false);
            return new ViewHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            UserMeta user = requests.get(position);
            holder.name.setText(user.getName());
            holder.email.setText(user.getEmail());
            holder.phone.setText(user.getPhone());

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    acceptRequest(user);
                }
            });
        }

        @Override
        public int getItemCount() {
            return requests.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, email, phone;
            Button accept;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = binding.name;
                email = binding.email;
                phone = binding.phone;
                accept = binding.accept;
            }
        }

        void acceptRequest(UserMeta userMeta) {
            Map<String, String> body = new HashMap<>();
            body.put("friend_id", userMeta.getId());
            RetrofitClient.getClient().acceptRequest(prefs.getToken(), body).enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    if (!response.isSuccessful()) {
                        Gson gson = new Gson();
                        Response errorResponse = gson.fromJson(response.errorBody().charStream(), Response.class);
                        Toast.makeText(RequestActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(RequestActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {

                }
            });
        }
    }
}