package com.search.wiki.manager;

import com.search.wiki.utility.PreferencesUtility;

public class SharedPreferenceManager {

    public static void setRecentSearchKey(String searchKey) {
        PreferencesUtility.setString(PreferencesUtility.PREF_KEY_RECENT_SEARCH, searchKey);
    }

    public static String getRecentSearchKey() {
        return PreferencesUtility.getString(PreferencesUtility.PREF_KEY_RECENT_SEARCH);
    }

}
