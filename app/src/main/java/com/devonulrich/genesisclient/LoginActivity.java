package com.devonulrich.genesisclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.devonulrich.genesisclient.data.login.LoginInfo;
import com.devonulrich.genesisclient.data.login.LoginStore;
import com.devonulrich.genesisclient.network.LoginTask;

public class LoginActivity extends Activity {

    public static String EXTRA_LOGIN_ERROR = "genesisclient.extra_login_error";

    private boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //get the saved login info
        LoginInfo li = LoginStore.getLoginInfo(this);

        //put the saved email in the email EditText
        EditText emailET = (EditText) findViewById(R.id.email);
        emailET.setText(li.email);

        //put the saved password in the password EditText
        EditText passET = (EditText) findViewById(R.id.pass);
        passET.setText(li.pass);

        //put the saved student ID in the id EditText
        EditText idET = (EditText) findViewById(R.id.id);
        idET.setText(li.id);

        Intent intent = getIntent();
        if (intent.getBooleanExtra(EXTRA_LOGIN_ERROR, false)) {
            //if this activity is supposed to show an error, then show it
            setErrorVisibility(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //save all of the things the user put in
        //only automatically log in if their credentials worked
        boolean autoLogin = isLoggedIn;

        EditText emailET = (EditText) findViewById(R.id.email);
        String email = emailET.getText().toString();

        EditText passET = (EditText) findViewById(R.id.pass);
        String pass = passET.getText().toString();

        EditText idET = (EditText) findViewById(R.id.id);
        String id = idET.getText().toString();

        //organize and save the login info
        LoginInfo li = new LoginInfo(email, pass, id, autoLogin);
        LoginStore.setLoginInfo(li, this);
    }

    @Override
    public void onBackPressed() {
        //instead of going back to the main activity, go to the home screen
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void onLoginClick(View view) {
        //get the email, password, and student ID
        EditText emailET = (EditText) findViewById(R.id.email);
        String email = emailET.getText().toString();

        EditText passET = (EditText) findViewById(R.id.pass);
        String pass = passET.getText().toString();

        EditText idET = (EditText) findViewById(R.id.id);
        String id = idET.getText().toString();

        //log in with the user's info
        LoginTask lt = new LoginTask(this);
        LoginInfo li = new LoginInfo(email, pass, id, true);
        lt.execute(li);
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public void setErrorVisibility(boolean visibility) {
        if (visibility) {
            //show the error text
            final TextView errorTextView = (TextView) findViewById(R.id.error_label);
            errorTextView.setVisibility(View.VISIBLE);

            //remove the error text 5 seconds later
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setErrorVisibility(false);
                }
            }, 5000);
        } else {
            //remove the error text
            TextView errorTextView = (TextView) findViewById(R.id.error_label);
            errorTextView.setVisibility(View.GONE);
        }
    }
}
