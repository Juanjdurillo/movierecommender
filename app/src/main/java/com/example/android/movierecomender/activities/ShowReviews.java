package com.example.android.movierecomender.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.example.android.movierecomender.R;
import com.example.android.movierecomender.container.MovieBasicInfo;
import com.example.android.movierecomender.fragments.ShowReviewsFragment;

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
