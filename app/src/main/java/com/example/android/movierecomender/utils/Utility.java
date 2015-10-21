package com.example.android.movierecomender.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.example.android.movierecomender.R;

/**
 * Juanjo Durillo <juanjod@gmail.com>
 * This class provide some utility functios that will be requires by different classes.
 */
public class Utility {
    /**
     * This method extracts the current sorting method from preferences
     */
    static  public String getCurrentSortingMethod(Context context) {
        String aux = PreferenceManager.getDefaultSharedPreferences(context).getString(
                context.getString(R.string.preferred_sorting_method_key),
                context.getString(R.string.default_sorting_method));
        return aux;
    }

    static public boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }


}
