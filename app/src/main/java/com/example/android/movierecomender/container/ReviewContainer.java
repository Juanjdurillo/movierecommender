package com.example.android.movierecomender.container;

import com.example.android.movierecomender.container.MovieInfoContainer;

/**
 * Class to store the information of a movie review
 */
public class ReviewContainer implements MovieInfoContainer {
    final private String author;
    final private String comment;

    public ReviewContainer(String author, String comment) {
        this.author = author;
        this.comment = comment;
    }
    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }
}
