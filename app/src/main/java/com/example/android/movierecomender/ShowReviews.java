package com.example.android.movierecomender;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.android.movierecomender.adapters.ReviewAdapter;
import com.example.android.movierecomender.fetchers.FetchReviews;

import java.util.ArrayList;

public class ShowReviews extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_details);
    }

    public static class ShowReviewsFragment extends Fragment {
        ArrayAdapter reviewAdapter = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_details, container, false);

            this.reviewAdapter = new ReviewAdapter(
                                this.getActivity(),
                                R.layout.fragment_details,
                                new ArrayList<ReviewContainer>()
            );




            Bundle arguments = getArguments();
            MovieBasicInfo movie = (MovieBasicInfo) arguments.getParcelable(MovieBasicInfo.class.getName());
            String key = getResources().getString(R.string.movie_db_key);
            String [] params = {movie.getId(), key};
            new FetchReviews(this.reviewAdapter).execute(params);

            return rootView;
        }
    }
}
