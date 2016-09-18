package com.devonulrich.genesisclient.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class StartAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //Log.d(StartAlarm.class.getSimpleName(), "boot action received");

            startAlarm(context);
        }
    }

    public static void startAlarm(Context c) {
        Log.d(StartAlarm.class.getSimpleName(), "starting alarm");
        //Log.d("Pref test", PreferenceManager.getDefaultSharedPreferences(c)
        //        .getString("notifications_sync_time", "3"));
        AlarmManager alarmMgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        Intent newIntent = new Intent(c, AlarmTriggered.class);
        PendingIntent alrmIntent = PendingIntent.getBroadcast(c, 0, newIntent, 0);

        long interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c)
                .getString("notifications_sync_time", "3600000"));//default is 1 hour
        Log.d("StartAlarm", "Interval: " + interval + " ms");

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                0, interval, alrmIntent);
    }
}
