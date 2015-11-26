package com.devonulrich.genesisclient.network;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.devonulrich.genesisclient.OverviewActivity;
import com.devonulrich.genesisclient.OverviewAdapter;
import com.devonulrich.genesisclient.R;
import com.devonulrich.genesisclient.data.SchoolClass;
import com.devonulrich.genesisclient.data.cache.OverviewCache;

import java.util.ArrayList;
import java.util.HashMap;

public class OverviewTask extends AsyncTask<String, Void, ArrayList<SchoolClass>> {

    private static final String LOG_TAG = OverviewTask.class.getSimpleName();

    private OverviewActivity activity;

    private boolean animate;

    public OverviewTask(OverviewActivity a, boolean animate) {
        activity = a;
        this.animate = animate;
    }

    @Override
    protected ArrayList<SchoolClass> doInBackground(String... params) {
        String session = params[0];
        String id = params[1];

        ArrayList<SchoolClass> classData;
        if(OverviewCache.exists(activity)) {
            //if the class data is cached, then load the saved data
            classData = OverviewCache.readData(activity);

            Log.i(LOG_TAG, "Loaded overview data from cache");
        } else {
            //if it's not cached, then download the info from the internet
            //get the overview page
            classData = GenesisHTTP.overview(session, id);
            HashMap<String, String> classGrades = GenesisHTTP.gradebook(session, id);
            addData(classData, classGrades);

            //save the downloaded info
            OverviewCache.writeData(activity, classData);

            Log.i(LOG_TAG, "Downloaded overview data from internet");
        }
        return classData;
    }

    private void addData(ArrayList<SchoolClass> table, HashMap<String, String> data) {
        for (SchoolClass sc : table) {
            String classData = data.get(sc.name);
            if (classData != null) {
                sc.grade = classData.split("---")[0];
                sc.id = classData.split("---")[1];
            }
        }
    }

    protected void onPostExecute(ArrayList<SchoolClass> result) {
        final RecyclerView recList = (RecyclerView) activity.findViewById(R.id.recycler_view);
        final OverviewAdapter adapter = (OverviewAdapter) recList.getAdapter();
        int delay = 0;
        for (int x = 0; x < result.size(); x++) {
            final SchoolClass sc = result.get(x);
            final int index = x;
            //cycle through all class data sets
            Handler handler = new Handler();
            //add the data set to the view, but with a delay to create a nice animation
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(adapter.dataSize() <= index) {
                        adapter.addData(sc);
                    } else {
                        adapter.modifyData(index, sc);
                    }
                }
            }, delay);
            //increase the delay to make each data set appear after the one before it
            if(animate) {
                delay += 80;
            }
        }
    }
}
