package com.example.android.movierecomender;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.movierecomender.adapters.ReviewAdapter;
import com.example.android.movierecomender.fetchers.FetchReviews;

import java.util.ArrayList;

public class ShowReviews extends ActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("SHOWREVIEWSACTIVITY", "Acivity is called");
        setContentView(R.layout.activity_reviews);
        if (savedInstanceState==null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MovieBasicInfo.class.getName(), (Parcelable) ((Bundle) getIntent().getExtras()).getSerializable(MovieBasicInfo.class.getName()));
            ShowReviewsFragment fragment = new ShowReviewsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_review_container, fragment)
                    .commit();
        }
    }
}
