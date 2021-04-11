package com.npdevs.riseup.books;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.npdevs.riseup.R;
import com.npdevs.riseup.SettingsActivity;
import com.npdevs.riseup.helper.EmotionData;

import java.util.ArrayList;
import java.util.Random;

public class BookActivity extends AppCompatActivity {

    private final static int TOP_EMOTION = 0;
    public ListView lview;
    ArrayList<String> moodArr;
    private BookListAdapter bookListAdapter;
    private ArrayList<EmotionData> topFourEmotionsList;
    //public TextView bknm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Bundle extras = getIntent().getExtras();
        Log.e("ashu12_chi", extras.toString());
        if (extras != null) {
            // TODO make these values string constants in here and emotion detect
            Bundle fromEmotionDetectBundle = extras.getBundle("emotionResultsBundle");
            topFourEmotionsList = (ArrayList<EmotionData>)
                    fromEmotionDetectBundle.getSerializable("topFourEmotions");
            Log.e("ashu12_chi", topFourEmotionsList.toString());
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
            chart.getXAxis().setTextColor(Color.WHITE);
            chart.getXAxis().setDrawLimitLinesBehindData(false);
            chart.getAxisLeft().setDrawAxisLine(false);
            chart.getAxisLeft().setDrawGridLines(false);
            chart.getAxisLeft().setDrawLabels(false);
            chart.getAxisLeft().setDrawTopYLabelEntry(false);
            chart.getAxisLeft().setDrawZeroLine(false);

            chart.getAxisRight().setDrawLabels(false);
            chart.getLegend().setTextColor(Color.WHITE);

            chart.setDrawGridBackground(false);
            chart.setPinchZoom(false);
            chart.setDescription(null);

            // refresh the chart
            chart.invalidate();
            // set the Title TextView for the top emotion found
            TextView playListTitle = findViewById(R.id.playlist_title);
            playListTitle.setText(getString(R.string.result_title_booklist,
                    topFourEmotionsList.get(TOP_EMOTION).getEmotion()));
            // set the WebView for the top emotion's playlist
        }
        //display errors, although they are not likely to happen at this point
        else {
            Toast.makeText(this, "Cannot display graph results", Toast.LENGTH_LONG).show();
            // Log.d("EmotionResultActivity",getString(R.string.null_result_bundles));
        }

        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Recommendations");

        //bknm = (TextView) findViewById(R.id.moodTV);
        lview = findViewById(R.id.bklist);
        moodArr = new ArrayList<>();
        String[] moods = {"Over a cup of coffee", "Raining cats & dogs", "Stress Buster", "Dozing Off", "Travel Diaries", "Addicted", "Keep it simple, silly!", "Summer is here"};
        String selectedMood = moods[(new Random()).nextInt(8)];
        //bknm.setText(selectedMood);

        try {
            BookDB bookDB = new BookDB(this);
            bookDB.copyDB();
            SQLiteDatabase bkdb = bookDB.openDB();
            bkdb.beginTransaction();
            Cursor cur = bkdb.rawQuery("select bookname from book where bookID in (select bookID from relate where moodID in (select moodID from mood where moodname = ?));", new String[]{selectedMood});
            while (cur.moveToNext()) {
                String booknm = cur.getString(0);

                moodArr.add(booknm);

                //bmodel.put(booknm, moodArr.toArray(new String[]{}));
                android.util.Log.d(this.getClass().getSimpleName(), "adding to model: " +
                        booknm + " at " + moodArr.toArray(new String[]{}).length +
                        " position.");
                //moods.close();
            }
            cur.close();
            bkdb.endTransaction();
            bkdb.close();
            bookDB.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        bookListAdapter = new BookListAdapter(BookActivity.this, android.R.layout.simple_list_item_1, moodArr);
        lview.setAdapter(bookListAdapter);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onClick(View v) {
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String book = moodArr.get(position);
                Intent intent = new Intent(
                        BookActivity.this,
                        DetailsActivity.class
                );
                intent.putExtra("selected", book);
                startActivity(intent);
                Toast t = Toast.makeText(getApplicationContext(), "view book details", Toast.LENGTH_SHORT);
                t.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        BookActivity.this.finish();

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
}