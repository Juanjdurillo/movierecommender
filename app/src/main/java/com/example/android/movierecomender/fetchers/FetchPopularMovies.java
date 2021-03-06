package com.example.android.movierecomender.fetchers;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.android.movierecomender.container.MovieBasicInfo;
import com.example.android.movierecomender.container.MovieInfoContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements an asynchronous task to retrieve a list of movies from the MovieDB.
 */
public class FetchPopularMovies  extends AsyncTask<String, Void, List<MovieInfoContainer>> {

    private ArrayAdapter movieAdapter;
    private List<MovieBasicInfo> cache;


    public static final String MOVIE_DB_URL                 =
            "http://api.themoviedb.org/3/discover/movie?";
    public static final String SORT_PARAMETER_LABEL         = "sort_by";
    public static final String KEY_PARAMETER_LABEL          = "api_key";

    public static final String CONNECTION_ERROR              = "IO ERROR, INTERNET CONNECTION";
    public static final String IO_OPERATION_ERROR            = "IO OPERATION ERROR";
    public static final String JSON_EXCEPTION                = "JSON PROCESSING EXCEPTON";
    public static final String JSON_MOVIE_EXCEPTION          = "Error detected. Some movies or movies' information may not been showed";

    // Tags for debugging
    public static final String CONNECTION_TAG                = "CONNECTION";


    public FetchPopularMovies(ArrayAdapter adapter, List<MovieBasicInfo> cache) {
        this.movieAdapter = adapter;
        this.cache  = cache;
    }


    @Override
    /**
     * Connects to the MovieDB database and obtains the list of most popular movies
     * @param <code>params</code> is an array of length
     * <code>params[0]</code> indicates the sorting preference
     * <code>params[1]</code> indicates the movie_db user key
     */
    protected List<MovieInfoContainer> doInBackground(String... params) {

        Uri uri_builder = Uri.parse(MOVIE_DB_URL).buildUpon()
                .appendQueryParameter(SORT_PARAMETER_LABEL, params[0])
                .appendQueryParameter(KEY_PARAMETER_LABEL,params[1]).build();
        Log.d(CONNECTION_TAG, uri_builder.toString());

        HttpURLConnection movieDBConnection = null;
        String                      jSonStr           = null;
        List<MovieInfoContainer>    res               = null;


        try {

            movieDBConnection =  (HttpURLConnection)(new URL(uri_builder.toString())).openConnection();

            try (InputStreamReader inputStreamReader = new InputStreamReader(movieDBConnection.getInputStream());
                 BufferedReader dataBuffer        = new BufferedReader(inputStreamReader)) {
                StringBuffer      obtainedData      = new StringBuffer();
                String line;
                while ((line = dataBuffer.readLine()) != null)
                    obtainedData.append(line + "\n");

                jSonStr = obtainedData.toString();
            } catch (IOException e1) {
                Log.e(this.getClass().getName(),IO_OPERATION_ERROR );
            }

        } catch (IOException e) {
            Log.e(this.getClass().getName(), CONNECTION_ERROR);
        } finally {
            if (movieDBConnection!=null)
                movieDBConnection.disconnect();

        }

        if (jSonStr != null) {
            res = parseMovieInformaton(jSonStr);
        }
        return res;
    }

    /**
     * This method parses a json string, extracting all the information
     * @param jSonStr is a <code>String</code> containing a JSon object
     * @return a <code>List<MovieBasicInfo></code> with all the movies included in the
     * jSonStr object
     */
    List<MovieInfoContainer> parseMovieInformaton(String jSonStr) {
        if (jSonStr == null || jSonStr.isEmpty())
            return null;

        List<MovieInfoContainer>    list_of_movies = null;

        final String ARRAY_OF_MOVIES_LABEL      = "results";
        final String TITLE_LABEL                = "original_title";
        final String ADULT_CLASSIFICATION_LABEL = "adult";
        final String LANGUAGE_LABEL             = "original_language";
        final String PLOT_LABEL                 = "overview";
        final String RELEASE_DATE_LABEL         = "release_date";
        final String POSTER_PATH_LABEL          = "poster_path";
        final String AVERAGE_VOTES_LABEL        = "vote_average";
        final String ID_LABEL                   ="id";

        try {
            JSONArray array_of_json_movies = (new JSONObject(jSonStr)).getJSONArray(ARRAY_OF_MOVIES_LABEL);
            list_of_movies = new ArrayList<>(array_of_json_movies.length());
            try {
                for (int i = 0; i < array_of_json_movies.length(); i++) {
                    JSONObject  json_movie          = array_of_json_movies.getJSONObject(i)             ;
                    boolean     is_adult_movie = json_movie.getBoolean(ADULT_CLASSIFICATION_LABEL) ;
                    String      movie_title = json_movie.getString(TITLE_LABEL)                 ;
                    String      movie_language      = json_movie.getString(LANGUAGE_LABEL)              ;
                    String      movie_plot          = json_movie.getString(PLOT_LABEL)                  ;
                    String      movie_release       = json_movie.getString(RELEASE_DATE_LABEL)          ;
                    String      movie_poster_path   = json_movie.getString(POSTER_PATH_LABEL)           ;
                    String      movie_average_votes = json_movie.getString(AVERAGE_VOTES_LABEL)         ;
                    String      movie_id            = json_movie.getString(ID_LABEL)                    ;

                    if ((movie_poster_path!=null) && (!movie_poster_path.isEmpty()) &&
                        (movie_plot!=null) && (!movie_plot.isEmpty())) {
                        MovieBasicInfo movie;
                        movie = new MovieBasicInfo(is_adult_movie, movie_title, movie_language, movie_plot, movie_release, movie_poster_path, new Double(movie_average_votes), new Integer(movie_id));
                        Log.d(CONNECTION_TAG, movie.toString());
                        list_of_movies.add(movie);
                    }
                }
            } catch (JSONException e1) {
                Log.e(CONNECTION_TAG, JSON_MOVIE_EXCEPTION);
            }

        } catch (JSONException e) {
            Log.e(CONNECTION_TAG,JSON_EXCEPTION);
            e.printStackTrace();
        }

        return list_of_movies;
    }

    @Override
    protected void onPostExecute(List<MovieInfoContainer> result) {
        if (result!=null && result.size()>0) {
            cache.clear();
            movieAdapter.clear(); // remove information from the last call
            List<MovieBasicInfo> movies = new ArrayList<>(result.size());
            for (MovieInfoContainer container : result)
                movies.add((MovieBasicInfo)container);
            cache.addAll(movies);
            movieAdapter.addAll(result);
        }
    }
}