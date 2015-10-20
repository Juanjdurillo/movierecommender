package com.example.android.movierecomender.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movierecomender.container.MovieBasicInfo;
import com.example.android.movierecomender.container.MovieInfoContainer;
import com.example.android.movierecomender.container.MovieVideoLink;
import com.example.android.movierecomender.R;
import com.example.android.movierecomender.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by juan on 10/12/15.
 */
public class MovieDetailsAdapter extends ArrayAdapter<MovieInfoContainer> implements Serializable {


    Context context;

    public MovieDetailsAdapter(Context context, int resource, ArrayList<MovieInfoContainer> movieInfoContainers) {
        super(context,resource);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MovieInfoContainer movieContainer = getItem(position);
        View view = null;

        if (movieContainer instanceof MovieBasicInfo && position==0) {
            view = MovieDetailsView.getMovieView((MovieBasicInfo)movieContainer,context,parent);
        } else if (movieContainer instanceof MovieVideoLink){
            view = LayoutInflater.from(context).inflate(R.layout.trailer_layout, parent, false);
            final MovieVideoLink link = (MovieVideoLink) movieContainer;
            ((TextView) view.findViewById(R.id.trailer_title)).setText(link.getName());
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.trailer_layout, parent, false);
            ((TextView) view.findViewById(R.id.trailer_title)).setText("See reviews");
        }
        return view;
    }



    static class MovieDetailsView {
        public static View getMovieView(MovieBasicInfo movie_param, Context context_param, ViewGroup parent) {
            final Context context       = context_param;
            final MovieBasicInfo movie  = movie_param;
            View view                   = LayoutInflater.from(context).inflate(R.layout.movie_layout, parent, false);
            ((TextView) view.findViewById(R.id.movie_title)).setText(movie.getOriginalTitle());
            ((TextView) view.findViewById(R.id.movie_language)).setText(context.getResources().getString(R.string.movie_language_label) + "\t" + movie.getOriginalLanguage());
            ((TextView) view.findViewById(R.id.release_date)).setText(context.getResources().getString(R.string.release_date_label) + "\t" + movie.getReleaseDate());
            ((TextView) view.findViewById(R.id.average_votes)).setText(context.getResources().getString(R.string.average_votes_label) + "\t" + movie.getAverage_votes());
            final TextView add_to_favourites = ((TextView) view.findViewById(R.id.add_to_favourites));
            if (locallyStoredMovie(movie,context))
                add_to_favourites.setText(context.getResources().getString(R.string.remove_from_favourites));
            else
                add_to_favourites.setText(context.getResources().getString(R.string.add_to_favourites));
            ((TextView) view.findViewById(R.id.add_to_favourites)).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //movie will be inserted in the database

                            if (!locallyStoredMovie(movie,context)) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(MovieContract.MovieEntry._ID,movie.getId());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_ADULTS,movie.isOnlyForAdults()?1:0);
                                contentValues.put(MovieContract.MovieEntry.COLUMN_PEOPLE_VOTES,movie.getAverage_votes());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_SUMMARY, movie.getMoviePlot());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_LANGUAGE, movie.getOriginalLanguage());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URI, movie.getPosterPath());
                                context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                                add_to_favourites.setText(context.getResources().getString(R.string.remove_from_favourites));
                            } else {
                                context.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,MovieContract.MovieEntry._ID+"=\""+movie.getId()+"\"",null);
                                add_to_favourites.setText(context.getResources().getString(R.string.add_to_favourites));
                            }
                        }
                    }
            );
            ((TextView) view.findViewById(R.id.movie_plot)).setText(context.getResources().getString(R.string.movie_plot_label) + "\t" + movie.getMoviePlot());
            Picasso.with(view.getContext()).load(movie.getPosterPath()).into((ImageView) view.findViewById(R.id.movie_thumnail));
            ((ImageView) view.findViewById(R.id.movie_thumnail)).setVisibility(ImageView.VISIBLE);
            return view;

        }

        static boolean locallyStoredMovie(MovieBasicInfo movie, Context context) {
            Cursor cursor = context.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    MovieContract.MovieEntry._ID + "=\"" + movie.getId() + "\"",
                    null,
                    null);
            if (cursor.getCount() > 0)
                return true;
            else
                return false;

        }
    }

}
