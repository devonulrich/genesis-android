package com.devonulrich.genesisclient.data.cache;

import android.content.Context;

import com.devonulrich.genesisclient.data.ClassAssignment;
import com.devonulrich.genesisclient.data.SchoolClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class ClassCache {

    private static final String FILE_NAME = "class-";

    //in milliseconds, how old the file can be to be usable at maximum
    private static final long MAX_TIME = 60000;

    //saves the list of assignments into the cache
    public static void writeData(Context c, String classID, String mp, ArrayList<ClassAssignment> data) {
        //make the class ID filename friendly
        classID = classID.replace("/", ":");
        try {
            File f = new File(c.getCacheDir(), FILE_NAME + classID + mp);
            FileWriter fw = new FileWriter(f);
            fw.append(String.valueOf(System.currentTimeMillis()));
            fw.append("\n");
            for(ClassAssignment ca : data) {
                fw.append(ca.toString());
                fw.append("\n");
            }
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //returns true if there is a suitable cache, and false if there isn't
    public static boolean exists(Context c, String classID, String mp) {
        File f = new File(c.getCacheDir(), FILE_NAME + classID + mp);
        if(!f.exists()) return false;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            long time = Long.parseLong(br.readLine());

            //return true if the cached file is less than 60 seconds old
            // (if the current time is less than 60 seconds after the file was created)
            return System.currentTimeMillis() < time + MAX_TIME;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //reads and returns data from the cache (or null if there is no suitable cache)
    public static ArrayList<ClassAssignment> readData(Context c, String classID, String mp) {
        File f = new File(c.getCacheDir(), FILE_NAME + classID + mp);
        //if there is no suitable file, then return null
        if(!f.exists()) return null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            //skip the first line
            br.readLine();

            ArrayList<ClassAssignment> assignments = new ArrayList<>();
            String inputLine;
            while((inputLine = br.readLine()) != null) {
                assignments.add(new ClassAssignment(inputLine));
            }

            return assignments;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
