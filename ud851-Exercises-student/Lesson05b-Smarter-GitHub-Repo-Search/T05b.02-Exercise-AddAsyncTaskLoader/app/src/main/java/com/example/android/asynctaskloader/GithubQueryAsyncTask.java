package com.example.android.asynctaskloader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.android.asynctaskloader.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by magic on 11/10/2017.
 * GithubQueryAsyncTask
 */

public class GithubQueryAsyncTask extends AsyncTaskLoader<String> {
    private static final String TAG = GithubQueryAsyncTask.class.getSimpleName();
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private Bundle mArgs;

    public GithubQueryAsyncTask(Context context, Bundle args) {
        super(context);
        this.mArgs = args;
        Log.i(TAG, "Call EarthquakeLoader Constructor ");
    }

    @Override
    protected void onStartLoading() {
        if (mArgs != null) {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        String query = mArgs.getString(SEARCH_QUERY_URL_EXTRA);
        if (TextUtils.isEmpty(query)) {
            return null;
        }
        URL url = NetworkUtils.buildUrl(query);
        try {
            return NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
