package com.npdevs.riseup.tabs;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.npdevs.riseup.MainActivity;
import com.npdevs.riseup.R;
import com.npdevs.riseup.friends.AddFriendsActivity;
import com.npdevs.riseup.friends.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ftab2.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        FloatingActionButton addFriends = view.findViewById(R.id.addFriendsBtn);
        addFriends.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
            startActivity(intent);
        });

        List<SampleItem2> msampleItem = new ArrayList<>();

        msampleItem.add(new SampleItem2("8004344462","nsp"));
        msampleItem.add(new SampleItem2("1234567890","someone"));


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new FriendsFragment.MainAdapter(msampleItem);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private class MainAdapter extends RecyclerView.Adapter<FriendsFragment.MainAdapter.ViewHolder> {
        private List<SampleItem2> samples;

        class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView, textViewmob;

            ViewHolder(View view) {
                super(view);
                textView = view.findViewById(R.id.textView11);
                textViewmob = view.findViewById(R.id.textView11mob);
            }
        }

        MainAdapter(List<SampleItem2> samples) {
            this.samples = samples;
            Log.e("nsp", samples.size() + "");
        }

        @NonNull
        @Override
        public FriendsFragment.MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_main_feature, parent, false);

            return new FriendsFragment.MainAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendsFragment.MainAdapter.ViewHolder holder, int position) {
            holder.textView.setText(samples.get(position).getName());
            holder.textViewmob.setText("Mob: " + samples.get(position).getMob());
            final String str = holder.textView.getText().toString();
            final String str1 = samples.get(position).getMob();
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    intent.putExtra("MOB",str1);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return samples.size();
        }
    }
}

class SampleItem2 {
    private String mob, name;

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SampleItem2() {
    }

    public SampleItem2(String mob, String name) {
        this.mob = mob;
        this.name = name;
    }
}
