package com.devonulrich.genesisclient.network;

import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.devonulrich.genesisclient.OverviewActivity;
import com.devonulrich.genesisclient.R;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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
        TableLayout tl = (TableLayout) activity.findViewById(R.id.schedule_table);

        boolean isOdd = true;
        int largestHeight = 0;
        for(ArrayList<String> classDetails : result) {
            TableRow tr = new TableRow(activity);
            Drawable background = activity.getResources().getDrawable(
                    isOdd ? R.drawable.schedule_table_odd : R.drawable.schedule_table_even,
                    activity.getTheme());
            isOdd = !isOdd;
            tr.setBackground(background);
            tr.setPadding(2, 2, 2, 2);

            TextView period = new TextView(activity);
            period.setText(classDetails.get(0));
            period.setTextSize(12);
            TableRow.LayoutParams periodParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            periodParams.setMarginEnd(10);
            tr.addView(period, periodParams);

            TextView name = new TextView(activity);
            name.setText(classDetails.get(1));
            name.setTextSize(12);
            TableRow.LayoutParams nameParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            nameParams.setMarginEnd(10);
            nameParams.weight = 1;
            tr.addView(name, nameParams);

            TextView room = new TextView(activity);
            room.setText(classDetails.get(4));
            room.setTextSize(12);
            TableRow.LayoutParams roomParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            roomParams.setMarginEnd(10);
            tr.addView(room, roomParams);

            TextView teacher = new TextView(activity);
            teacher.setText(classDetails.get(5));
            teacher.setTextSize(12);
            TableRow.LayoutParams teacherParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            teacherParams.setMarginEnd(10);
            teacherParams.weight = 1;
            tr.addView(teacher, teacherParams);

            if(tr.getHeight() > largestHeight) largestHeight = tr.getHeight();

            tl.addView(tr);
        }

        tl.setVisibility(View.VISIBLE);
    }
}
