package com.devonulrich.genesisclient;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devonulrich.genesisclient.data.SchoolClass;

import java.util.ArrayList;

public class OverviewAdapter extends RecyclerView.Adapter<OverviewAdapter.OverviewViewHolder> {

    private ArrayList<SchoolClass> data;

    public OverviewAdapter() {
        data = new ArrayList<>();
    }

    public void addData(SchoolClass newData) {
        //add the data
        data.add(newData);
        //tell the adapter that an item was added - necessary for animations
        notifyItemInserted(getItemCount() - 1);
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return data.size();
    }

    public void onBindViewHolder(OverviewViewHolder viewHolder, int i) {
        SchoolClass sc = data.get(i);

        //special case : if the class is "Introduction to Engineering", then shorten it, since
        //it would otherwise cause layout issues
        if (sc.name.equals("Introduction to Engineering"))
            sc.name = "Intro to Engineering";

        //set all of the parts of the layout to the given data
        viewHolder.period.setText(sc.period);
        viewHolder.schoolClass.setText(sc.name);
        viewHolder.grade.setText(sc.grade);
        viewHolder.teacher.setText(sc.teacher);
        viewHolder.room.setText(sc.room);
        viewHolder.id.setText(sc.id);
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

        TextView period;
        TextView schoolClass;
        TextView grade;
        TextView teacher;
        TextView room;
        TextView id;

        public OverviewViewHolder(View v) {
            super(v);

            period = (TextView) v.findViewById(R.id.card_period);
            schoolClass = (TextView) v.findViewById(R.id.card_class);
            grade = (TextView) v.findViewById(R.id.card_grade);
            teacher = (TextView) v.findViewById(R.id.card_teacher);
            room = (TextView) v.findViewById(R.id.card_room);
            id = (TextView) v.findViewById(R.id.card_id);
        }
    }
}
