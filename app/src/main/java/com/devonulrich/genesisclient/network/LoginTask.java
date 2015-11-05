package com.devonulrich.genesisclient.network;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.devonulrich.genesisclient.LauncherActivity;
import com.devonulrich.genesisclient.LoginActivity;
import com.devonulrich.genesisclient.OverviewActivity;
import com.devonulrich.genesisclient.R;
import com.devonulrich.genesisclient.login.LoginInfo;

import org.jsoup.Connection;

public class LoginTask extends AsyncTask<LoginInfo, Void, Connection.Response> {

    private Activity activity;
    private String studentId;

    public LoginTask(Activity a) {
        activity = a;
    }

    protected void onPreExecute() {
        //Toast.makeText(activity, "Logging in...", Toast.LENGTH_SHORT).show();
    }

    protected Connection.Response doInBackground(LoginInfo... info) {
        //save the student's ID for later use
        studentId = info[0].id;
        //log in
        return GenesisHTTP.login(info[0]);
    }

    protected void onPostExecute(Connection.Response result) {
        if(activity instanceof LoginActivity) {
            //if we logged in from LoginActivity
            ((LoginActivity) activity).setLoggedIn(result != null);

            if(result != null) {
                //successfully logged in from LoginActivity
                //go to the overview activity
                Intent i = new Intent(activity, OverviewActivity.class);
                i.putExtra(activity.getString(R.string.id_session_id), result.cookie("JSESSIONID"));
                i.putExtra(activity.getString(R.string.id_student_id), studentId);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(i);
            } else {
                //did not log in from LoginActivity
                //make the error text visible
                ((LoginActivity) activity).setErrorVisibility(true);
            }
        } else if(activity instanceof LauncherActivity) {
            //if we logged in from LauncherActivity

            if(result != null) {
                //successfully logged in from LauncherActivity
                //go to the overview activity
                Intent i = new Intent(activity, OverviewActivity.class);
                i.putExtra(activity.getString(R.string.id_session_id), result.cookie("JSESSIONID"));
                i.putExtra(activity.getString(R.string.id_student_id), studentId);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(i);
            } else {
                //did not log in from LauncherActivity
                //go to LoginActivity, and show the error text
                Intent i = new Intent(activity, LoginActivity.class);
                i.putExtra(activity.getString(R.string.id_login_show_error), true);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(i);
            }
        }
    }
}
