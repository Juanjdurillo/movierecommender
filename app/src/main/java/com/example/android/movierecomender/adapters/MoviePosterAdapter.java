package com.example.android.movierecomender.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;


import com.example.android.movierecomender.container.MovieBasicInfo;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Array adapter to feed a list_view with Movie info
 */
public class MoviePosterAdapter extends ArrayAdapter<MovieBasicInfo> implements Serializable {
    Context context;

    public MoviePosterAdapter(Context context, int resource, ArrayList<MovieBasicInfo> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    // I saw other examples using a holder view. I also used one myself for the Spotify streamer
    // application needed to be developed for June. I'm trying not to use it here to see the
    // potential benefits and drawbacks of one and the other approach
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieBasicInfo movie = getItem(position);
        ImageView imageView;

        if (convertView==null) {
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new GridView.LayoutParams(parent.getWidth()/2,  (int) ((parent.getWidth()/2) * 1.5)));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(this.context).load(movie.getPosterPath()).fit().into(imageView);
        imageView.setVisibility(ImageView.VISIBLE);
        return imageView;
    }
}
