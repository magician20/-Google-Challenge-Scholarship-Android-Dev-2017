package com.example.android.sunshine.sync;

import android.content.Context;
import android.content.Intent;

//  (9) Create a class called SunshineSyncUtils
//   (10) Create a public static void method called startImmediateSync
//   (11) Within that method, start the SunshineSyncIntentService
public class SunshineSyncUtils {
    public static void startImmediateSync(Context context) {
        Intent intentService = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intentService);
    }
}