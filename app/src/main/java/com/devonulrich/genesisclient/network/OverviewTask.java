package com.devonulrich.genesisclient.network;

import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.devonulrich.genesisclient.OverviewActivity;
import com.devonulrich.genesisclient.OverviewAdapter;
import com.devonulrich.genesisclient.R;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class OverviewTask extends AsyncTask<String, Void, ArrayList<ArrayList<String>>>{

    private OverviewActivity activity;

    public OverviewTask(OverviewActivity a) {
        activity = a;
    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... params) {
        return parse(GenesisHTTP.overview(params[0], params[1]));
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
        RecyclerView recList = (RecyclerView) activity.findViewById(R.id.recycler_view);
        recList.setAdapter(new OverviewAdapter(result));
    }
}
