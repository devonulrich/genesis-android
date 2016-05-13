package com.devonulrich.genesisclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.devonulrich.genesisclient.background.StartAlarm;
import com.devonulrich.genesisclient.data.LoginInfo;
import com.devonulrich.genesisclient.data.files.LoginStore;
import com.devonulrich.genesisclient.network.LoginTask;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //make sure the alarm for grade updating is on
        StartAlarm.startAlarm(this);

        //get the saved login credentials
        LoginInfo li = LoginStore.getLoginInfo(this);
        if (!li.autoLogin) {
            //if we shouldn't automatically log in, then go to LoginActivity
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        } else {
            //otherwise, attempt to log in
            LoginTask lt = new LoginTask(this);
            lt.execute(li);
        }
    }
}
