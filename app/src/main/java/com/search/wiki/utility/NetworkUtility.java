package com.search.wiki.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.search.wiki.R;
import com.search.wiki.app.WikiSearchApplication;

public class NetworkUtility {
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) WikiSearchApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void showNetworkError(Context context) {
        DialogUtility.showToastMessage(context, context.getString(R.string.network_error_alert_message), Toast.LENGTH_SHORT);
    }


}
