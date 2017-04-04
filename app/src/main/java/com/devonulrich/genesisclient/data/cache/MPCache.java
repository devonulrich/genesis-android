package com.devonulrich.genesisclient.data.cache;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MPCache {

    public static final String FILE_NAME = "markingperiod";

    //in milliseconds, how old the file can be to be usable at maximum
    //in this case, it's 1000 ms * 60 s * 60 m * 24 h, or 1 day
    private static final long MAX_TIME = 86400000;

    public static void writeData(Context c, String mp) {
        try {
            File f = new File(c.getCacheDir(), FILE_NAME);
            FileWriter fw = new FileWriter(f);
            fw.append(String.valueOf(System.currentTimeMillis()));
            fw.append("\n");
            fw.append(mp);
            fw.append("\n");
            fw.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean exists(Context c) {
        File f = new File(c.getCacheDir(), FILE_NAME);
        if(!f.exists()) return false;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            long time = Long.parseLong(br.readLine());

            //prevents a corrupt cache from being used
            String mp = br.readLine();
            if(mp.equals("null")) return false;

            //return true if the cached file is less than 1 day old
            // (if the current time is less than 1 day after the file was created)
            return System.currentTimeMillis() < time + MAX_TIME;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readData(Context c) {
        File f = new File(c.getCacheDir(), FILE_NAME);
        //if there is no suitable file, then return null
        if(!exists(c)) return null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            //skip the first line
            br.readLine();

            String mp = br.readLine();
            return mp;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
