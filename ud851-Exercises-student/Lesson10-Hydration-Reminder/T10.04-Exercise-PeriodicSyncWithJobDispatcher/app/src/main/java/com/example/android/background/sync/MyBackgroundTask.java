package com.example.android.background.sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by magic on 12/17/2017.
 * AsyncTask
 * //new MyBackgroundTask(this,job).execute();
 */

public class MyBackgroundTask extends AsyncTask {
    private JobService mJobService;
    private JobParameters mJob;

    MyBackgroundTask(JobService jobService, JobParameters job) {
        this.mJobService = jobService;
        mJob = job;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        ReminderTasks.executeTask(mJobService, ReminderTasks.ACTION_CHARGING_REMINDER);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        mJobService.jobFinished(mJob, false);
    }
}
