package com.example.android.movierecomender.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.movierecomender.container.MovieBasicInfo;
import com.example.android.movierecomender.container.MovieInfoContainer;
import com.example.android.movierecomender.R;
import com.example.android.movierecomender.container.ReviewContainer;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<MovieInfoContainer>{
    Context context;

    public ReviewAdapter(Context context, int resource, ArrayList<MovieInfoContainer> movieInfoContainers) {
        super(context,resource);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MovieInfoContainer container = getItem(position);
        View view = null;

        if (container instanceof MovieBasicInfo) {
            MovieBasicInfo movie = (MovieBasicInfo) container;
            view = MovieDetailsAdapter.MovieDetailsView.getMovieView(movie,context,parent);

        } else if (container instanceof ReviewContainer){
            ReviewContainer review = (ReviewContainer) container;
            view = LayoutInflater.from(context).inflate(R.layout.review_layout, parent, false);
            ((TextView) view.findViewById(R.id.review_author)).setText("Author: " + review.getAuthor());
            ((TextView) view.findViewById(R.id.review_content)).setText("Review: " + review.getComment());
        }
        return view;
    }


}
