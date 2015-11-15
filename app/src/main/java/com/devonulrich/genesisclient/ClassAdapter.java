package com.devonulrich.genesisclient;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devonulrich.genesisclient.data.ClassAssignment;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private ArrayList<ClassAssignment> data;

    public ClassAdapter() {
        data = new ArrayList<>();
    }

    public void addData(ClassAssignment assignment) {
        //add the data
        data.add(assignment);
        //tell the adapter that an item was added - necessary for animations
        notifyItemInserted(getItemCount() - 1);
    }

    public int getItemCount() {
        return data.size();
    }

    public void onBindViewHolder(ClassViewHolder viewHolder, int i) {
        ClassAssignment assignment = data.get(i);

        //set all of the parts of the layout to the given data
        viewHolder.date.setText(assignment.date);
        viewHolder.category.setText(assignment.category);
        viewHolder.name.setText(assignment.name);
        viewHolder.points.setText(assignment.points);
        viewHolder.grade.setText(assignment.grade);
    }

    public ClassViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //inflate the view
        View classView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.class_item, viewGroup, false);

        return new ClassViewHolder(classView);
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        //stores references to all items in the class item layout

        TextView name;
        TextView grade;
        TextView category;
        TextView date;
        TextView points;

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
