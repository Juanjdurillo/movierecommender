package com.example.android.movierecomender.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.movierecomender.R;
import com.example.android.movierecomender.activities.ShowReviews;
import com.example.android.movierecomender.adapters.MovieDetailsAdapter;
import com.example.android.movierecomender.container.MovieBasicInfo;
import com.example.android.movierecomender.container.MovieInfoContainer;
import com.example.android.movierecomender.container.MovieVideoLink;
import com.example.android.movierecomender.fetchers.FetchVideos;
import com.example.android.movierecomender.utils.Utility;

import java.util.ArrayList;

public class DetailedMovieFragment extends Fragment {
    MovieDetailsAdapter         movieAdapter = null;
    private boolean             twoPanels = true;
    private MovieInfoContainer  movie = null;
    private ArrayList<MovieVideoLink> cache = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        Bundle sourceOfData;

        sourceOfData = (savedInstanceState != null) ? savedInstanceState : getArguments();
        if (sourceOfData != null) {
            movie     = sourceOfData.getParcelable(MovieBasicInfo.class.getName());
            twoPanels = sourceOfData.getBoolean("TWO_PANELS");
            cache     = sourceOfData.getParcelableArrayList(MovieVideoLink.class.getName());
        } else {
            return null;
        }

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        this.movieAdapter = new MovieDetailsAdapter(
                this.getActivity(),
                R.layout.fragment_details,
                new ArrayList<MovieInfoContainer>()
        );
        ListView listView = (ListView) rootView.findViewById(R.id.list_movie);
        listView.setAdapter(this.movieAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfoContainer movie = movieAdapter.getItem(position);
                if (movie instanceof MovieVideoLink) {
                    MovieVideoLink link = (MovieVideoLink) movie;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getLink()));
                    intent.putExtra("force_fullscreen", true);
                    startActivity(intent);
                } else if (position == 1) {
                    if (!twoPanels) {
                        Intent intent = (new Intent(getActivity(), ShowReviews.class));
                        Bundle b = new Bundle();
                        b.putSerializable(MovieBasicInfo.class.getName(), movie);
                        intent.putExtras(b);
                        getActivity().startActivity(intent);
                    } else {
                        ((Callback) getActivity()).onReviewsSelected((MovieBasicInfo) movie);
                    }

                }
            }
        });

        this.movieAdapter.add(movie);
        this.movieAdapter.add(movie);
        if (this.cache==null || this.cache.size()==0) {
            if (this.cache == null)
                this.cache = new ArrayList<>();
            fetchMovies();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(MovieBasicInfo.class.getName(), (MovieBasicInfo) movie);;
        savedInstanceState.putBoolean("TWO_PANELS", twoPanels);
        if (cache.size() > 0) {
            savedInstanceState.putParcelableArrayList(MovieVideoLink.class.getName(), this.cache);
        }

    }

    public void fetchMovies() {
        if (Utility.isNetworkAvailable(getActivity())) {
            String key = getResources().getString(R.string.movie_db_key);
            String[] params = {new Integer(((MovieBasicInfo) movie).getId()).toString(), key};
            new FetchVideos(this.movieAdapter, this.cache).execute(params);
        }
    }

    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        public void onReviewsSelected(MovieBasicInfo movie);
    }
}
