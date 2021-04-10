package com.npdevs.riseup.books;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.npdevs.riseup.LoginActivity;
import com.npdevs.riseup.R;
import com.npdevs.riseup.SettingsActivity;

import java.io.IOException;
import java.sql.SQLException;

public class DetailsActivity extends AppCompatActivity {

    public TextView bkname;
    public TextView authornm;
    public TextView link1;
    public TextView link2;
    String selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar toolbar = getSupportActionBar();
        toolbar.setTitle("Book details");

        bkname = (TextView)findViewById(R.id.bkname);
        authornm = (TextView)findViewById(R.id.authnm);
        link1 = (TextView)findViewById(R.id.amlink);
        link2 = (TextView)findViewById(R.id.gdlink);

        Bundle b = getIntent().getExtras();
        selectedBook = b.getString("selected");
        bkname.setText(selectedBook);

        try {
            Log.w("DetailsActivity", "showing book details");
            BookDB bookdb = new BookDB((Context)this);

            bookdb.copyDB();
            SQLiteDatabase bkdb = bookdb.openDB();
            bkdb.beginTransaction();
            Cursor cur = bkdb.rawQuery("select author, link1, link2, votes from book where bookname = ?;",new String[]{selectedBook});
            while (cur.moveToNext()){
                String auth = cur.getString(0);
                String amazon = cur.getString(1);
                String goodreads = cur.getString(2);
                String vote = cur.getString(3);
                authornm.setText(auth);
                link1.setText(amazon);
                link2.setText(goodreads);
            }
            cur.close();
            bkdb.endTransaction();
            bkdb.close();
            bookdb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}