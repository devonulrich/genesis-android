package com.devonulrich.genesisclient.network;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.devonulrich.genesisclient.OverviewActivity;
import com.devonulrich.genesisclient.OverviewAdapter;
import com.devonulrich.genesisclient.R;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GradebookTask extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {

    private OverviewActivity activity;
    private ArrayList<ArrayList<String>> data;

    public GradebookTask(OverviewActivity a, ArrayList<ArrayList<String>> data) {
        this.activity = a;
        this.data = data;
    }

    protected ArrayList<ArrayList<String>> doInBackground(String... params) {
        return addData(GenesisHTTP.gradebook(params[0], params[1]));
    }

    protected ArrayList<ArrayList<String>> addData(Connection.Response response) {
        try {
            Document html = response.parse();

            HashMap<String, String> classGrades = new HashMap<>();
            for (Element classRow : html.select("tr.listrowodd, tr.listroweven")) {
                String className = classRow.getElementsByTag("td").get(0).text().split(" - ")[1];
                String classGrade = classRow.getElementsByTag("td").get(2).text();
                classGrades.put(className, classGrade);
            }

            for (ArrayList<String> classData : data) {
                classData.add(classGrades.get(classData.get(1)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    protected void onPostExecute(ArrayList<ArrayList<String>> result) {
        RecyclerView recList = (RecyclerView) activity.findViewById(R.id.recycler_view);
        recList.setAdapter(new OverviewAdapter(result));
    }
}
