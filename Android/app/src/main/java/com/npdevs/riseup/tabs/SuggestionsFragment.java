package com.npdevs.riseup.tabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.npdevs.riseup.R;
import com.npdevs.riseup.activity.ActivityActivity;
import com.npdevs.riseup.activity.VideosActivity;
import com.npdevs.riseup.books.BookActivity;
import com.npdevs.riseup.emotion.EmotionDetectActivity;
import com.npdevs.riseup.emotion.EmotionResultActivity;
import com.npdevs.riseup.helper.EmotionData;

import java.io.Serializable;
import java.util.ArrayList;
public class SuggestionsFragment extends Fragment {

    private CardView musicCardView;
    private CardView booksCardView;
    private CardView activitiesCardView;
    private CardView videoCardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggestions, container, false);

        musicCardView = view.findViewById(R.id.music);
        videoCardView = view.findViewById(R.id.video);
        musicCardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("EmotionData", Context.MODE_PRIVATE);
                if (sharedPreferences.getString("Emotion0", null) == null) {
                    Intent intent = new Intent(getActivity(), EmotionDetectActivity.class);
                    intent.putExtra("name", "music");
                    startActivity(intent);
                } else {
                    Intent toResults = new Intent(getActivity(), EmotionResultActivity.class);
                    ArrayList<EmotionData> topFourArrayList = new ArrayList<>();
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion0", null), sharedPreferences.getFloat("EmotionValue0", 0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion1", null), sharedPreferences.getFloat("EmotionValue1", 0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion2", null), sharedPreferences.getFloat("EmotionValue2", 0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion3", null), sharedPreferences.getFloat("EmotionValue3", 0)));
                    Bundle bundleToEmotionResults = new Bundle();
                    bundleToEmotionResults.putSerializable("topFourEmotions", (Serializable) topFourArrayList);
                    toResults.putExtra("emotionResultsBundle", bundleToEmotionResults);
                    startActivity(toResults);
                }
            }
        });

        booksCardView = view.findViewById(R.id.books);
        booksCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("EmotionData", Context.MODE_PRIVATE);
                if (sharedPreferences.getString("Emotion0", null) == null) {
                    Intent intent = new Intent(getActivity(), EmotionDetectActivity.class);
                    intent.putExtra("name", "book");
                    startActivity(intent);
                } else {
                    Intent toResults = new Intent(getActivity(), BookActivity.class);
                    ArrayList<EmotionData> topFourArrayList = new ArrayList<>();
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion0", null), sharedPreferences.getFloat("EmotionValue0", 0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion1", null), sharedPreferences.getFloat("EmotionValue1", 0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion2", null), sharedPreferences.getFloat("EmotionValue2", 0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion3", null), sharedPreferences.getFloat("EmotionValue3", 0)));
                    Bundle bundleToEmotionResults = new Bundle();
                    bundleToEmotionResults.putSerializable("topFourEmotions", (Serializable) topFourArrayList);
                    toResults.putExtra("emotionResultsBundle", bundleToEmotionResults);
                    startActivity(toResults);
                }
            }
        });

        activitiesCardView = view.findViewById(R.id.activityCard);
        activitiesCardView.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("EmotionData", Context.MODE_PRIVATE);
            if (sharedPreferences.getString("Emotion0", null) == null) {
                Intent intent = new Intent(getActivity(), EmotionDetectActivity.class);
                intent.putExtra("name", "activity");
                startActivity(intent);
            } else {
                Intent toResults = new Intent(getActivity(), ActivityActivity.class);
                ArrayList<EmotionData> topFourArrayList = new ArrayList<>();
                topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion0", null), sharedPreferences.getFloat("EmotionValue0", 0)));
                topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion1", null), sharedPreferences.getFloat("EmotionValue1", 0)));
                topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion2", null), sharedPreferences.getFloat("EmotionValue2", 0)));
                topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion3", null), sharedPreferences.getFloat("EmotionValue3", 0)));
                Bundle bundleToEmotionResults = new Bundle();
                bundleToEmotionResults.putSerializable("topFourEmotions", (Serializable) topFourArrayList);
                toResults.putExtra("emotionResultsBundle", bundleToEmotionResults);
                startActivity(toResults);
            }
        });

        videoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("EmotionData", Context.MODE_PRIVATE);
                if (sharedPreferences.getString("Emotion0", null) == null) {
                    Intent intent = new Intent(getActivity(), EmotionDetectActivity.class);
                    intent.putExtra("name", "video");
                    startActivity(intent);
                } else {
                    Intent toResults = new Intent(getActivity(), VideosActivity.class);
                    ArrayList<EmotionData> topFourArrayList = new ArrayList<>();
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion0", null), sharedPreferences.getFloat("EmotionValue0", 0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion1", null), sharedPreferences.getFloat("EmotionValue1", 0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion2", null), sharedPreferences.getFloat("EmotionValue2", 0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion3", null), sharedPreferences.getFloat("EmotionValue3", 0)));
                    Bundle bundleToEmotionResults = new Bundle();
                    bundleToEmotionResults.putSerializable("topFourEmotions", (Serializable) topFourArrayList);
                    toResults.putExtra("emotionResultsBundle", bundleToEmotionResults);
                    startActivity(toResults);
                }
            }
        });



        return view;
    }
}