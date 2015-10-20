package com.example.android.movierecomender.container;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * This class serves as a container for the information related to a particular movie. Basically, it
 * only holds/record information. It does not provide any behaviour besides accessing to the movie
 * fields.
 * Implements <code>Serializable</code> so it can be passed between intents using a bundle object
 */
public class MovieBasicInfo implements Serializable, Parcelable, MovieInfoContainer {
    private final boolean   only_adults;
    private final String    original_title;
    private final String    original_language;
    private final String    movie_plot;
    private final String    release_date;
    private final String    poster_path;
    private final double    average_votes;
    private final int    id;

    public int getId() {
        return id;
    }

    private final String    poster_path_base = "http://image.tmdb.org/t/p/w185";

    /**
     * Creates a MovieBasicInfo object with all the required information
     * @param only_adults
     * @param original_title
     * @param original_language
     * @param movie_plot
     * @param release_date
     * @param poster_path
     * @param average_votes
     * @param id
     */
    public MovieBasicInfo(boolean only_adults, String original_title, String original_language, String movie_plot, String release_date, String poster_path, double average_votes, int id) {
        this.only_adults        = only_adults;
        this.original_title     = original_title;
        this.original_language  = original_language;
        this.movie_plot         = movie_plot;
        this.release_date       = release_date;
        this.poster_path        = this.poster_path_base+poster_path;
        this.average_votes      = average_votes;
        this.id                 = id;
    }

    /**
     * Creates MovieBasicInfo from a Parceable object
     */
    public MovieBasicInfo(Parcel parcel) {
        MovieBasicInfo other = (MovieBasicInfo) parcel.readSerializable();
        this.only_adults        = other.only_adults;
        this.original_title     = other.original_title;
        this.original_language  = other.original_language;
        this.movie_plot         = other.movie_plot;
        this.release_date       = other.release_date;
        this.poster_path        = other.poster_path;
        this.average_votes      = other.average_votes;
        this.id                 = other.id;
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

    /**
     * Obtains the average votes for this movie
     * @return A <code>String</code> with the average votes
     */
    public double getAverage_votes() {
        return average_votes;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }

    public static final Parcelable.Creator<MovieBasicInfo> CREATOR = new Parcelable.Creator<MovieBasicInfo>() {
        public MovieBasicInfo createFromParcel(Parcel in) {
            return new MovieBasicInfo(in);
        }

        public MovieBasicInfo[] newArray(int size) {
            return new MovieBasicInfo[size];
        }
    };
}
