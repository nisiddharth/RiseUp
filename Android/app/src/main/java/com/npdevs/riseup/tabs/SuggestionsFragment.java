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
import com.npdevs.riseup.books.BookActivity;
import com.npdevs.riseup.emotion.EmotionDetectActivity;
import com.npdevs.riseup.emotion.EmotionResultActivity;
import com.npdevs.riseup.helper.EmotionData;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuggestionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuggestionsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView musicCardView;
    private CardView booksCardView;

    public SuggestionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ftab3.
     */
    // TODO: Rename and change types and number of parameters
    public static SuggestionsFragment newInstance(String param1, String param2) {
        SuggestionsFragment fragment = new SuggestionsFragment();
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
        View view = inflater.inflate(R.layout.fragment_suggestions, container, false);

        musicCardView = view.findViewById(R.id.music);
        musicCardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("EmotionData", Context.MODE_PRIVATE);
                if(sharedPreferences.getString("Emotion0",null) == null) {
                    Intent intent = new Intent(getActivity(), EmotionDetectActivity.class);
                    intent.putExtra("name","music");
                    startActivity(intent);
                }
                else {
                    Intent toResults = new Intent(getActivity(), EmotionResultActivity.class);
                    ArrayList<EmotionData> topFourArrayList = new ArrayList<>();
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion0",null),sharedPreferences.getFloat("EmotionValue0",0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion1",null),sharedPreferences.getFloat("EmotionValue1",0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion2",null),sharedPreferences.getFloat("EmotionValue2",0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion3",null),sharedPreferences.getFloat("EmotionValue3",0)));
                    Bundle bundleToEmotionResults = new Bundle();
                    bundleToEmotionResults.putSerializable("topFourEmotions", (Serializable)topFourArrayList);
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
                if(sharedPreferences.getString("Emotion0",null) == null) {
                    Intent intent = new Intent(getActivity(), EmotionDetectActivity.class);
                    intent.putExtra("name","book");
                    startActivity(intent);
                }
                else {
                    Intent toResults = new Intent(getActivity(), BookActivity.class);
                    ArrayList<EmotionData> topFourArrayList = new ArrayList<>();
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion0",null),sharedPreferences.getFloat("EmotionValue0",0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion1",null),sharedPreferences.getFloat("EmotionValue1",0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion2",null),sharedPreferences.getFloat("EmotionValue2",0)));
                    topFourArrayList.add(new EmotionData(sharedPreferences.getString("Emotion3",null),sharedPreferences.getFloat("EmotionValue3",0)));
                    Bundle bundleToEmotionResults = new Bundle();
                    bundleToEmotionResults.putSerializable("topFourEmotions", (Serializable)topFourArrayList);
                    toResults.putExtra("emotionResultsBundle", bundleToEmotionResults);
                    startActivity(toResults);
                }
            }
        });

        return view;
    }
}