package com.example.android.movierecomender;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

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
 * This class implements a view for the movie recommended app.
 */
public class MovieRecommenderEntryPointFragment extends Fragment {

    //ArrayAdapter using to inflate the layout
    private MoviePosterAdapter movieAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this fragment should also show menu events
        setHasOptionsMenu(true);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            fetchMovies();
            return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_recommender_fragment, menu);
    }

    private void fetchMovies() {
        this.movieAdapter.clear();// remove from the adapter the movies fetched last time
        String sorting_key =
        PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.preferred_sorting_method_key),
                                                                               getString(R.string.default_sorting_method));
        FetchPopularMovies movieObtainer = new FetchPopularMovies();
        movieObtainer.execute(sorting_key);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_recomender_entry_point, container, false);

        this.movieAdapter = new MoviePosterAdapter(
                getActivity(),
                R.layout.fragment_movie_recomender_entry_point,
                new ArrayList<MovieInfoContainer>()
        );

        GridView listView = (GridView) rootView.findViewById(R.id.grid_movies);
        listView.setAdapter(this.movieAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfoContainer movie = movieAdapter.getItem(position);
                Intent intent = (new Intent(getActivity(),
                        ShowMovieDetails.class));
                Bundle b = new Bundle();
                b.putSerializable(MovieInfoContainer.class.getName(), movie);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchMovies();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchMovies();
    }

    public class FetchPopularMovies  extends AsyncTask<String, Void, List<MovieInfoContainer>> {


        // Stores my key for movieDB database
        // To do: move SRINGS into the String file
        public static final String MOVIE_DB_KEY                 = "";
        public static final String MOVIE_DB_URL                 =
                "http://api.themoviedb.org/3/discover/movie?";
        public static final String SORT_PARAMETER_LABEL         = "sort_by";
        public static final String KEY_PARAMETER_LABEL          = "api_key";

        public static final String CONNECTION_ERROR              = "IO ERROR, INTERNET CONNECTION";
        public static final String IO_OPERATION_ERROR            = "IO OPERATION ERROR";
        public static final String JSON_EXCEPTION                = "JSON PROCESSING EXCEPTON";
        public static final String JSON_MOVIE_EXCEPTION          = "Error detected. Some movies or movies' information may not been showed";

        // Tags for debugging
        public static final String CONNECTION_TAG                = "JJ";


        @Override
        /**
         * Connects to the MovieDB database and obtains the list of most popular movies
         * <code>params</code> It contains only one parameter, which is the value the user wants
         * the movies sorted by
         */
        protected List<MovieInfoContainer> doInBackground(String... params) {


            Uri uri_builder = Uri.parse(MOVIE_DB_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAMETER_LABEL, params[0])
                    .appendQueryParameter(KEY_PARAMETER_LABEL,MOVIE_DB_KEY).build();
            Log.d(CONNECTION_TAG,uri_builder.toString());

            HttpURLConnection           movieDBConnection = null;
            String                      jSonStr           = null;
            List<MovieInfoContainer>    res               = null;


            try {

                movieDBConnection =  (HttpURLConnection)(new URL(uri_builder.toString())).openConnection();

                try (InputStreamReader inputStreamReader = new InputStreamReader(movieDBConnection.getInputStream());
                     BufferedReader    dataBuffer        = new BufferedReader(inputStreamReader)) {
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
         * @return a <code>List<MovieInfoContainer></code> with all the movies included in the
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
                        MovieInfoContainer movie;
                        movie = new MovieInfoContainer(is_adult_movie,movie_title,movie_language,movie_plot,movie_release,movie_poster_path,movie_average_votes);
                        Log.d(CONNECTION_TAG,movie.toString());
                        list_of_movies.add(movie);
                    }
                } catch (JSONException e1) {
                    Log.e(CONNECTION_TAG,JSON_MOVIE_EXCEPTION);
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
                movieAdapter.addAll(result);
            }
        }
    }
}
