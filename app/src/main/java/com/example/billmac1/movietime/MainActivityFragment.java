package com.example.billmac1.movietime;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

/**
 * A fragment containing the list view of Android versions.
 */


public class MainActivityFragment extends Fragment {

   private MovieThumbAdapter movieAdapter;



    MovieThumb[] movieThumbs = {};

    private MovieThumbAdapter movieThumbAdapter;

   /** Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);  **/

    public MainActivityFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setHasOptionsMenu(true);
        FetchMovieDataTask movieDataTask = new FetchMovieDataTask();
        movieDataTask.execute("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        movieAdapter = new  MovieThumbAdapter(getActivity(), Arrays.asList(movieThumbs));

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(movieThumbAdapter);

        return rootView;
    }


    public class FetchMovieDataTask extends AsyncTask<String, Void, String[]> {



        private String[] getWeatherDataFromJson(String movieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_LIST = "results";
            final String OWM_PATH = "poster_path";
            final String OWM_OVERVIEW = "overview";
            final String OWM_RELEASE = "release_date";
            final String OWM_TITLE = "original_title";
            final String OWM_VOTE = "vote_average";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);

            String[] resultStrs = new String[20];  //fix later


            for(int i = 0; i < movieArray.length(); i++) {

                String postPath;
                String overView;
                String releaseDate;
                String movieTitle;
                String avgRating;

                // Get the JSON object representing the movie
                JSONObject movieData = movieArray.getJSONObject(i);

                // posterPath is in a child array called
                JSONObject movieObject = movieData.getJSONArray(movieJsonStr).getJSONObject(0);
                postPath = movieObject.getString(OWM_PATH);
                overView = movieObject.getString(OWM_OVERVIEW);
                releaseDate = movieObject.getString(OWM_RELEASE);
                movieTitle = movieObject.getString(OWM_TITLE);
                avgRating = movieObject.getString(OWM_VOTE);

                resultStrs[i] = " - ";   //fix this

            }

            //   for (String s : resultStrs) {
            //       Log.v(LOG_TAG, "Forecast entry: " + s);
            //   }
            return resultStrs;

        }




        @Override
        protected String[] doInBackground(String... params) {

                  // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;


            try

            {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //     URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=10025,USA&mode=xml&units=metric&cnt=7&APPID=856b7efa2d5da7eb0a02075affe0e963");
                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&";
                final String API_KEY = "API_KEY";

                Uri builder = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builder.toString());

                //   Log.v(LOG_TAG,"Built URI" + builder.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                //.v(LOG_TAG,"Forecast JSON String: " + forecastJsonStr);
            } catch (IOException e) {
             //   Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                      //  Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try{
                return getWeatherDataFromJson(movieJsonStr);
            }
            catch (JSONException e) {
             //   Log.e(LOG_TAG,e.getMessage(), e);
             //   e.printStackTrace();
            }

            return null;

        }

    }


}