package com.example.android.movierecomender.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Juanjo Durillo (juanjod@gmail.com)
 * MovieContract for the MovieDataBase
 *
 */

public class MovieContract {

    //Content authority for the provider created for this application
    public static final String CONTENT_AUTHORITY = "com.example.android.movierecomender";

    //defines the base content URI (will be used to create other URIs)
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    //defines the path for the movies table
    public static final String PATH_MOVIES = "movies";


    /*Inner class representing the contract for the Movie Database */
    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_MOVIES;

        public static final String TABLE_NAME = "movies";

        /*stores whether the movie is only for adults */
        public static final String COLUMN_ADULTS = "adults_movie";
        /*stores the title of the movie as a String */
        public static final String COLUMN_TITLE = "title";
        /*stores the path to the poster of the movie as a String */
        public static final String COLUMN_POSTER_URI = "poster_path";
        /*stores the language of the movie as a string */
        public static final String COLUMN_LANGUAGE  = "original_language";
        /*stores peoples votes on this movie */
        public static final String COLUMN_PEOPLE_VOTES = "people_votes";
        /*stores the summary of the movie */
        public static final String COLUMN_SUMMARY = "summary";
        /*stores the movie release date */
        public static final String COLUMN_RELEASE_DATE = "release_date";


        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieWithTitle(String title) {
            return CONTENT_URI.buildUpon().appendPath(title).build();
        }

        public static String getTitleFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
