package com.devonulrich.genesisclient.network;

import android.os.AsyncTask;

import com.devonulrich.genesisclient.OverviewActivity;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class OverviewTask extends AsyncTask<String, Void, ArrayList<ArrayList<String>>>{

    private OverviewActivity activity;
    private String session;
    private String id;

    public OverviewTask(OverviewActivity a) {
        activity = a;
    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... params) {
        session = params[0];
        id = params[1];
        return parse(GenesisHTTP.overview(session, id));
    }

    private ArrayList<ArrayList<String>> parse(Connection.Response response) {
        try {
            //parse the document
            Document html = response.parse();
            //get the "notecard" items - the divs which show the student overviews
            Elements students = html.getElementsByClass("notecard");
            //get the first one (the one for the current student ID)
            Element studentInfo = students.get(0);

            ArrayList<ArrayList<String>> arr = new ArrayList<>();
            //cycle through all rows in the schedule table
            for(Element classRow : studentInfo.select("table.list").get(2)
                    .select("tr.listrowodd, tr.listroweven")) {
                ArrayList<String> arr2 = new ArrayList<>();
                //cycle through all table data for each school-class
                for(Element classInfo : classRow.getElementsByTag("td")) {
                    arr2.add(classInfo.text());
                }
                arr.add(arr2);
            }

            return arr;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(ArrayList<ArrayList<String>> result) {
        GradebookTask gt = new GradebookTask(activity, result);
        gt.execute(session, id);
    }
}
