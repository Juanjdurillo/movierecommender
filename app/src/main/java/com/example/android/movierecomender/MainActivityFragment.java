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
public class MainActivityFragment extends Fragment {

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
        PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.preferred_sorting_method_key),getString(R.string.default_sorting_method));
        new FetchPopularMovies(this.movieAdapter).execute(sorting_key);
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

    /*
    @Override
    public void onResume() {
        super.onResume();
        fetchMovies();
    }
    */

}
