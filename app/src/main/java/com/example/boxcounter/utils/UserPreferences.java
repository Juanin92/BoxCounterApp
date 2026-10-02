package com.example.boxcounter.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static final String PREF_NAME = "box_counter_prefs";
    private static final String KEY_USER_NAME = "key_user_name";

    private final SharedPreferences preferences;

    public UserPreferences(Context context){
        this.preferences = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserName(String name){
        preferences.edit().putString(KEY_USER_NAME, name.trim()).apply();
    }

    public String getUserName(){
        return preferences.getString(KEY_USER_NAME, "");
    }

    public boolean hasUserName(){
        return !getUserName().isEmpty();
    }
}
