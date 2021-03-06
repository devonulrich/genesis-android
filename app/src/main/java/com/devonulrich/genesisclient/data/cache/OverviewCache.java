package com.devonulrich.genesisclient.data.cache;

import android.content.Context;

import com.devonulrich.genesisclient.data.SchoolClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class OverviewCache {

    private static final String FILE_NAME = "overview";

    //in milliseconds, how old the file can be to be usable at maximum
    private static final long MAX_TIME = 300000;

    //saves the list of class info into the cache
    public static void writeData(Context c, String mp, ArrayList<SchoolClass> data) {
        try {
            File f = new File(c.getCacheDir(), FILE_NAME + mp);
            FileWriter fw = new FileWriter(f);
            fw.append(String.valueOf(System.currentTimeMillis()));
            fw.append("\n");
            for(SchoolClass sc : data) {
                fw.append(sc.toString());
                fw.append("\n");
            }
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //returns true if there is a suitable cache, and false if there isn't
    public static boolean exists(Context c, String mp) {
        File f = new File(c.getCacheDir(), FILE_NAME + mp);
        if(!f.exists()) return false;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            long time = Long.parseLong(br.readLine());

            //return true if the cached file is less than 300 seconds old
            // (if the current time is less than 300 seconds after the file was created)
            return System.currentTimeMillis() < time + MAX_TIME;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //reads and returns data from the cache (or null if there is no suitable cache)
    public static ArrayList<SchoolClass> readData(Context c, String mp) {
        File f = new File(c.getCacheDir(), FILE_NAME + mp);
        //if there is no suitable file, then return null
        if(!f.exists()) return null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            //skip the first line
            br.readLine();

            ArrayList<SchoolClass> classes = new ArrayList<>();
            String inputLine;
            while((inputLine = br.readLine()) != null) {
                classes.add(new SchoolClass(inputLine));
            }

            return classes;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteData(Context c, String mp) {
        File f = new File(c.getCacheDir(), FILE_NAME + mp);
        return f.delete();
    }
}
