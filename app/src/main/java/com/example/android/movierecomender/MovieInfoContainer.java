package com.example.android.movierecomender;

/**
 * This class serves as a container for the information related to a particular movie
 */
public class MovieInfoContainer {
    private final boolean   only_adults;
    private final String    original_title;
    private final String    original_language;
    private final String    movie_plot;
    private final String    release_date;
    private final String    poster_path;
    private final String    poster_path_base = "http://image.tmdb.org/t/p/w185";

    /**
     * Creates a MovieInfoContainer object with all the required information
     * @param only_adults
     * @param original_title
     * @param original_language
     * @param movie_plot
     * @param release_date
     * @param poster_path
     */
    public MovieInfoContainer(boolean only_adults, String original_title, String original_language, String movie_plot, String release_date, String poster_path) {
        this.only_adults = only_adults;
        this.original_title = original_title;
        this.original_language = original_language;
        this.movie_plot = movie_plot;
        this.release_date = release_date;
        this.poster_path = this.poster_path_base+poster_path;
    }

    /**
     * Indicates whether the movie is only for adults or not
     * @return <code>true</code> if the movie is only for adults and <code>false</code> if not
     */
    public boolean isOnlyForAdults() {
        return only_adults;
    }

    /**
     * Obtains the title of the movie
     * @return A <code>String</code> containing the title of the movie
     */
    public String getOriginalTitle() {
        return original_title;
    }

    /**
     * Obtains the movie original language
     * @return A <code>String</code> with the language code
     */
    public String getOriginalLanguage() {
        return original_language;
    }

    /**
     * Obtains the movie plot
     * @return A <code>String</code> with the movie plot
     */
    public String getMoviePlot() {
        return movie_plot;
    }

    /**
     * Obtains the release date of the movie
     * @return A <code>String</code> containing the release date of the movie
     */
    public String getReleaseDate() {
        return release_date;
    }

    /**
     * Obtains the path where to find the movie poster
     * @return A <code>String</code> with the path to the movie poster
     */
    public String getPosterPath() {
        return poster_path;
    }

    @Override
    /**
      * @return a <code>String</code> with all the information of a movie
     */
    public String toString() {
        String result = "Movie:\t";
        result += getOriginalTitle()+".\t";
        result += "Summary\t"+getMoviePlot() + ".\t";
        result += "Language:\t"+ getOriginalLanguage()+".\t";
        result += isOnlyForAdults() ? "Warning: +18." : "";
        return result;
    }

}
