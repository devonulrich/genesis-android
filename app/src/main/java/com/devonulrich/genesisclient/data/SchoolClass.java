package com.devonulrich.genesisclient.data;

import android.util.Log;

public class SchoolClass {

    public String period;
    public String name;
    public String room;
    public String teacher;
    public String id;
    public String grade;

    public SchoolClass() {
        period = " ";
        name = " ";
        room = " ";
        teacher = " ";
        id = " ";
        grade = " ";
    }

    public SchoolClass(String encoded) {
        String[] parts = encoded.split(" : ");
        period = parts[0];
        name = parts[1];
        room = parts[2];
        teacher = parts[3];
        id = parts[4];
        grade = parts[5];
    }

    public String toString() {
        return period + " : " + name + " : " + room + " : " + teacher + " : " + id + " : " + grade;
    }
}
