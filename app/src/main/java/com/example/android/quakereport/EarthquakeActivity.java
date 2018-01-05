/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.MenuRes;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Earthquake>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    ArrayList<Earthquake> dummy =new ArrayList<Earthquake>();
    ListAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ProgressBar progressBar;
    private boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
//        QuakeAsyncTask quakeAsyncTask=new QuakeAsyncTask();
//        quakeAsyncTask.execute(USGS_REQUEST_URL);
        ConnectivityManager connectivityManager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        isConnected=networkInfo!=null&&networkInfo.isConnected();
        mEmptyStateTextView=(TextView)findViewById(R.id.emptyView);
        progressBar=(ProgressBar)findViewById(R.id.circularProgressBar);
        if(isConnected)
        {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.noInternet);
        }
        Log.v(LOG_TAG,"initExe");



    }

    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int i, Bundle bundle)
    {
        Log.v(LOG_TAG,"onCreateLoader");
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude=sharedPreferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy= sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        Uri baseUri=Uri.parse(USGS_REQUEST_URL);
        Uri.Builder builder=baseUri.buildUpon();
        builder.appendQueryParameter("format","geojson");
        builder.appendQueryParameter("limit","10");
        builder.appendQueryParameter("minmag",minMagnitude);
        builder.appendQueryParameter("orderby",orderBy);

        return new EarthQuakeLoader(this,builder+"");
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> earthquakes) {
        progressBar.setVisibility(View.INVISIBLE);
        if(isConnected) {
            // Set empty state text to display "No earthquakes found."
            mEmptyStateTextView.setText(R.string.no_earthquakes);
        }
        else
        {
            mEmptyStateTextView.setText(R.string.noInternet);
        }
        // Clear the adapter of previous earthquake data
        if(mAdapter!=null)
        {
            mAdapter.clear();
        }
        if (earthquakes != null && !earthquakes.isEmpty()) {
            updateUi(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader) {
        Log.v(LOG_TAG,"onLoaderReset");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


//    private class QuakeAsyncTask extends AsyncTask<String,Void,ArrayList<Earthquake>> {
//        @Override
//        protected ArrayArrayList<Earthquake>  doInBackground(String... strings) {
//            if(strings.length<1||strings[0]==null)
//                return null;
//             ArrayArrayList<Earthquake> arrayArrayList=QueryUtils.fetchEarthquakeData(USGS_REQUEST_URL);
//            return arrayArrayList;
////            ArrayArrayList<Earthquake> arrayArrayList=new ArrayArrayList<>();
////            arrayArrayList.add(new Earthquake(3.6,2222222L,"arti","arti"));
////            return arrayArrayList;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayArrayList<Earthquake> earthquakes) {
//           if(earthquakes.size()<1||earthquakes.get(0)==null)
//               return;
//            updateUi(earthquakes);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_settings)
        {
            Intent settingIntent=new Intent(this,SettingsActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUi(final ArrayList<Earthquake> earthquakes)
    {
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        earthquakeListView.setEmptyView(mEmptyStateTextView);
        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new ListAdapter(this, android.R.layout.simple_list_item_1, earthquakes);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Earthquake currentEarthquake=earthquakes.get(i);
                Intent intent=new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY,currentEarthquake.getUrl());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
}