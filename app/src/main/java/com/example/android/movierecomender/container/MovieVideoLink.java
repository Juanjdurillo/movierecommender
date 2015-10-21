package com.example.android.movierecomender.container;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by JuanJose on 10/3/2015.
 */
public class MovieVideoLink implements Serializable, Parcelable, MovieInfoContainer {
    private static String youTubeSite = "https://www.youtube.com/watch?v=";
    private String key;
    private String name;
    public MovieVideoLink(String key, String name) {
        this.key = key;this.name = name;
    }

    public MovieVideoLink(Parcel parcel) {
        MovieVideoLink other = (MovieVideoLink) parcel.readSerializable();
        this.key    = other.key;
        this.name   = other.name;
    }

    public String getLink() {
        return youTubeSite+this.key;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }

    public static final Parcelable.Creator<MovieVideoLink> CREATOR = new Parcelable.Creator<MovieVideoLink>() {
        public MovieVideoLink createFromParcel(Parcel in) {
            return new MovieVideoLink(in);
        }

        public MovieVideoLink[] newArray(int size) {
            return new MovieVideoLink[size];
        }
    };
}
