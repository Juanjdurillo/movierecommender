package com.example.android.movierecomender;

/**
 * Created by JuanJose on 10/3/2015.
 */
public class MovieVideoLink implements MovieInfoContainer {
    private static String youTubeSite = "https://www.youtube.com/watch?v=";
    private String key;
    private String name;
    public MovieVideoLink(String key, String name) {
        this.key = key;this.name = name;
    }

    public String getLink() {
        return youTubeSite+this.key;
    }

    public String getName() {
        return this.name;
    }
}
