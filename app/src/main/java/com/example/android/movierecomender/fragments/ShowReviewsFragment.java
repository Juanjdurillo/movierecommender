package com.example.android.movierecomender.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.movierecomender.R;
import com.example.android.movierecomender.adapters.ReviewAdapter;
import com.example.android.movierecomender.container.MovieBasicInfo;
import com.example.android.movierecomender.container.MovieInfoContainer;
import com.example.android.movierecomender.container.ReviewContainer;
import com.example.android.movierecomender.fetchers.FetchReviews;
import com.example.android.movierecomender.utils.Utility;

import java.util.ArrayList;

public class ShowReviewsFragment extends Fragment {
    ArrayAdapter reviewAdapter       = null;
    ArrayList<ReviewContainer> cache = new ArrayList<ReviewContainer>();
    MovieBasicInfo             movie = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle sourceOfData;
        sourceOfData = (savedInstanceState != null) ? savedInstanceState : getArguments();
        if (sourceOfData != null) {
            movie  = (MovieBasicInfo) sourceOfData.getParcelable(MovieBasicInfo.class.getName());
            cache  = sourceOfData.getParcelableArrayList(ReviewContainer.class.getName());
        }

        View rootView = null;
        if (movie != null) {

            rootView = inflater.inflate(R.layout.fragment_reviews, container, false);

            this.reviewAdapter = new ReviewAdapter(
                    this.getActivity(),
                    R.layout.fragment_reviews,
                    new ArrayList<MovieInfoContainer>()
            );

            ListView listView = (ListView) rootView.findViewById(R.id.list_reviews);
            listView.setAdapter(this.reviewAdapter);

            if (cache == null || cache.size() == 0) {
                if (this.cache == null)
                    this.cache = new ArrayList<>();
                fetchReviews();
            }

        }

        return rootView;
    }

    public void fetchReviews() {
        if (Utility.isNetworkAvailable(this.getActivity())) {
            String key = getResources().getString(R.string.movie_db_key);
            String[] params = {new Integer(movie.getId()).toString(), key};
            this.reviewAdapter.add(movie);
            new FetchReviews(this.reviewAdapter,this.cache).execute(params);
        }
    }
}
