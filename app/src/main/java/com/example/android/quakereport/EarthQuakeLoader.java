package com.example.android.quakereport;

import java.util.List;

import android.support.v4.app.LoaderManager.LoaderCallbacks;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bandriya on 23-Sep-17.
 */
public class EarthQuakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>>
{
    /** Tag for log messages */
    private static final String LOG_TAG = EarthQuakeLoader.class.getName();

    String murl;
    public EarthQuakeLoader(Context context,String url) {
        super(context);
        murl=url;
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        Log.v(LOG_TAG,"loadInBackGround");
        if(murl==null)
            return null;
        ArrayList<Earthquake> arrayList=QueryUtils.fetchEarthquakeData(murl);
        return arrayList;
    }

    @Override
    protected void onStartLoading() {
        Log.v(LOG_TAG,"startLoading");
        forceLoad();
    }
}
