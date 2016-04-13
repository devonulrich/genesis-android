package com.devonulrich.genesisclient.network;

import android.os.AsyncTask;

import com.devonulrich.genesisclient.OverviewActivity;

public class MPTask extends AsyncTask<String, Void, String> {

    private OverviewActivity activity;

    public MPTask(OverviewActivity a) {
        activity = a;
    }

    protected String doInBackground(String... params) {
        String session = params[0];
        String id = params[1];

        return GenesisHTTP.markingPeriod(session, id);
    }

    protected void onPostExecute(String result) {
        activity.setMPOption(result);
    }
}
