package com.example.android.movierecomender;


import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ShowMovieDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movie_details);

        if (savedInstanceState==null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieInfoContainer.class.getName(), (Parcelable) ((Bundle) getIntent().getExtras()).getSerializable(MovieInfoContainer.class.getName()));
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
        List<YouToubeLink> cache = new ArrayList<YouToubeLink>();
        TrailersAdapter trailersAdapter = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_show_movie_details, container, false);
            Bundle arguments = getArguments();
            if (arguments!=null) {
                MovieInfoContainer movie = (MovieInfoContainer) arguments.getParcelable(MovieInfoContainer.class.getName());


                // Fetch for the YouTube links here
                String key = getResources().getString(R.string.movie_db_key);
                String [] params = {movie.getId(), key};
                this.trailersAdapter = new TrailersAdapter(
                        getActivity().getBaseContext(),
                        R.id.listview_trailers,
                        new ArrayList<YouToubeLink>());
                new FetchVideos(this.trailersAdapter,this.cache).execute(params);



                ListView listView = (ListView) rootView.findViewById(R.id.listview_trailers);
                listView.setAdapter(this.trailersAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        YouToubeLink movie = trailersAdapter.getItem(position);
                /*Intent intent = (new Intent(getActivity(),
                    ShowMovieDetails.class));
                    Bundle b = new Bundle();
                    b.putSerializable(MovieInfoContainer.class.getName(), movie);
                    intent.putExtras(b);
                    startActivity(intent);*/
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getLink()));
                        intent.putExtra("force_fullscreen",true);
                        startActivity(intent);
                    }
                });



                    ((TextView) rootView.findViewById(R.id.movie_title)).setText(movie.getOriginalTitle());
                    ((TextView) rootView.findViewById(R.id.movie_language)).setText(getResources().getString(R.string.movie_language_label) + "\t" + movie.getOriginalLanguage());
                    ((TextView) rootView.findViewById(R.id.release_date)).setText(getResources().getString(R.string.release_date_label) + "\t" + movie.getReleaseDate());
                    ((TextView) rootView.findViewById(R.id.average_votes)).setText(getResources().getString(R.string.average_votes_label) + "\t" + movie.getAverage_votes());

                    ((TextView) rootView.findViewById(R.id.movie_plot)).setText(getResources().getString(R.string.movie_plot_label) + "\t" + movie.getMoviePlot());
                    Picasso.with(rootView.getContext()).load(movie.getPosterPath()).into(
                            (ImageView) rootView.findViewById(R.id.movie_thumnail));
                    ((ImageView) rootView.findViewById(R.id.movie_thumnail)).setVisibility(ImageView.VISIBLE);








            }

            return rootView;
        }
    }

}
