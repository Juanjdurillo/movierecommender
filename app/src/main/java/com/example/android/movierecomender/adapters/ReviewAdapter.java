package com.example.android.movierecomender.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.movierecomender.R;
import com.example.android.movierecomender.ReviewContainer;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<ReviewContainer>{
    Context context;

    public ReviewAdapter(Context context, int resource, ArrayList<ReviewContainer> movieInfoContainers) {
        super(context,resource);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewContainer review = getItem(position);
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.review_entry_layout, parent, false);
        ((TextView) view.findViewById(R.id.review_author)).setText("Author: "+review.getAuthor());
        ((TextView) view.findViewById(R.id.review_content)).setText("Review: "+review.getComment());
        return view;
    }


}
