package com.example.android.movierecomender;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.movierecomender.adapters.MovieDetailsAdapter;
import com.example.android.movierecomender.fetchers.FetchVideos;

import java.util.ArrayList;

public class DetailedMovieFragment extends Fragment {
    MovieDetailsAdapter movieAdapter = null;
    private boolean twoPanels = true;


    public DetailedMovieFragment() {
        super();
        Bundle arguments = getArguments();
        if (arguments!=null)
            twoPanels = true; //arguments.getBoolean("TWO_PANELS");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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


        Bundle arguments = getArguments();
        if (arguments!=null) {
            MovieBasicInfo movie = (MovieBasicInfo) arguments.getParcelable(MovieBasicInfo.class.getName());
            if (movie != null) {
                this.movieAdapter.add(movie);
                this.movieAdapter.add(movie);
                String key = getResources().getString(R.string.movie_db_key);
                String[] params = {movie.getId(), key};
                new FetchVideos(this.movieAdapter).execute(params);
            }
        }

        return rootView;
    }

    public interface Callback {
        /**
         * Callback for when an item has been selected.
         */
        public void onReviewsSelected(MovieBasicInfo movie);
    }
}
