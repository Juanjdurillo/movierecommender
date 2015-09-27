package com.example.android.movierecomender.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by juanjo on 9/26/15 <juanjod@gmail.com>
 */
public class MovieProvider extends ContentProvider {
    //Uri matcher using by this provider
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieDBHelper dbHelper;

    static final int MOVIES = 100;
    static final int MOVIES_WITH_TITLE = 101;

    /* this function will create a UriMatcher for this provider */
    public static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher    = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority      = MovieContract.CONTENT_AUTHORITY;

        // add for the two defined Uris
        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIES+"/*",MOVIES_WITH_TITLE);

        return matcher;
    }


    private Cursor getMovieByTitle(Uri uri, String [] projection, String sortOrder) {
        String movieTitle = MovieContract.MovieEntry.getTitleFromUri(uri);

        String selection = MovieContract.MovieEntry.TABLE_NAME+
                "."+MovieContract.MovieEntry.COLUMN_TITLE+" = ?";
        String [] selectionArgs = new String[]{movieTitle};

        return dbHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        final int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIES :
                retCursor = dbHelper.getReadableDatabase().query(
                    MovieContract.MovieEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
                break;
            case MOVIES_WITH_TITLE :
                retCursor = getMovieByTitle(uri, projection, sortOrder);
                break;
            default :
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIES_WITH_TITLE:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = uriMatcher.match(uri);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri retUri = null;

        switch (match) {
            case MOVIES:
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,values);
                if (_id > 0)
                    retUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row in the database: "+uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return retUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;

        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);

        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }
}
