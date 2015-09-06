package com.example.android.movierecomender.data;

import android.provider.BaseColumns;

/**
 * @author Juanjo Durillo (juanjod@gmail.com)
 * MovieContract for the MovieDataBase
 *
 */

public class MovieContract {
    /*Inner class representing the contract for the Movie Database */
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";

        /*stores the title of the movie as a String */
        public static final String COLUMN_TITLE = "title";
        /*stores the path to the poster of the movie as a String */
        public static final String COLUMN_POSTER_URI = "poster_path";
        /*stores the release date of the movie as a date*/
        public static final String COLUMN_RELEASE_DATE = "release_date";
        /*stores the language of the movie as a string */
        public static final String COLUMN_LANGUAGE  = "original_language";
        /*stores the number of votes of this movie as an integer */
        public static final String COLUMN_VOTES = "votes";
        /*stores the plot of the movie as a text*/
        public static final String COLUMN_SUMMARY = "summary";
    }
}
