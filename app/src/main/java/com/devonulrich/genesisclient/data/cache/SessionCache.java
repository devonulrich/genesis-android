package com.devonulrich.genesisclient.data.cache;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class SessionCache {

    private static final String FILE_NAME = "session";

    //in milliseconds, how old the file can be to be usable at maximum
    //in this case, it's 1000 ms * 60 s * 60 m, or 1 hour
    private static final long MAX_TIME = 3600000;

    public static void writeData(Context c, String sessionID, String studentID) {
        try {
            File f = new File(c.getCacheDir(), FILE_NAME);
            FileWriter fw = new FileWriter(f);
            fw.append(String.valueOf(System.currentTimeMillis()));
            fw.append("\n");
            fw.append(sessionID);
            fw.append("\n");
            fw.append(studentID);
            fw.append("\n");
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //returns true if there is a suitable cache, and false if there isn't
    public static boolean exists(Context c) {
        File f = new File(c.getCacheDir(), FILE_NAME);
        if(!f.exists()) return false;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            long time = Long.parseLong(br.readLine());

            //return true if the cached file is less than 60 minutes old
            // (if the current time is less than 60 seconds after the file was created)
            return System.currentTimeMillis() < time + MAX_TIME;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //reads and returns data from the cache (or null if there is no suitable cache)
    public static String[] readData(Context c) {
        File f = new File(c.getCacheDir(), FILE_NAME);
        //if there is no suitable file, then return null
        if(!exists(c)) return null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            //skip the first line
            br.readLine();

            String[] ids = new String[2];
            ids[0] = br.readLine();
            ids[1] = br.readLine();

            return ids;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
