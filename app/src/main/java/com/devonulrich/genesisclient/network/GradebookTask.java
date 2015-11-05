package com.devonulrich.genesisclient.network;

import android.os.AsyncTask;
import android.os.Handler;
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
        //get the gradebook page
        Connection.Response response = GenesisHTTP.gradebook(params[0], params[1]);
        //add the gradebook data to the list of data
        return addData(response);
    }

    protected ArrayList<ArrayList<String>> addData(Connection.Response response) {
        try {
            Document html = response.parse();

            HashMap<String, String> classGrades = new HashMap<>();
            for (Element classRow : html.select("tr.listrowodd, tr.listroweven")) {
                //cycle through all table rows (which contain class information and grades)
                //get the class name by getting the list of table data, selecting the first one,
                //getting the text, and getting the part after the ' - '
                //the last part is necessary so the class name matches the class name from the
                //overview page, otherwise it would have the class ID number in front of it
                //ex: "1000/2 - English I Honors"
                String className = classRow.getElementsByTag("td").get(0).text().split(" - ")[1];
                //save the class id for later use
                String classID = classRow.getElementsByTag("td").get(0).text().split(" - ")[0];
                //get the class grade, which is the 3rd column in
                String classGrade = classRow.getElementsByTag("td").get(2).text();
                //add the class name and grade to the hashmap
                classGrades.put(className, classGrade + "---" + classID);
            }

            for (ArrayList<String> classData : data) {
                //cycle through every set of data for all classes

                //get the name of the current class
                String className = classData.get(1);
                String grade = "";
                String id = "";
                if (classGrades.get(className) != null) {
                    //get the grade specific to this class
                    grade = classGrades.get(className).split("---")[0];
                    //get the ID for this class
                    id = classGrades.get(className).split("---")[1];
                }
                //add the grade to the class's data set
                classData.add(grade);
                classData.add(id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> arrayLists) {
        final RecyclerView recList = (RecyclerView) activity.findViewById(R.id.recycler_view);
        int delay = 0;
        for (final ArrayList<String> dataSet : data) {
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
