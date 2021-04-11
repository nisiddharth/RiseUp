package com.npdevs.riseup.emotion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.npdevs.riseup.R;
import com.npdevs.riseup.helper.EmotionData;

import java.util.ArrayList;

public class EmotionResultActivity extends AppCompatActivity {
    // the index where the top emotion is
    private final static int TOP_EMOTION = 0;
    // top four emotions from our detect activity.
    private ArrayList<EmotionData> topFourEmotionsList;
    // the WebView which will hold our playlists
    private WebView playListView;
    //the url of the top emotion playlist
    private String topEmotionPlaylistURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get the scores and emotions as String ArrayLists
        // they should already be in order from greatest to lowest
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // TODO make these values string constants in here and emotion detect
            Bundle fromEmotionDetectBundle = extras.getBundle("emotionResultsBundle");
            topFourEmotionsList = (ArrayList<EmotionData>)
                    fromEmotionDetectBundle.getSerializable("topFourEmotions");
        } else {
            Toast.makeText(this, "Cannot display graph results", Toast.LENGTH_LONG).show();
            // Log.d("EmotionResultActivity",getString(R.string.null_result_bundles));
        }
        if (topFourEmotionsList != null) {
            // create a new Horizontal Bar Chart
            HorizontalBarChart chart = findViewById(R.id.chart);
            // get the chart data from the helper method getDataSet()
            BarData data = new BarData(getDataSet());
            data.setBarWidth(.25f);
            //set the data
            chart.setData(data);
            // make graph animated
            chart.animateXY(2000, 2000);

            // further format the graph make lines/labels invisible that we don't want to see
            chart.getXAxis().setDrawAxisLine(false);
            chart.getXAxis().setDrawGridLines(false);
            chart.getXAxis().setDrawLabels(false);
            chart.getXAxis().setTextColor(Color.BLACK);
            chart.getXAxis().setDrawLimitLinesBehindData(false);
            chart.getAxisLeft().setDrawAxisLine(false);
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getAxisLeft().setDrawLabels(false);
            chart.getAxisLeft().setDrawTopYLabelEntry(false);
            chart.getAxisLeft().setDrawZeroLine(false);

            chart.getAxisRight().setDrawLabels(false);
            chart.getLegend().setTextColor(Color.BLACK);

            chart.setDrawGridBackground(false);
            chart.setPinchZoom(false);
            chart.setDescription(null);

            // refresh the chart
            chart.invalidate();
            // set the Title TextView for the top emotion found
            TextView playListTitle = findViewById(R.id.playlist_title);
            playListTitle.setText(getString(R.string.result_title_playlist,
                    topFourEmotionsList.get(TOP_EMOTION).getEmotion()));
            // set the WebView for the top emotion's playlist
            playListView = findViewById(R.id.web_playlist);
            this.setPlayListWebView(topFourEmotionsList.get(TOP_EMOTION).getEmotion());
        }
        //display errors, although they are not likely to happen at this point
        else {
            Toast.makeText(this, "Cannot display graph results", Toast.LENGTH_LONG).show();
            // Log.d("EmotionResultActivity",getString(R.string.null_result_bundles));
        }

    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();

        ArrayList<BarEntry> emotion0 = new ArrayList<>();
        //add .1 to make sure each score shows up on the graph
        BarEntry score0 = new BarEntry(3.000f, (float) topFourEmotionsList.get(0)
                .getEmotionValue() + .01f);
        //make a set for each score and emotion
        emotion0.add(score0);
        BarDataSet emotion0Set = new BarDataSet(emotion0, topFourEmotionsList.get(0).getEmotion());
        emotion0Set.setColor(Color.GREEN);
        emotion0Set.setDrawValues(false);
        dataSets.add(emotion0Set);

        ArrayList<BarEntry> emotion1 = new ArrayList<>();
        BarEntry score1 = new BarEntry(2.000f, (float) topFourEmotionsList.get(1)
                .getEmotionValue() + .01f);
        emotion1.add(score1);
        BarDataSet emotion1Set = new BarDataSet(emotion1, topFourEmotionsList.get(1).getEmotion());
        emotion1Set.setColor(Color.BLUE);
        emotion1Set.setDrawValues(false);
        dataSets.add(emotion1Set);

        ArrayList<BarEntry> emotion2 = new ArrayList<>();
        BarEntry score2 = new BarEntry(1.000f, (float) topFourEmotionsList.get(2)
                .getEmotionValue() + .01f);
        emotion2.add(score2);
        BarDataSet emotion2Set = new BarDataSet(emotion2, topFourEmotionsList.get(2).getEmotion());
        emotion2Set.setColor(Color.CYAN);
        emotion2Set.setDrawValues(false);
        dataSets.add(emotion2Set);

        ArrayList<BarEntry> emotion3 = new ArrayList<>();
        BarEntry score3 = new BarEntry(0.000f, (float) topFourEmotionsList.get(3)
                .getEmotionValue() + .01f);
        emotion3.add(score3);
        BarDataSet emotion3Set = new BarDataSet(emotion3, topFourEmotionsList.get(3).getEmotion());
        emotion3Set.setColor(Color.MAGENTA);
        emotion3Set.setDrawValues(false);
        dataSets.add(emotion3Set);

        return dataSets;
    }

    private void setPlayListWebView(String emotion) {
        // javascript is required for the playlist to display
        playListView.getSettings().setJavaScriptEnabled(true);
        playListView.setWebViewClient(new WebViewClient() {
            // create a new WebView client with Overriden methods.

            //the loading dialog to be displayed with page is loading.
            ProgressDialog loadingDialog = new ProgressDialog(EmotionResultActivity.this);

            @Override
            public void onPageFinished(WebView view, String url) {
                loadingDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingDialog.setCancelable(false);
                loadingDialog.setMessage("Loading...");
                loadingDialog.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl() != null && request.getUrl().toString().
                        startsWith("market://")) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(topEmotionPlaylistURL)));
                    return true;
                } else {
                    return false;
                }
            }


        });
        // switch on the emotion and load the url for each emotions playlist
        switch (emotion.toUpperCase()) {
            case "HAPPINESS":
                playListView.loadUrl(getString(R.string.happy_playlist_url));
                topEmotionPlaylistURL = getString(R.string.happy_playlist_url);
                break;
            case "ANGER":
                playListView.loadUrl(getString(R.string.angry_playlist_url));
                topEmotionPlaylistURL = getString(R.string.angry_playlist_url);
                break;
            case "NEUTRAL":
                playListView.loadUrl(getString(R.string.netural_playlist_url));
                topEmotionPlaylistURL = getString(R.string.netural_playlist_url);
                break;
            case "SURPRISE":
                playListView.loadUrl(getString(R.string.suprise_playlist_url));
                topEmotionPlaylistURL = getString(R.string.suprise_playlist_url);
                break;
            case "SADNESS":
                playListView.loadUrl(getString(R.string.sad_playlist_url));
                topEmotionPlaylistURL = getString(R.string.sad_playlist_url);
                break;

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
