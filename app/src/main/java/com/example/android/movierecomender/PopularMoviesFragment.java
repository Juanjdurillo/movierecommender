package com.example.android.movierecomender;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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

import com.example.android.movierecomender.adapters.MoviePosterAdapter;
import com.example.android.movierecomender.fetchers.FetchPopularMovies;

import java.util.ArrayList;


/**
 * This class implements a view for the movie recommended app.
 */
public class PopularMoviesFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener
                                                                 {

    // ArrayAdapter feeding the GridView in the main Activity
    private MoviePosterAdapter movieAdapter = null;
    private ArrayList<MovieBasicInfo> cache  =  new ArrayList<>(20);
    private String sortingKey;
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this fragment should also show menu events (the refresh menu)
        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener((SharedPreferences.OnSharedPreferenceChangeListener) this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            fetchMovies(); // if the button has been clicked retrieve movies
            return true;
        }
        return onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(MovieBasicInfo.class.getName(),this.cache);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_recommender_fragment, menu);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        this.movieAdapter = new MoviePosterAdapter(
                    getActivity(),
                    R.layout.fragment_main,
                    new ArrayList<MovieBasicInfo>()
        );

        GridView listView = (GridView) rootView.findViewById(R.id.grid_movies);
        listView.setAdapter(this.movieAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieBasicInfo movie = movieAdapter.getItem(position);
                /*Intent intent = (new Intent(getActivity(),
                    ShowMovieDetails.class));
                    Bundle b = new Bundle();
                    b.putSerializable(MovieBasicInfo.class.getName(), movie);
                    intent.putExtras(b);
                    startActivity(intent);*/
                    ((Callback) getActivity()).onItemSelected(movie);
                }
            });
        if (savedInstanceState != null && !savedInstanceState.isEmpty())
            this.cache = savedInstanceState.getParcelableArrayList(MovieBasicInfo.class.getName());

        if (this.cache.size() > 0)
            this.movieAdapter.addAll(this.cache);
        else
            this.fetchMovies();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * This method will create a <code>FetchPopularMovies</code> object to retrieve movies from
     * the database. The <code>FetchPopularMovies</code> extends <code>AsyncTask</code>
     */
    private void fetchMovies() {
        if (!this.isNetworkAvailable()) {
            Log.e("Network is not ", "Network is not available");
            return;
        }
        Log.e("Network is avaie", "Network is aable");
        sortingKey = Utility.getCurrentSortingMethod(this.getActivity().getBaseContext());
        String key = getResources().getString(R.string.movie_db_key);
        String [] params = {sortingKey, key};
        new FetchPopularMovies(this.movieAdapter,this.cache).execute(params);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String newSortingKey = Utility.getCurrentSortingMethod(this.getActivity().getBaseContext());
        if (!newSortingKey.equals(sortingKey))
            this.fetchMovies();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }

     /**
      * A callback interface that all activities containing this fragment must
      * implement. This mechanism allows activities to be notified of item
      * selections.
      */
     public interface Callback {
         /**
          * DetailFragmentCallback for when an item has been selected.
          */
         public void onItemSelected(MovieBasicInfo movie);
     }


 }
