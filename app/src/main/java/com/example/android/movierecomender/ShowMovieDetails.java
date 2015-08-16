package com.example.android.movierecomender;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ShowMovieDetails extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movie_details);
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
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_show_movie_details, container, false);
            if (intent!=null) {
                Bundle b = intent.getExtras();
                MovieInfoContainer movie = (MovieInfoContainer) b.getSerializable(MovieInfoContainer.class.getName());
                Log.d("JJ", movie.toString());
                Log.d("JJ", rootView.toString());
                ((TextView) rootView.findViewById(R.id.movie_title)).setText(movie.getOriginalTitle());
                ((TextView) rootView.findViewById(R.id.movie_language)).setText("language:"+"\t"+movie.getOriginalLanguage());
                ((TextView) rootView.findViewById(R.id.release_date)).setText("Release date:"+"\t"+movie.getReleaseDate());
                ((TextView) rootView.findViewById(R.id.average_votes)).setText("Votes:"+"\t"+movie.getAverage_votes());

                ((TextView) rootView.findViewById(R.id.movie_plot)).setText("Summary:"+"\t"+movie.getMoviePlot());
                Picasso.with(rootView.getContext()).load(movie.getPosterPath()).into(
                        (ImageView) rootView.findViewById(R.id.movie_thumnail));
                ((ImageView) rootView.findViewById(R.id.movie_thumnail)).setVisibility(ImageView.VISIBLE);

            }
            return rootView;
        }
    }

}
