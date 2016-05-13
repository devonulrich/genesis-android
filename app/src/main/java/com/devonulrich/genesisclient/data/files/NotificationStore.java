package com.devonulrich.genesisclient.data.files;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationStore {

    private static final String NOTIFIC_PREFS_NAME = "com.devonulrich.genesisclient.notific-";

    private static final String ASSIGNMENT_COUNT_KEY = "ass-count";

    public static int getNotificationInfo(Context c, String classID) {
        //note; the classID string must be with a colon instead of a slash
        SharedPreferences sp = c.getSharedPreferences(
                NOTIFIC_PREFS_NAME + classID, Context.MODE_PRIVATE);
        return sp.getInt(ASSIGNMENT_COUNT_KEY, 2147483647);
    }

    public static void setNotificationInfo(Context c, String classID, int assignmentCount) {
        SharedPreferences sp = c.getSharedPreferences(
                NOTIFIC_PREFS_NAME + classID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(ASSIGNMENT_COUNT_KEY, assignmentCount);
        editor.apply();
    }
}
