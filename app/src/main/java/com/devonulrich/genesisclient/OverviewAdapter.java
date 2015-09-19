package com.devonulrich.genesisclient;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.OverviewViewHolder> {

    private ArrayList<ArrayList<String>> data;

    public OverviewAdapter() {
        data = new ArrayList<>();
    }

    public void addData(ArrayList<String> newData) {
        //add the data
        data.add(newData);
        //tell the adapter that an item was added - necessary for animations
        notifyItemInserted(getItemCount() - 1);
    }

    public int getItemCount() {
        return data.size();
    }

    public void onBindViewHolder(OverviewViewHolder viewHolder, int i) {
        ArrayList<String> classInfo = data.get(i);

        //special case : if the class is "Introduction to Engineering", then shorten it, since
        //it would otherwise cause layout issues
        if (classInfo.get(1).equals("Introduction to Engineering"))
            classInfo.set(1, "Intro to Engineering");

        //set all of the parts of the layout to the given data
        viewHolder.period.setText(classInfo.get(0));
        viewHolder.schoolClass.setText(classInfo.get(1));
        viewHolder.grade.setText(classInfo.get(6));
        viewHolder.teacher.setText(classInfo.get(5));
        viewHolder.room.setText(classInfo.get(4));
    }

    public OverviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //inflate the view
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.overview_item, viewGroup, false);

        //get the references for the view
        return new OverviewViewHolder(itemView);
    }

    public static class OverviewViewHolder extends RecyclerView.ViewHolder {
        //stores references to all items in the item layout

        protected TextView period;
        protected TextView schoolClass;
        protected TextView grade;
        protected TextView teacher;
        protected TextView room;

        public OverviewViewHolder(View v) {
            super(v);

            period = (TextView) v.findViewById(R.id.card_period);
            schoolClass = (TextView) v.findViewById(R.id.card_class);
            grade = (TextView) v.findViewById(R.id.card_grade);
            teacher = (TextView) v.findViewById(R.id.card_teacher);
            room = (TextView) v.findViewById(R.id.card_room);
        }
    }
}
