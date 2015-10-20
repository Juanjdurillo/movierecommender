package com.example.android.movierecomender.fetchers;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.android.movierecomender.container.MovieInfoContainer;
import com.example.android.movierecomender.container.MovieVideoLink;

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
 * Created by JuanJose on 10/3/2015.
 */
public class FetchVideos extends AsyncTask<String, Void, List<MovieInfoContainer>> {

    private ArrayAdapter movieAdapter;

    public static final String MOVIE_DB_URL                 =
            "http://api.themoviedb.org/3/movie/";
    public static final String VIDEOS_LABEL                 = "/videos?";
    public static final String KEY_PARAMETER_LABEL          = "api_key";
    public static final String CONNECTION_ERROR              = "IO ERROR, INTERNET CONNECTION";
    public static final String IO_OPERATION_ERROR            = "IO OPERATION ERROR";
    public static final String JSON_EXCEPTION                = "JSON PROCESSING EXCEPTON";
    public static final String JSON_MOVIE_EXCEPTION          = "Error detected. Some movies or movies' information may not been showed";

    // Tags for debugging
    public static final String CONNECTION_TAG                = "CONNECTION";


    public FetchVideos(ArrayAdapter adapter) {
        this.movieAdapter = adapter;
    }


    @Override
    /**
     * Connects to the MovieDB database and obtains the list of most popular movies
     * @param <code>params</code> is an array of length
     * <code>params[0]</code> indicates the movie id
     * <code>params[1]</code> indicates the movie_db user key
     */
    protected List<MovieInfoContainer> doInBackground(String... params) {

        Uri uri_builder = Uri.parse(MOVIE_DB_URL + params[0] + VIDEOS_LABEL).buildUpon()
                                   .appendQueryParameter(KEY_PARAMETER_LABEL, params[1]).build();

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
     * This method parses a json string, extracting all the trailers links
     */
    List<MovieInfoContainer> parseMovieInformaton(String jSonStr) {
        if (jSonStr == null || jSonStr.isEmpty())
            return null;

        List<MovieInfoContainer>    list_of_links = null;

        final String ARRAY_OF_LINKS      = "results";
        final String KEY_LABEL           = "key";
        final String NAME_LABEL          = "name";
        try {
            JSONArray array_of_json_movies = (new JSONObject(jSonStr)).getJSONArray(ARRAY_OF_LINKS);
            list_of_links = new ArrayList<>(array_of_json_movies.length());
            try {
                for (int i = 0; i < array_of_json_movies.length(); i++) {
                    JSONObject  json_movie          = array_of_json_movies.getJSONObject(i)             ;
                    String      link                = json_movie.getString(KEY_LABEL)         ;
                    String      name                = json_movie.getString(NAME_LABEL);
                    MovieVideoLink youToubeLink;
                    youToubeLink = new MovieVideoLink(link,name);
                    Log.d(CONNECTION_TAG,youToubeLink.getLink());
                    list_of_links.add(youToubeLink);
                }
            } catch (JSONException e1) {
                Log.e(CONNECTION_TAG,JSON_MOVIE_EXCEPTION);
            }

        } catch (JSONException e) {
            Log.e(CONNECTION_TAG,JSON_EXCEPTION);
            e.printStackTrace();
        }

        return list_of_links;
    }

    @Override
    protected void onPostExecute(List<MovieInfoContainer> result) {
        if (result!=null && result.size()>0) {
            movieAdapter.addAll(result);
        }
    }
}
