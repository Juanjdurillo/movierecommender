package com.example.android.movierecomender.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * author Juanjo Durillo - juanjod@gmail.com
 * This class tests the movie database
 */
public class TestDB extends AndroidTestCase {
    public static final String LOG_TAG = TestDB.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    /**
     * Tests that the movie table has been created and contains all the required columns"
     * @throws Throwable
     */
    public void testCreateDb () throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true,db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: this means that the database has not been createdCorrectly", c.moveToFirst());

        // verify that all tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error your data base has been created without all the tables", tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")",
                null);
        assertTrue("Error this means that we are unable to query for information", c.moveToFirst());

        final HashSet<String> movieTableColumns = new HashSet<String>();
        movieTableColumns.add(MovieContract.MovieEntry._ID);
        movieTableColumns.add(MovieContract.MovieEntry.COLUMN_LANGUAGE);
        movieTableColumns.add(MovieContract.MovieEntry.COLUMN_POSTER_URI);
        movieTableColumns.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
        movieTableColumns.add(MovieContract.MovieEntry.COLUMN_SUMMARY);
        movieTableColumns.add(MovieContract.MovieEntry.COLUMN_TITLE);
        movieTableColumns.add(MovieContract.MovieEntry.COLUMN_USER_VOTES);
        movieTableColumns.add(MovieContract.MovieEntry.COLUMN_POPULARITY);
        movieTableColumns.add(MovieContract.MovieEntry.COLUMN_PEOPLE_VOTES);
        movieTableColumns.add(MovieContract.MovieEntry.COLUMN_REVENUE);

        int columnIdx = c.getColumnIndex("name");

        do {
            String columnName = c.getString(columnIdx);

            movieTableColumns.remove(columnName);
        }while (c.moveToNext());

        assertTrue("Error the database does not conatain all the required columns ", movieTableColumns.isEmpty());
        db.close();
    }

    public void testMovieTable() {
        MovieDBHelper dbHelper = new MovieDBHelper(this.mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieRow();
        long locationRowId;
        locationRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
        assertTrue(locationRowId!=-1);
        Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                null, //all columns
                null, //columns for the where clause
                null, //values for the "where" clause
                null, //columns group by
                null, //columns to filter by row groups
                null // sort order
        );

        assertTrue("Error: no records have been returned from the last query", cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("Error: movie query validation failed", cursor, testValues);
        assertFalse("Error there are more data in the database", cursor.moveToNext());
        cursor.close();
        db.close();
    }

}
