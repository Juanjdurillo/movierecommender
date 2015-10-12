package com.example.android.movierecomender;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.movierecomender.adapters.MovieDetailsAdapter;
import com.example.android.movierecomender.fetchers.FetchVideos;

import java.util.ArrayList;


public class ShowMovieDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movie_details);

        if (savedInstanceState==null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieBasicInfo.class.getName(), (Parcelable) ((Bundle) getIntent().getExtras()).getSerializable(MovieBasicInfo.class.getName()));
            DetailedMovieFragment fragment = new DetailedMovieFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DetailedMovieFragment extends Fragment {
        MovieDetailsAdapter movieAdapter = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_details, container, false);

            this.movieAdapter = new MovieDetailsAdapter(
                    this.getActivity(),
                    R.layout.fragment_details,
                    new ArrayList<MovieInfoContainer>()
            );

            ListView listView = (ListView) rootView.findViewById(R.id.list_movie);
            listView.setAdapter(this.movieAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieInfoContainer movie = movieAdapter.getItem(position);
                    if (movie instanceof MovieVideoLink) {
                        MovieVideoLink link = (MovieVideoLink) movie;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getLink()));
                        intent.putExtra("force_fullscreen", true);
                        startActivity(intent);
                    }
                }
            });


            Bundle arguments = getArguments();
            MovieBasicInfo movie = (MovieBasicInfo) arguments.getParcelable(MovieBasicInfo.class.getName());
            this.movieAdapter.add(movie);


            String key = getResources().getString(R.string.movie_db_key);
            String [] params = {movie.getId(), key};
            new FetchVideos(this.movieAdapter).execute(params);

            return rootView;
        }
    }

}
