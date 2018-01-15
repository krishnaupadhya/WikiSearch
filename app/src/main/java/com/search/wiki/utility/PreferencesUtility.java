package com.search.wiki.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.search.wiki.app.WikiSearchApplication;

public class PreferencesUtility {

    private static final String PREF_NAME = "com.search.wiki";
    public static final String PREF_KEY_RECENT_SEARCH = "pref_key_recent_search";



    /**
     * Saves  String value in Shared Preference.
     *
     * @param key   Key name
     * @param value Value to be saved against the specified key name.
     */
    public static void setString(String key, String value) {
        SharedPreferences sharedPref = WikiSearchApplication.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }


    /**
     * Returns the String value stored in Shared Preference.
     *
     * @param key Key name whose value to be returned.
     * @return Value of the key name specified.
     */
    public static String getString(String key) {
        SharedPreferences sharedPref = WikiSearchApplication.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }



    /**
     * Removes the key value pair stored in the Shared Preference.
     *
     * @param key Key to be removed
     */
    public static void remove(String key) {
        SharedPreferences sharedPref = WikiSearchApplication.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
    }

}
