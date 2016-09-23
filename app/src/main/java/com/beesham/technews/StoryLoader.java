package com.beesham.technews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beesham on 9/22/2016.
 */
public class StoryLoader extends AsyncTaskLoader<List<Story>> {

    private String mUrl;

    public StoryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Story> loadInBackground() {
        if(mUrl == null) return null;
        return new ArrayList<Story>(QueryUtils.fetchNewsData(mUrl));
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }

}
