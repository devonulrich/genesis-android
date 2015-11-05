package com.devonulrich.genesisclient.network;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import com.devonulrich.genesisclient.ClassActivity;
import com.devonulrich.genesisclient.ClassAdapter;
import com.devonulrich.genesisclient.R;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class ClassTask extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {

    private ClassActivity activity;
    private String session;
    private String id;

    public ClassTask(ClassActivity a) {
        activity = a;
    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... params) {
        //save the session and id for later
        session = params[0];
        id = params[1];
        String classID = params[2];
        return parse(GenesisHTTP.classPage(session, id, classID));
    }

    private ArrayList<ArrayList<String>> parse(Connection.Response response) {
        try {
            //parse the document
            Document html = response.parse();

            //create a 2D arraylist for storing all the rows of data
            ArrayList<ArrayList<String>> data = new ArrayList<>();

            //cycle through each row - a row is for one assignment
            for (Element assignmentRow : html.select("table.list").get(0)
                    .select("tr.listrowodd, tr.listroweven")) {
                //create an arraylist for storing this row of data
                ArrayList<String> dataRow = new ArrayList<>();

                //cycle through all the data for this one assignment
                for (Element assignmentInfo : assignmentRow.getElementsByTag("td")) {
                    //add the data to the arraylist
                    dataRow.add(assignmentInfo.text());
                }

                //add the arraylist to the bigger arraylist
                data.add(dataRow);
            }

            //returns a 2D arraylist - contains arraylists for each assignment, and they contain
            //info about the assignment (name, date, etc)
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists) {
        final RecyclerView recList = (RecyclerView) activity.findViewById(R.id.class_recyclerview);
        int delay = 0;
        for (final ArrayList<String> dataSet : arrayLists) {
            //cycle through all class data sets
            Handler handler = new Handler();
            //add the data set to the view, but with a delay to create a nice animation
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ClassAdapter) recList.getAdapter()).addData(dataSet);
                }
            }, delay);
            //increase the delay to make each data set appear after the one before it
            delay += 80;
        }
    }
}
