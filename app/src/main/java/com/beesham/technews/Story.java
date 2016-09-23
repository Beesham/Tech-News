package com.beesham.technews;

/**
 * Created by Beesham on 9/22/2016.
 */
public class Story {
    private String mTitle;
    private String mUrl;
    private String mPubDate;

    public Story(String mTitle, String mUrl, String mPubDate) {
        this.mTitle = mTitle;
        this.mUrl = mUrl;
        this.mPubDate = mPubDate;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmPubDate() {
        return mPubDate;
    }

    public String getmTitle() {
        return mTitle;
    }
}
