package com.devonulrich.genesisclient;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private ArrayList<ArrayList<String>> data;

    public ClassAdapter() {
        data = new ArrayList<>();
    }

    public void addData(ArrayList<String> dataRow) {
        //add the data
        data.add(dataRow);
        //tell the adapter that an item was added - necessary for animations
        notifyItemInserted(getItemCount() - 1);
    }

    public int getItemCount() {
        return data.size();
    }

    public void onBindViewHolder(ClassViewHolder viewHolder, int i) {
        ArrayList<String> assignmentInfo = data.get(i);

        //set all of the parts of the layout to the given data
        viewHolder.date.setText(assignmentInfo.get(1));
        String[] category = assignmentInfo.get(5).split("\\s+");
        viewHolder.category.setText(category[category.length - 1]);
        viewHolder.name.setText(assignmentInfo.get(7));
        viewHolder.points.setText(assignmentInfo.get(14) + " / " + assignmentInfo.get(16));
        viewHolder.grade.setText(assignmentInfo.get(17));

        for (int x = 0; x < assignmentInfo.size(); x++) {
            Log.d("devonulrich", x + ": " + assignmentInfo.get(x));
        }
    }

    public ClassViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //inflate the view
        View classView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.class_item, viewGroup, false);

        return new ClassViewHolder(classView);
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        //stores references to all items in the class item layout

        protected TextView name;
        protected TextView grade;
        protected TextView category;
        protected TextView date;
        protected TextView points;

        public ClassViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.class_card_name);
            grade = (TextView) v.findViewById(R.id.class_card_grade);
            category = (TextView) v.findViewById(R.id.class_card_category);
            date = (TextView) v.findViewById(R.id.class_card_date);
            points = (TextView) v.findViewById(R.id.class_card_points);
        }
    }
}
