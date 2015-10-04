package com.example.android.movierecomender;

import java.net.URL;

/**
 * Created by JuanJose on 10/3/2015.
 */
public class YouToubeLink {
    private static String youTubeSite = "https://www.youtube.com/watch?v=";
    private String key;
    private String name;
    public YouToubeLink(String key, String name) {
        this.key = key;this.name = name;
    }

    public String getLink() {
        return youTubeSite+this.key;
    }

    public String getName() {
        return this.name;
    }
}
