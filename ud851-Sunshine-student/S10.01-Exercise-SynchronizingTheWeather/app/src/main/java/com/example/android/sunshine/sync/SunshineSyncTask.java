package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;


//   (1) Create a class called SunshineSyncTask
class SunshineSyncTask {
    //   (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather

    synchronized public static void syncWeather(Context context) {
        //   (3) Within syncWeather, fetch new weather data
        URL url = NetworkUtils.getUrl(context);
        try {
            String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(url);
            ContentValues[] weatherContentValues = OpenWeatherJsonUtils.
                    getWeatherContentValuesFromJson(context, responseFromHttpUrl);

            //   (4) If we have valid results, delete the old data and insert the new
            if (weatherContentValues != null && weatherContentValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        null,
                        null
                );
                contentResolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        weatherContentValues
                );
                /* If the code reaches this point, we have successfully performed our sync */
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}