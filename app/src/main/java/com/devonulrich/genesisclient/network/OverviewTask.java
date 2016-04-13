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

    public OverviewTask(OverviewActivity a) {
        activity = a;
    }

    @Override
    protected ArrayList<SchoolClass> doInBackground(String... params) {
        String session = params[0];
        String id = params[1];
        String mp = "";
        if(params.length > 2) mp = params[2];

        ArrayList<SchoolClass> classData;
        if(OverviewCache.exists(activity)) {
            //if the class data is cached, then load the saved data
            classData = OverviewCache.readData(activity);

            Log.i(LOG_TAG, "Loaded overview data from cache");
        } else {
            //if it's not cached, then download the info from the internet
            //get the overview data
            classData = GenesisHTTP.overview(session, id);
            //get the grades of each class
            HashMap<String, String> classGrades = GenesisHTTP.gradebook(session, id, mp);
            //combine the two sets of data
            addData(classData, classGrades);

            //save the downloaded info
            OverviewCache.writeData(activity, classData);

            Log.i(LOG_TAG, "Downloaded overview data from internet");
        }
        return classData;
    }

    private void addData(ArrayList<SchoolClass> table, HashMap<String, String> data) {
        for (SchoolClass sc : table) {
            //loop through every SchoolClass object
            String classData = data.get(sc.name);
            //get the extra data for that class
            if (classData != null) {
                //if it's a valid class name, then add the extra data into the SchoolClass object
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
            //loop through every SchoolClass object

            //get some variables which are used in the inner class, so they must be final
            final SchoolClass sc = result.get(x);
            final int index = x;

            Handler handler = new Handler();
            //add the data set to the view, but with a delay to create a nice animation
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(adapter.getItemCount() <= index) {
                        //if there is no data for this index - there's no card for this specific
                        //class yet, then add the data
                        adapter.addData(sc);
                    } else {
                        //if there's already data for this index - there's a card for this specific
                        //class, then modify the data instead of adding new data
                        adapter.modifyData(index, sc);
                    }
                }
            }, delay);
            //increase the delay to make each data set appear after the one before it
            delay += 80;
        }
    }
}
