/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
//       (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    //   (18) Create a String array containing the names of the desired data columns from our ContentProvider
//   (19) Create constant int values representing each column name's position above
//   (20) Create a constant int to identify our loader used in DetailActivity
    public static final String[] DETAIL_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_CONDITION_ID = 1;
    public static final int INDEX_WEATHER_MAX_TEMP = 2;
    public static final int INDEX_WEATHER_MIN_TEMP = 3;
    public static final int INDEX_WEATHER_HUMIDITY = 4;
    public static final int INDEX_WEATHER_PRESSURE = 5;
    public static final int INDEX_WEATHER_WIND_SPEED = 6;
    public static final int INDEX_WEATHER_DEGREES = 7;

    private static final int ID_FORECAST_LOADER = 22;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

    //   (15) Declare a private Uri field called mUri
    private Uri mUri;

    //   (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mWeatherDate;
    private TextView mWeatherDescription;
    private TextView mWeatherHighTemp;
    private TextView mWeatherLowTemp;
    private TextView mWeatherHumidity;
    private TextView mWeatherWind;
    private TextView mWeatherPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//       (13) Find each of the TextViews by ID
        mWeatherDate = (TextView) findViewById(R.id.weather_date);
        mWeatherDescription = (TextView) findViewById(R.id.weather_description);
        mWeatherHighTemp = (TextView) findViewById(R.id.weather_high_temp);
        mWeatherLowTemp = (TextView) findViewById(R.id.weather_low_temp);
        mWeatherHumidity = (TextView) findViewById(R.id.weather_humidity);
        mWeatherPressure = (TextView) findViewById(R.id.weather_pressure);
        mWeatherWind = (TextView) findViewById(R.id.weather_wind);
//       (14) Remove the code that checks for extra text
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            mUri = intentThatStartedThisActivity.getData();
            if (mUri == null) {
                throw new NullPointerException("Uri is unknown.");
            }
        }
//       (16) Use getData to get a reference to the URI passed with this Activity's Intent
//       (17) Throw a NullPointerException if that URI is null
//       (35) Initialize the loader for DetailActivity
        getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    //   (22) Override onCreateLoader
//         (23) If the loader requested is our detail loader, return the appropriate CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        /* URI for all rows of weather data in our weather table */
                /* Sort order: Ascending by date */
        switch (loaderId) {
            case ID_FORECAST_LOADER:
                return new CursorLoader(this,
                        mUri,
                        DETAIL_PROJECTION,
                        null,
                        null,
                        null
                );
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    //   (24) Override onLoadFinished
//       (25) Check before doing anything that the Cursor has valid data
//       (26) Display a readable data string
//       (27) Display the weather description (using SunshineWeatherUtils)
//       (28) Display the high temperature
//       (29) Display the low temperature
//       (30) Display the humidity
//       (31) Display the wind speed and direction
//       (32) Display the pressure
//       (33) Store a forecast summary in mForecastSummary
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursorData) {
        boolean cursorHasValidData = false;
        if (cursorData != null && cursorData.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }
        Long dateInMillis = cursorData.getLong(DetailActivity.INDEX_WEATHER_DATE);
        String dateString = SunshineDateUtils.getFriendlyDateString(this, dateInMillis, true);

        int weatherCondition = cursorData.getInt(DetailActivity.INDEX_WEATHER_CONDITION_ID);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherCondition);

        double miniTemp = cursorData.getDouble(DetailActivity.INDEX_WEATHER_MIN_TEMP);
        double maxTemp = cursorData.getDouble(DetailActivity.INDEX_WEATHER_MAX_TEMP);
        String formattedMiniTemp = SunshineWeatherUtils.formatTemperature(this, miniTemp);
        String formattedMaxTemp = SunshineWeatherUtils.formatTemperature(this, maxTemp);

        double humidity = cursorData.getDouble(DetailActivity.INDEX_WEATHER_HUMIDITY);
        double pressure = cursorData.getDouble(DetailActivity.INDEX_WEATHER_PRESSURE);
        String formattedPressure = getString(R.string.format_pressure, pressure);
        String formattedHumidity = getString(R.string.format_humidity, humidity);
//        String formattedHumidity = SunshineWeatherUtils.formatTemperature(this, humidity);
//        String formattedPressure = SunshineWeatherUtils.formatTemperature(this, pressure);

        float windSpeed = cursorData.getFloat(DetailActivity.INDEX_WEATHER_WIND_SPEED);
        float degrees = cursorData.getFloat(DetailActivity.INDEX_WEATHER_DEGREES);
        String formattedWind = SunshineWeatherUtils.getFormattedWind(this, windSpeed, degrees);

        mWeatherDate.setText(dateString);
        mWeatherDescription.setText(description);
        mWeatherLowTemp.setText(formattedMiniTemp);
        mWeatherHighTemp.setText(formattedMaxTemp);
        mWeatherWind.setText(formattedWind);
        mWeatherPressure.setText(formattedPressure);
        mWeatherHumidity.setText(formattedHumidity);
//        mForecastSummary = new StringBuilder().append(dateString).append(" , ").append(description)
//                .append(" ,Temp:").append(formattedMiniTemp).append(":").append(formattedMaxTemp)
//                .append(" ,Pressure:").append(formattedPressure)
//                .append(" ,Humidity:").append(formattedHumidity)
//                .append(" ,WindSpeed:").append(formattedWind)
//                .toString();
        mForecastSummary = String.format("%s - %s - %s/%s",
                dateString, description, formattedMaxTemp, formattedMiniTemp);
//        mForecastSummary = dateString + " , " + description + " ,Temp:" + formattedMiniTemp + ":" + formattedMaxTemp +
//                " ,Pressure:" + formattedPressure + " ,Humidity:" + formattedHumidity + " ,WindSpeed:" + formattedWind;

    }

    //   (34) Override onLoaderReset, but don't do anything in it yet
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}