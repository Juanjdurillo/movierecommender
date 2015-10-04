package com.example.android.movierecomender;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by JuanJose on 10/3/2015.
 */
public class TrailersAdapter extends ArrayAdapter<YouToubeLink> implements Serializable {
    Context context;

    public TrailersAdapter(Context context, int resource, ArrayList<YouToubeLink> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        YouToubeLink movie = getItem(position);
        TextView textView;
        if (convertView==null) {
            textView = new TextView(this.context);
        } else {
            textView = (TextView) convertView;
        }
        textView.setText(movie.getName());

        textView.setTextAppearance(context, android.R.style.TextAppearance_Large);
        textView.setTextColor(Color.rgb(255,0,0));
        return textView;
    }
}
