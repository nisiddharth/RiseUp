package com.npdevs.riseup.books;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public class BookListAdapter extends ArrayAdapter<String> implements Serializable {

    public BookActivity parent;
    public TextView currentSelectedTV = null;
    public String selectedM;
    public String moodnm;
    private Context context;

    public BookListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        android.util.Log.d(this.getClass().getSimpleName(), "in constructor so creating new model");
        this.parent = parent;
        this.context = context;
        setModelFromDB(objects);
    }

    private void setModelFromDB(List<String> objects) {
    }

    private void resetModel(LinkedHashMap<String, String[]> aModel) {
        List<String> objects = null;
        setModelFromDB(objects);
        this.notifyDataSetChanged();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}