package com.npdevs.riseup.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.npdevs.riseup.R;
import com.npdevs.riseup.api.responseModels.user.GetFriendsResponse;
import com.npdevs.riseup.api.retrofit.RetrofitClient;
import com.npdevs.riseup.databinding.FragmentFriendsBinding;
import com.npdevs.riseup.databinding.RecyclerFriendsBinding;
import com.npdevs.riseup.datamodels.UserMeta;
import com.npdevs.riseup.friends.AddFriendsActivity;
import com.npdevs.riseup.friends.ProfileActivity;
import com.npdevs.riseup.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {
    FragmentFriendsBinding binding;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    List<UserMeta> data = new ArrayList<>();
    private SharedPrefs prefs;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        context = getContext();
        prefs = new SharedPrefs(context);

        recyclerView = view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);
        FloatingActionButton addFriends = view.findViewById(R.id.addFriendsBtn);
        addFriends.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
            startActivity(intent);
        });

        loadData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        return view;
    }

    private void onStartLoad(){
        swipeRefreshLayout.setRefreshing(true);
    }

    private void onCompleteLoad(){
        swipeRefreshLayout.setRefreshing(false);
    }

    private void loadData(){
        onStartLoad();
        RetrofitClient.getClient().getFriends(prefs.getToken()).enqueue(new Callback<GetFriendsResponse>() {
            @Override
            public void onResponse(Call<GetFriendsResponse> call, Response<GetFriendsResponse> response) {
                onCompleteLoad();
                if(response.isSuccessful()){
                    data = response.body().getFriends();
                    setUpRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<GetFriendsResponse> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerView (){
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new FriendsFragment.MainAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    private class MainAdapter extends RecyclerView.Adapter<FriendsFragment.MainAdapter.ViewHolder> {
        private List<UserMeta> samples;
        private RecyclerFriendsBinding binding;
        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name, email, phone;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name= binding.name;
                email = binding.email;
                phone = binding.phone;

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        intent.putExtra("user_id",samples.get(getAdapterPosition()).getId());
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        }

        MainAdapter(List<UserMeta> samples) {
            this.samples = samples;
            Log.e("nsp", samples.size() + "");
        }

        @NonNull
        @Override
        public FriendsFragment.MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            binding = RecyclerFriendsBinding.inflate(LayoutInflater.from(context), parent, false);
            return new FriendsFragment.MainAdapter.ViewHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull FriendsFragment.MainAdapter.ViewHolder holder, int position) {
            holder.name.setText(samples.get(position).getName());
            holder.phone.setText("Mob: " + samples.get(position).getPhone());
            holder.email.setText(samples.get(position).getEmail());
        }

        @Override
        public int getItemCount() {
            return samples.size();
        }
    }
}
