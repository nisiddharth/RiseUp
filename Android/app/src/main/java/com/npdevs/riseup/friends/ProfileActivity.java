package com.npdevs.riseup.friends;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.Gson;
import com.npdevs.riseup.R;
import com.npdevs.riseup.SymptomsActivity;
import com.npdevs.riseup.api.responseModels.user.GetEmotionResponse;
import com.npdevs.riseup.api.retrofit.RetrofitClient;
import com.npdevs.riseup.databinding.ActivityProfileBinding;
import com.npdevs.riseup.utils.SharedPrefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private SharedPrefs prefs;
    private PieChart chart;
    private List<List<String>> emotions;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityProfileBinding binding;
    private int offset = 0;
    private String friendId;
    private String friendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Friend Summary");
        friendId = getIntent().getStringExtra("id");
        friendName = getIntent().getStringExtra("name");

        chart = binding.chart1;
        prefs = new SharedPrefs(this);

        loadData();
        setMetaData();

        TextView messageTap = binding.messageTap;
        messageTap.setOnClickListener(v -> {
            Intent intent = new Intent(this, SymptomsActivity.class);
            startActivity(intent);
        });

        setDateNav();
    }

    private void setMetaData() {
        binding.heading.setText(friendName);
    }

    private void setDateNav() {
        binding.previousDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset++;
                loadData();
            }
        });

        binding.nextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offset == 0)
                    return;
                offset--;
                loadData();
            }
        });
    }

    private void onLoadStart() {
        binding.swipeToRefresh.setRefreshing(true);
    }

    private void onLoadEnd() {
        binding.swipeToRefresh.setRefreshing(false);
    }

    private String getDateFormat() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1 * offset);
        Date date = cal.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    private void loadData() {
        binding.date.setText(getDateFormat());
        TextView summary = binding.summarytext;
        summary.setText("You doing good!");
        summary.setTextColor(getResources().getColor(R.color.dark_green));
        onLoadStart();
        RetrofitClient.getClient().getFriendEmotion(friendId, offset).enqueue(new Callback<GetEmotionResponse>() {
            @Override
            public void onResponse(Call<GetEmotionResponse> call, Response<GetEmotionResponse> response) {
                onLoadEnd();
                if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    com.npdevs.riseup.api.responseModels.Response errorResponse = gson.fromJson(response.errorBody().charStream(), com.npdevs.riseup.api.responseModels.Response.class);
                    Toast.makeText(ProfileActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                emotions = response.body().getData();
                setChart();
            }

            @Override
            public void onFailure(Call<GetEmotionResponse> call, Throwable t) {

            }
        });
    }

    private void setChart() {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
        setData(chart, emotions);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Their moods today");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 17, 0);
        return s;
    }

    private void setData(PieChart chart, List<List<String>> emotions) {
        HashMap<String, Integer> map = new HashMap<>();
        for (List<String> emo : emotions) {
            map.put(emo.get(1), 1 + map.getOrDefault(emo.get(1), 0));
        }
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        int total = 0, neg = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
            String label = entry.getKey();
            if (label.equalsIgnoreCase("Fear") || label.equalsIgnoreCase("Sad") || label.equalsIgnoreCase("Angry"))
                ++neg;
            ++total;
            if (neg >= 0.75 * total) {
                TextView summary = binding.summarytext;
                summary.setText("You are on the sad side.");
                summary.setTextColor(getResources().getColor(R.color.light_red));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Moods");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);
        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}