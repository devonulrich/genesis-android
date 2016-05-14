package com.devonulrich.genesisclient.background;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.util.Log;

import com.devonulrich.genesisclient.OverviewActivity;
import com.devonulrich.genesisclient.R;
import com.devonulrich.genesisclient.data.cache.SessionCache;
import com.devonulrich.genesisclient.data.LoginInfo;
import com.devonulrich.genesisclient.data.files.LoginStore;
import com.devonulrich.genesisclient.data.files.NotificationStore;
import com.devonulrich.genesisclient.network.GenesisHTTP;

import java.util.HashMap;
import java.util.Map;

public class AlarmTriggered extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(AlarmTriggered.class.getSimpleName(), "alarm triggered");

        //we don't use the AsyncTasks we made since those are designed to work with the UI

        new Thread(new Runnable() {
            @Override
            public void run() {
                String sessID;
                String studID;
                if(SessionCache.exists(context)) {
                    //if a session ID is stored and is usable, then use it
                    String[] ids = SessionCache.readData(context);
                    sessID = ids[0];
                    studID = ids[1];
                } else {
                    //we must log in to get a new session ID
                    LoginInfo li = LoginStore.getLoginInfo(context);
                    if(li.autoLogin) {
                        //if we can log in automatically, then do it
                        sessID = GenesisHTTP.login(li);
                        studID = li.id;
                    } else {
                        //if we can't log in automatically, then give up
                        Log.d(AlarmTriggered.class.getSimpleName(), "failed to login to genesis");
                        return;
                    }
                }

                //get a list of classes with class ID's
                String mp = GenesisHTTP.markingPeriod(sessID, studID);
                HashMap<String, String> classes = GenesisHTTP.gradebook(sessID, studID, mp);

                for(Map.Entry<String, String> es : classes.entrySet()) {
                    //for every class, get the class ID and find how many assignments are
                    //listed for that class
                    String val = es.getValue();
                    String classID = val.split("---")[1];
                    classID = classID.replace('/', ':');
                    int count = GenesisHTTP.classPage(sessID, studID, classID, mp).size();
                    if(count > NotificationStore.getNotificationInfo(context, classID)) {
                        //there is a new assignment that has not been previously counted
                        //notify the user!
                        makeNotification(context, es.getKey());
                    }

                    //record the new amount, in case if it's new or there isn't an older one
                    NotificationStore.setNotificationInfo(context, classID, count);
                }
            }
        }).start();
    }

    public void makeNotification(Context context, String className) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle("New Assignment");
        builder.setContentText("There is a new assignment on genesis for " + className);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setVibrate(new long[]{0, 300, 100, 300, 100, 100});
        builder.setAutoCancel(true);

        Intent result = new Intent(context, OverviewActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(OverviewActivity.class);
        stackBuilder.addNextIntent(result);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager notifManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify(0, builder.build());
    }
}
