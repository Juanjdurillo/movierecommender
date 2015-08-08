package com.example.android.movierecomender;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Array adapter to feed a list_view with Movie info
 */
public class MovieInfoArrayAdapter extends ArrayAdapter<MovieInfoContainer> {
    Context context;

    public MovieInfoArrayAdapter(Context context, int resource, ArrayList<MovieInfoContainer> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    // I saw other examples using a holder view. I also used one myself for the Spotify streamer
    // application needed to be developed for June. I'm trying not to use it here to see the
    // potential benefits and drawbacks of one and the other approach
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieInfoContainer movie = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) this.context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = mInflater.inflate(R.layout.movie_item_layaout, null);

        ((TextView) convertView.findViewById(R.id.movie_title)).setText(movie.getOriginalTitle());
        ((TextView) convertView.findViewById(R.id.movie_language)).setText(movie.getOriginalLanguage());

        if (movie.isOnlyForAdults())
            ((TextView) convertView.findViewById(R.id.movie_adult)).setText("+18");

        ((TextView) convertView.findViewById(R.id.movie_plot)).setText(movie.getMoviePlot());
        Picasso.with(this.context).load(movie.getPosterPath()).into(
        (ImageView) convertView.findViewById(R.id.movie_poster));
        ((ImageView) convertView.findViewById(R.id.movie_poster)).setVisibility(ImageView.VISIBLE);

        return convertView;
    }
}
