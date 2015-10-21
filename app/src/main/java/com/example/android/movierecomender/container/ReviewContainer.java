package com.example.android.movierecomender.container;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Class to store the information of a movie review
 */
public class ReviewContainer implements Serializable, Parcelable,MovieInfoContainer {
    final private String author;
    final private String comment;

    public ReviewContainer(String author, String comment) {
        this.author = author;
        this.comment = comment;
    }

    public ReviewContainer(Parcel parcel) {
        ReviewContainer other = (ReviewContainer) parcel.readSerializable();
        this.author = other.author;
        this.comment = other.comment;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }

    public static final Parcelable.Creator<ReviewContainer> CREATOR = new Parcelable.Creator<ReviewContainer>() {
        public ReviewContainer createFromParcel(Parcel in) {
            return new ReviewContainer(in);
        }

        public ReviewContainer[] newArray(int size) {
            return new ReviewContainer[size];
        }
    };
}
