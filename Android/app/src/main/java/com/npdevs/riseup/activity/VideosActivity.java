package com.npdevs.riseup.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.npdevs.riseup.api.responseModels.videos.GetVideoResponse;
import com.npdevs.riseup.api.retrofit.RetrofitClient;
import com.npdevs.riseup.databinding.ActivityVideosBinding;
import com.npdevs.riseup.databinding.RecyclerVideoBinding;
import com.npdevs.riseup.datamodels.YoutubeVideo;
import com.npdevs.riseup.helper.EmotionData;
import com.npdevs.riseup.utils.SharedPrefs;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosActivity extends AppCompatActivity {
    private final static int TOP_EMOTION = 0;
    ActivityVideosBinding binding;
    private List<YoutubeVideo> data;
    private SharedPrefs prefs;
    private ArrayList<EmotionData> topFourEmotionsList;
    private String emotion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = new SharedPrefs(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bundle fromEmotionDetectBundle = extras.getBundle("emotionResultsBundle");
            topFourEmotionsList = (ArrayList<EmotionData>)
                    fromEmotionDetectBundle.getSerializable("topFourEmotions");
            emotion = topFourEmotionsList.get(TOP_EMOTION).getEmotion();
            Log.e("Emotion for video", emotion);
        } else {
            Toast.makeText(this, "Cannot display graph results", Toast.LENGTH_LONG).show();
            // Log.d("EmotionResultActivity",getString(R.string.null_result_bundles));
        }

        loadData();
    }

    void toggleProgress() {

    }

    private void loadData() {
        toggleProgress();
        RetrofitClient.getClient().getVideos(prefs.getToken(), emotion).enqueue(new Callback<GetVideoResponse>() {
            @Override
            public void onResponse(Call<GetVideoResponse> call, Response<GetVideoResponse> response) {
                toggleProgress();
                if (!response.isSuccessful()) {

                }
                data = response.body().getVideos();
                setUpRecyclerView();
            }

            @Override
            public void onFailure(Call<GetVideoResponse> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerView() {
        binding.videoRecycler.setLayoutManager(new LinearLayoutManager(this));
        VideoRecyclerAdapter adapter = new VideoRecyclerAdapter();
        binding.videoRecycler.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.ViewHolder> {
        RecyclerVideoBinding binding;
        Context context = VideosActivity.this;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            binding = RecyclerVideoBinding.inflate(getLayoutInflater(), parent, false);
            return new ViewHolder(binding.getRoot());
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            YoutubeVideo video = data.get(position);
            getLifecycle().addObserver(holder.youTubePlayerView);
            holder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    super.onReady(youTubePlayer);
                    youTubePlayer.loadVideo(video.getVideoId(), 0);
                }
            });
            holder.title.setText(video.getTitle());
            holder.channel.setText(video.getChannel());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            YouTubePlayerView youTubePlayerView;
            TextView channel, title;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                youTubePlayerView = binding.youtubePlayerView;
                channel = binding.channel;
                title = binding.title;
            }
        }
    }
}