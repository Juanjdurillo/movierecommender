package com.example.android.movierecomender;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements PopularMoviesFragment.Callback {

    boolean twoPanelVersion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container)!=null) {
            //there is a container for the movie detail fragment
            twoPanelVersion = true;
            if (savedInstanceState==null)
                getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, new ShowMovieDetails.DetailedMovieFragment()).commit();

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
    public void onItemSelected(MovieInfoContainer movie) {
        if (twoPanelVersion) {
            Log.e("se toco con la mano","se toco con la mano");
            Bundle args = new Bundle();
            args.putParcelable(MovieInfoContainer.class.getName(), movie);

            ShowMovieDetails.DetailedMovieFragment fragment = new ShowMovieDetails.DetailedMovieFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            Intent intent = (new Intent(this, ShowMovieDetails.class));
            Bundle b = new Bundle();
            b.putSerializable(MovieInfoContainer.class.getName(), movie);
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}
