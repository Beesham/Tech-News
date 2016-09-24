package com.beesham.technews;

/**
 * Created by Beesham on 9/22/2016.
 */
public class Story {
    private String mTitle;
    private String mUrl;
    private String mPubDate;
    private String mContributor;

    public Story(String mTitle, String mUrl, String mPubDate, String mContributor) {
        this.mTitle = mTitle;
        this.mUrl = mUrl;
        this.mPubDate = mPubDate;
        this.mContributor = mContributor;
    }

    public String getmContributor() {
        return mContributor;
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
