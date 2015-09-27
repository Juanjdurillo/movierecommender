package com.example.android.movierecomender.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Movie;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by juan on 9/27/15.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG= TestProvider.class.getSimpleName();

    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecordsFromProvider();
    }



    /** function to delete all rows in the database. Used from the udacity course to
     * develop the sunshine application */

    public void deleteAllRecordsFromProvider () {
        mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null);


        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        assertEquals("Error: Records not deleted from Movie", 0, cursor.getCount());
        cursor.close();

    }


    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());

        try {
            ProviderInfo providerINfo = pm.getProviderInfo(componentName, 0);
            assertEquals("Error: MovieProvider registered with authority: "+providerINfo.authority +
            " instead of authority" + MovieContract.CONTENT_AUTHORITY,
                    providerINfo.authority, MovieContract.CONTENT_AUTHORITY);

        } catch (PackageManager.NameNotFoundException e) {
                assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(), false);
        }
    }


    public void testBasicMovieQuery () {
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db      = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieRow();
        long movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME,null,testValues);
        assertTrue("Unable to Insert values in the database", movieRowId != -1);
        db.close();

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        TestUtilities.validateCursor("testBasicMovieQuery",movieCursor,testValues);
    }

    public void testUpdateMovie() {
        ContentValues testValues = TestUtilities.createMovieRow();

        Uri movieUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, testValues);
        long movieRowId = ContentUris.parseId(movieUri);
        assertTrue(movieRowId != -1);
        Log.d(LOG_TAG, "New row id: " + movieRowId);

        ContentValues updatedValues = new ContentValues(testValues);
        updatedValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Se armon el belen 2");

        Cursor movieCursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        movieCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                MovieContract.MovieEntry.CONTENT_URI, updatedValues, MovieContract.MovieEntry._ID+"= ?",
                new String [] {Long.toString(movieRowId)});
        assertEquals(count, 1);

        tco.waitForNotificationOrFail();
        movieCursor.unregisterContentObserver(tco);
        movieCursor.close();

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry._ID +" = "+movieRowId,
                null,
                null);

        TestUtilities.validateCursor("testUpdateLocation, error validating location entry update",cursor,updatedValues);
        cursor.close();


    }

    public void testInsertProvider() {
        ContentValues testValues = TestUtilities.createMovieRow();

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.MovieEntry.CONTENT_URI, true, tco);
        Uri movieUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, testValues);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long movieRowId = ContentUris.parseId(movieUri);

        assertTrue(movieRowId!=-1);


        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,null,null);

        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry",cursor,testValues);
    }

}
