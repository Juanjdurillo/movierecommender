package com.example.android.movierecomender;

import android.content.Context;
import android.preference.PreferenceManager;

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

}
