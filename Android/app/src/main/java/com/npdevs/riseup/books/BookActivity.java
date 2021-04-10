package com.npdevs.riseup.books;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.npdevs.riseup.LoginActivity;
import com.npdevs.riseup.R;
import com.npdevs.riseup.SettingsActivity;

import java.util.ArrayList;
import java.util.Random;

public class BookActivity extends AppCompatActivity {

    ArrayList<String> moodArr;
    public ListView lview;
    private BookListAdapter bookListAdapter;
    public TextView bknm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        bknm = (TextView) findViewById(R.id.moodTV);
        lview = (ListView) findViewById(R.id.bklist);
        moodArr = new ArrayList<>();
        String[] moods = {"Over a cup of coffee", "Raining cats & dogs", "Stress Buster", "Dozing Off", "Travel Diaries", "Addicted", "Keep it simple, silly!", "Summer is here"};
        String selectedMood = moods[(new Random()).nextInt(8)];
        bknm.setText(selectedMood);

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
}