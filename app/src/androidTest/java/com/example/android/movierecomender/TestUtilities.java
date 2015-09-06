package com.example.android.movierecomender;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.android.movierecomender.data.MovieContract;

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
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTES, 800);
        return movieValues;
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues movieValues) {
        assertTrue("Empty cursor returner. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor,movieValues);
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


}
