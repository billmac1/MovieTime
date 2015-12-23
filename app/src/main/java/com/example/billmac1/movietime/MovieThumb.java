package com.example.billmac1.movietime;

/**
 * Created by billmac1 on 12/22/2015.
 */
public class     MovieThumb {

    String movieTitle;
    String movieThumbPath;
    String movieOverview;
    String movieVoteAverage;
    String movieReleaseDate;


    public MovieThumb(String mTitle, String mThumbPath, String mOverview, String mVoteAverage,
                      String mReleaseDate)
    {
        this.movieTitle = mTitle;
        this.movieThumbPath = mThumbPath;
        this.movieOverview = mOverview;
        this.movieVoteAverage = mVoteAverage;
        this.movieReleaseDate = mReleaseDate;
    }

}
