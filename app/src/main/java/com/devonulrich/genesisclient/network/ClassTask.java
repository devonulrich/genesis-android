package com.devonulrich.genesisclient.network;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.devonulrich.genesisclient.ClassActivity;
import com.devonulrich.genesisclient.ClassAdapter;
import com.devonulrich.genesisclient.R;
import com.devonulrich.genesisclient.data.ClassAssignment;
import com.devonulrich.genesisclient.data.cache.ClassCache;
import com.devonulrich.genesisclient.data.cache.OverviewCache;

import java.util.ArrayList;

public class ClassTask extends AsyncTask<String, Void, ArrayList<ClassAssignment>> {

    private static final String LOG_TAG = ClassTask.class.getSimpleName();

    private ClassActivity activity;

    public ClassTask(ClassActivity a) {
        activity = a;
    }

    @Override
    protected ArrayList<ClassAssignment> doInBackground(String... params) {
        //save the session and ids for later
        String session = params[0];
        String id = params[1];
        String classID = params[2];
        String mp = params[3];

        if(ClassCache.exists(activity, classID, mp)) {
            //if there is a cache, then read and use it
            Log.i(LOG_TAG, "Loaded class (" + classID + ") info from cache");
            return ClassCache.readData(activity, classID, mp);
        } else {
            //if there is no cache, then download new data
            ArrayList<ClassAssignment> arr = GenesisHTTP.classPage(session, id, classID, mp);
            Log.i(LOG_TAG, "Downloaded class (" + classID + ") info from internet");
            //save the downloaded data for later
            ClassCache.writeData(activity, classID, mp, arr);
            return arr;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<ClassAssignment> result) {
        final RecyclerView recList = (RecyclerView) activity.findViewById(R.id.class_recyclerview);
        int delay = 0;
        for (final ClassAssignment assignment : result) {
            //cycle through all class data sets
            Handler handler = new Handler();
            //add the data set to the view, but with a delay to create a nice animation
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ClassAdapter) recList.getAdapter()).addData(assignment);
                }
            }, delay);
            //increase the delay to make each data set appear after the one before it
            delay += 80;
        }
    }
}
