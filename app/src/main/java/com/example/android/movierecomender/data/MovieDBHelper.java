package com.example.android.movierecomender.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Juanjo Durillo - juanjod@gmail.com
 * This class facilitates the creation and management of the application database.
 *
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    // increase this number when the database schema gets changed
    private static final int DATABASE_VERSION = 1;

    /*name of the database file*/
    public static final String DATABASE_NAME = "movies.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
        MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL,"+
        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL,"+
        MovieContract.MovieEntry.COLUMN_POSTER_URI + " TEXT NOT NULL,"+
        MovieContract.MovieEntry.COLUMN_LANGUAGE + " TEXT NOT NULL,"+
        MovieContract.MovieEntry.COLUMN_VOTES + " INTEGER NOT NULL,"+
        MovieContract.MovieEntry.COLUMN_SUMMARY + " TEXT NOT NULL"+
        ");";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
