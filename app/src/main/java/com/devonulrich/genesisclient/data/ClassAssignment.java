package com.devonulrich.genesisclient.data;

public class ClassAssignment {

    public String name;
    public String grade;
    public String category;
    public String date;
    public String points;

    public ClassAssignment() {
        name = " ";
        grade = " ";
        category = " ";
        date = " ";
        points = " ";
    }

    public ClassAssignment(String encoded) {
        String[] parts = encoded.split(" : ");
        name = parts[0];
        grade = parts[1];
        category = parts[2];
        date = parts[3];
        points = parts[4];
    }

    public String toString() {
        return name + " : " + grade + " : " + category + " : " + date + " : " + points;
    }
}
