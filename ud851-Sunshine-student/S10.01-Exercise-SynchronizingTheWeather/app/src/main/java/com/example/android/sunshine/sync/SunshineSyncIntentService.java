package com.example.android.sunshine.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

//  (5) Create a new class called SunshineSyncIntentService that extends IntentService
//   (6) Create a constructor that calls super and passes the name of this class
//   (7) Override onHandleIntent, and within it, call SunshineSyncTask.syncWeather
public class SunshineSyncIntentService extends IntentService{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * name Used to name the worker thread, important only for debugging.
     */
    public static final String sName = "SunshineSyncIntentService";
    public SunshineSyncIntentService() {
        super(sName);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SunshineSyncTask.syncWeather(this);
    }
}