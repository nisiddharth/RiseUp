package com.npdevs.riseup.books;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.npdevs.riseup.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BookDB extends SQLiteOpenHelper {

    private static final boolean debugon = false;
    private static final int DATABASE_VERSION = 3;
    private static String dbName = "bookdb";
    private final Context context;
    private String dbPath;
    private SQLiteDatabase bkdb;

    public BookDB(Context context) {
        super(context, dbName, null, DATABASE_VERSION);
        this.context = context;
        dbPath = context.getFilesDir().getPath() + "/";
        android.util.Log.d(this.getClass().getSimpleName(), "dbpath: " + dbPath);
    }

    public void createDB() throws IOException {
        this.getReadableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "createDB Error copying database " + e.getMessage());
        }
    }


    private boolean checkDB() {    //does the database exist and is it initialized?
        SQLiteDatabase checkDB = null;
        boolean ret = false;
        try {
            String path = dbPath + dbName + ".db";
            debug("BookDB --> checkDB: path to db is", path);
            File aFile = new File(path);
            if (aFile.exists()) {
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB != null) {
                    debug("BookDB --> checkDB", "opened db at: " + checkDB.getPath());
                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='mood';", null);
                    boolean crsTabExists = false;
                    if (tabChk == null) {
                        debug("BookDB --> checkDB", "check for mood table result set is null");
                    } else {
                        tabChk.moveToNext();
                        debug("BookDB --> checkDB", "check for mood table result set is: " +
                                ((tabChk.isAfterLast() ? "empty" : tabChk.getString(0))));
                        crsTabExists = !tabChk.isAfterLast();
                    }
                    if (crsTabExists) {
                        Cursor c = checkDB.rawQuery("SELECT * FROM mood", null);
                        c.moveToFirst();
                        while (!c.isAfterLast()) {
                            String wName = c.getString(0);
                            int wid = c.getInt(1);
                            debug("BookDB --> checkDB", "Mood table has MoodName: " +
                                    wName + "\tmoodID: " + wid);
                            c.moveToNext();
                        }
                        ret = true;
                    }
                }
            }
        } catch (SQLiteException e) {
            android.util.Log.w("MoodDB->checkDB", e.getMessage());
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return ret;
    }

    public void copyDB() throws IOException {
        try {
            if (!checkDB()) {
                // only copy the database if it doesn't already exist in my database directory
                debug("BookDB --> copyDB", "checkDB returned false, starting copy");
                InputStream ip = context.getResources().openRawResource(R.raw.bookdb);
                // make sure the database path exists. if not, create it.
                File aFile = new File(dbPath);
                if (!aFile.exists()) {
                    aFile.mkdirs();
                }
                String op = dbPath + dbName + ".db";
                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            android.util.Log.w("BookDB --> copyDB", "IOException: " + e.getMessage());
        }
    }

    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPath + dbName + ".db";
        bkdb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        debug("CourseDB --> openDB", "opened db at path: " + bkdb.getPath());
        return bkdb;
    }

    @Override
    public synchronized void close() {
        if (bkdb != null)
            bkdb.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void debug(String hdr, String msg) {
        if (debugon) {
            android.util.Log.d(hdr, msg);
        }
    }
}
