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

        LoginInfo li = LoginStore.getLoginInfo(this);
        if(!li.autoLogin) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else {
            LoginTask lt = new LoginTask(this);
            lt.execute(li);
        }
    }
}
