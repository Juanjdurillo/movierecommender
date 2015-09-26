package com.example.android.movierecomender.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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
    static UriMatcher buildUriMatcher()
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

        String selection =
        String [] selectionARgs = new String[]{movieTitle};

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
            case MOVIES_WITH_TITLE =
        }


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
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
