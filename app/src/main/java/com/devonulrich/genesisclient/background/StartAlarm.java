package com.devonulrich.genesisclient.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StartAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //Log.d(StartAlarm.class.getSimpleName(), "boot action received");

            //disabled while GenesisHTTP is being improved
            startAlarm(context);
        }
    }

    public static void startAlarm(Context c) {
        Log.d(StartAlarm.class.getSimpleName(), "starting alarm");
        AlarmManager alarmMgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);

        Intent newIntent = new Intent(c, AlarmTriggered.class);
        PendingIntent alrmIntent = PendingIntent.getBroadcast(c, 0, newIntent, 0);

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                0,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, alrmIntent);
    }
}
