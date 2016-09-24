package com.beesham.technews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Story>> {

    private static final String LOG_TAG = FeedActivity.class.getSimpleName();
    private static final String GUARDIAN_REQUEST_URL = "http://content.guardianapis.com/search";
    private static int STORY_LOADER_ID = 1;

    private ListView mStoryListView;
    private StoryAdapter mStoryAdapter;
    private ProgressBar mProgressBar;
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_view);

        mStoryListView = (ListView) findViewById(R.id.list);
        mStoryListView.setEmptyView(findViewById(R.id.empty_view));

        mStoryAdapter = new StoryAdapter(this, new ArrayList<Story>());
        mStoryListView.setAdapter(mStoryAdapter);

        mStoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mStoryAdapter.getItem(i).getmUrl()));
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });

        if(checkForInternet()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(STORY_LOADER_ID, null, this);
        }else{
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setText(R.string.no_internet);
        }

    }

    @Override
    public Loader<List<Story>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String pageCount = sharedPreferences.getString(
                getString(R.string.settings_page_count_key),
                getString(R.string.settings_page_count_default));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q","technology");
        uriBuilder.appendQueryParameter("format","json");
        uriBuilder.appendQueryParameter("page-size", pageCount);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key","test");

        return new StoryLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Story>> loader, List<Story> data) {
        mEmptyView.setText(R.string.no_news);
        mProgressBar.setVisibility(View.GONE);

        mStoryAdapter.clear();
        if(data != null && !data.isEmpty()){
            mStoryAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Story>> loader) {
        mStoryAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Checks if device has internet connection
    */
    private boolean checkForInternet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
