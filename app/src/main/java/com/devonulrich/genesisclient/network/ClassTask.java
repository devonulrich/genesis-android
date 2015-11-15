package com.devonulrich.genesisclient.network;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import com.devonulrich.genesisclient.ClassActivity;
import com.devonulrich.genesisclient.ClassAdapter;
import com.devonulrich.genesisclient.R;
import com.devonulrich.genesisclient.data.ClassAssignment;

import java.util.ArrayList;

public class ClassTask extends AsyncTask<String, Void, ArrayList<ClassAssignment>> {

    private ClassActivity activity;

    public ClassTask(ClassActivity a) {
        activity = a;
    }

    @Override
    protected ArrayList<ClassAssignment> doInBackground(String... params) {
        //save the session and id for later
        String session = params[0];
        String id = params[1];
        String classID = params[2];
        return GenesisHTTP.classPage(session, id, classID);
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
