package com.devonulrich.genesisclient.network;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import com.devonulrich.genesisclient.OverviewActivity;
import com.devonulrich.genesisclient.OverviewAdapter;
import com.devonulrich.genesisclient.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OverviewTask extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {

    private OverviewActivity activity;
    private String session;
    private String id;

    public OverviewTask(OverviewActivity a) {
        activity = a;
    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... params) {
        //save the session ID and student ID for later use
        session = params[0];
        id = params[1];
        //get the overview page
        ArrayList<ArrayList<String>> classData = GenesisHTTP.overview(session, id);
        HashMap<String, String> classGrades = GenesisHTTP.gradebook(session, id);
        addData(classData, classGrades);

        return classData;
    }

    protected void addData(ArrayList<ArrayList<String>> table, HashMap<String, String> data) {
        for (ArrayList<String> row : table) {
            String className = row.get(1);
            String classGrade = "";
            String classID = "";

            String classData = data.get(className);
            if (classData != null) {
                classGrade = classData.split("---")[0];
                classID = classData.split("---")[1];
            }

            row.add(classGrade);
            row.add(classID);
        }
    }

    protected void onPostExecute(ArrayList<ArrayList<String>> result) {
        final RecyclerView recList = (RecyclerView) activity.findViewById(R.id.recycler_view);
        int delay = 0;
        for (final ArrayList<String> dataSet : result) {
            //cycle through all class data sets
            Handler handler = new Handler();
            //add the data set to the view, but with a delay to create a nice animation
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((OverviewAdapter) recList.getAdapter()).addData(dataSet);
                }
            }, delay);
            //increase the delay to make each data set appear after the one before it
            delay += 80;
        }
    }
}
