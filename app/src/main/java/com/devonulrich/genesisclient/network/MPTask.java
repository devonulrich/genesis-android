package com.devonulrich.genesisclient.network;

import android.os.AsyncTask;
import android.util.Log;

import com.devonulrich.genesisclient.OverviewActivity;
import com.devonulrich.genesisclient.data.cache.MPCache;

public class MPTask extends AsyncTask<String, Void, String> {

    private OverviewActivity activity;

    public MPTask(OverviewActivity a) {
        activity = a;
    }

    protected String doInBackground(String... params) {
        String session = params[0];
        String id = params[1];

        String mp;

        if(MPCache.exists(activity)) {
            mp = MPCache.readData(activity);
            Log.d(MPCache.class.getSimpleName(), "Getting MP from cache");
        } else {
            mp = GenesisHTTP.markingPeriod(session, id);
            MPCache.writeData(activity, mp);
            Log.d(MPCache.class.getSimpleName(), "Downloading MP from internet");
        }

        return mp;
    }

    protected void onPostExecute(String result) {
        activity.setMPOption(result);
    }
}
