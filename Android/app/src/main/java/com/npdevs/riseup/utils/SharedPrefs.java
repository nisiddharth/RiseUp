package com.npdevs.riseup.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    String email = "email", name = "name", token = "token", userId = "userId";
    boolean isDoctor = false;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPrefs(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getEmail() {
        return sharedPreferences.getString(email, null);
    }

    public void saveEmail(String key) {
        editor.putString(email, key);
        editor.commit();
    }

    public String getName() {
        return sharedPreferences.getString(name, null);
    }

    public void saveName(String key) {
        editor.putString(name, key);
        editor.commit();
    }

    public String getToken() {
        return sharedPreferences.getString(token, null);
    }

    public void saveToken(String key) {
        editor.putString(token, key);
        editor.commit();
    }

    public void clearData() {
        editor.clear();
        editor.commit();
    }

    public void saveUserId(int id) {
        editor.putInt(userId, id);
        editor.commit();
    }

    public boolean isSignedIn() {
        return sharedPreferences.getString("token", null) != null;
    }

    public int getUserId() {
        return sharedPreferences.getInt(userId, 1);
    }
}
