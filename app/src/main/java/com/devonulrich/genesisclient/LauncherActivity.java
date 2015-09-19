package com.devonulrich.genesisclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.devonulrich.genesisclient.login.LoginInfo;
import com.devonulrich.genesisclient.login.LoginStore;
import com.devonulrich.genesisclient.network.LoginTask;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //get the saved login credentials
        LoginInfo li = LoginStore.getLoginInfo(this);
        if(!li.autoLogin) {
            //if the user chose not to log in automatically, then go to the login activity
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            //otherwise, attempt to log in
            LoginTask lt = new LoginTask(this);
            lt.execute(li);
        }
    }
}
