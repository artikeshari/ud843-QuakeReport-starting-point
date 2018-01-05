package com.example.android.quakereport;

/**
 * Created by bandriya on 16-Sep-17.
 */
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = "MainActivity";


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    public QueryUtils() {
    }

    /**
     * Query the USGS dataset and return an object to represent a single earthquake.
     */

    public static ArrayList<Earthquake> fetchEarthquakeData(String requestUrl){
        Log.v(LOG_TAG,"fetchEarthquakeData");
        URL url=createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse=null;
        try{
            Log.v("jsonrequest",requestUrl);
            jsonResponse=makeHttpRequest(url);
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG,"Error closing input stream",e);
        }
        ArrayList<Earthquake> earthquake = extractEarthquakesFromJson(jsonResponse);
        return earthquake;
    }
    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse="";
        if(url==null)
        {
            return jsonResponse;
        }
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        }
            catch(IOException e)
            {
                Log.e(LOG_TAG,"Exception in http request",e);
            }
            finally
            {
                if (urlConnection != null) {

                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }

        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output=new StringBuilder();
        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while (line!=null){
                output.append(line);
                line=reader.readLine();
            }

        }
        return output.toString();
    }
    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createUrl(String stringUrl){
        URL url=null;
        try
        {
            url=new URL(stringUrl);
        }
        catch (MalformedURLException exception)
        {
            Log.e(LOG_TAG, "Error with creating URL", exception);
        }
        return url;
    }
    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */

    public static ArrayList<Earthquake>  extractEarthquakesFromJson(String json_response) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonObject=new JSONObject(json_response);

            JSONArray features=jsonObject.getJSONArray("features");
            for(int i=0;i<features.length();i++) {
                JSONObject arrayObject=features.getJSONObject(i);
                JSONObject properties=arrayObject.getJSONObject("properties");
                 Double mag=properties.getDouble("mag");
                 String place=properties.getString("place");
                Long time=properties.getLong("time");
                String url=properties.getString("url");
                earthquakes.add(new Earthquake(mag,time,place,url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of earthquakes
        return earthquakes;
    }

}