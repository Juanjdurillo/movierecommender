package com.example.android.movierecomender.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.android.movierecomender.data.MovieContract;
import com.example.android.movierecomender.data.MovieProvider;

/**
 * Created by juanjo on 9/27/15.
 */
public class TestUriMatcher extends AndroidTestCase{

        private static final String MOVIE_TITLE = "Sparrows";
        private static final long TEST_MOVIE_ID = 10L;

        // content://com.example.android.movierecomender/MOVIE"
        private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;
        private static final Uri TEST_MOVIE_WITH_TITLE = MovieContract.MovieEntry.buildMovieWithTitle(MOVIE_TITLE);

        public void testUriMatcher() {
            UriMatcher testMatcher = MovieProvider.buildUriMatcher();

            assertEquals("Error: The MOVIE URI was matched incorrectly.",
                    testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIES);
            assertEquals("Error: The WEATHER WITH LOCATION URI was matched incorrectly.",
                    testMatcher.match(TEST_MOVIE_WITH_TITLE), MovieProvider.MOVIES_WITH_TITLE);

        }
}



