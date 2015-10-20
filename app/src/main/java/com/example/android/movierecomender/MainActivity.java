package com.example.android.movierecomender;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.movierecomender.container.MovieBasicInfo;


public class MainActivity extends ActionBarActivity implements PopularMoviesFragment.Callback,
                                                               DetailedMovieFragment.Callback{

    boolean twoPanelVersion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container)!=null) {
            //there is a container for the movie detail fragment
            twoPanelVersion = true;
            if (savedInstanceState==null)
                getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, new DetailedMovieFragment()).commit();

        } else {
            twoPanelVersion = false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_recomender_entry_point, menu);
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
            startActivity(new Intent(this, SortPreferencesSetting.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(MovieBasicInfo movie) {
        if (twoPanelVersion) {
            Bundle args = new Bundle();
            args.putParcelable(MovieBasicInfo.class.getName(), movie);
            Log.e("TWO_PANELS", "Pongo true");
            args.putBoolean("TWO_PANELS",true);

            DetailedMovieFragment fragment = new DetailedMovieFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            Intent intent = (new Intent(this, ShowMovieDetails.class));
            Bundle b = new Bundle();
            b.putSerializable(MovieBasicInfo.class.getName(), movie);
            Log.e("TWO_PANELS", "Pongo falso");
            b.putBoolean("TWO_PANELS",false);
            intent.putExtras(b);
            startActivity(intent);
        }
    }


    @Override
    public void onReviewsSelected(MovieBasicInfo movie) {
        if (twoPanelVersion) {
            Bundle args = new Bundle();
            args.putParcelable(MovieBasicInfo.class.getName(), movie);

            ShowReviewsFragment fragment = new ShowReviewsFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        }
    }
}
