package com.example.android.movierecomender.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.example.android.movierecomender.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * @author Juan J. Durillo
 * Class providing some utilities to perform tests witht he database
 */
public class TestUtilities  extends AndroidTestCase {
    static final long TEST_DATE = 1419033600L;
    static ContentValues createMovieRow () {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Se armó el belén");
        movieValues.put(MovieContract.MovieEntry.COLUMN_LANGUAGE, "es");
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URI, "localhost");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, TEST_DATE);
        movieValues.put(MovieContract.MovieEntry.COLUMN_SUMMARY, "Se arma el belen en una casa por navidad");
        /*movieValues.put(MovieContract.MovieEntry.COLUMN_USER_VOTES, 800);
        movieValues.put(MovieContract.MovieEntry.COLUMN_PEOPLE_VOTES, 30);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 3.8);
        movieValues.put(MovieContract.MovieEntry.COLUMN_REVENUE, 3000);*/
        return movieValues;
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues movieValues) {
        assertTrue("Empty cursor returner. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, movieValues);
    }

    static void validateCurrentRecord(String error, Cursor movieValues, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = movieValues.getColumnIndex(columnName);
            assertFalse("Column '"+ columnName + "' not found. " + error, idx==-1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not match the expected value'" +
            expectedValue + "' ." + error, expectedValue, movieValues.getString(idx));
        }
    }


    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }







}
