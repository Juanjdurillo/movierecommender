package com.example.android.movierecomender;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.movierecomender.adapters.ReviewAdapter;
import com.example.android.movierecomender.container.MovieBasicInfo;
import com.example.android.movierecomender.container.MovieInfoContainer;
import com.example.android.movierecomender.fetchers.FetchReviews;

import java.util.ArrayList;

public class ShowReviewsFragment extends Fragment {
    ArrayAdapter reviewAdapter = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);

        this.reviewAdapter = new ReviewAdapter(
                this.getActivity(),
                R.layout.fragment_reviews,
                new ArrayList<MovieInfoContainer>()
        );

        ListView listView = (ListView) rootView.findViewById(R.id.list_reviews);
        listView.setAdapter(this.reviewAdapter);


        Bundle arguments = getArguments();
        MovieBasicInfo movie = (MovieBasicInfo) arguments.getParcelable(MovieBasicInfo.class.getName());
        String key = getResources().getString(R.string.movie_db_key);
        String [] params = {new Integer(movie.getId()).toString(), key};
        this.reviewAdapter.add(movie);
        new FetchReviews(this.reviewAdapter).execute(params);

        return rootView;
    }
}
