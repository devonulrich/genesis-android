package com.devonulrich.genesisclient.network;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.devonulrich.genesisclient.LoginActivity;
import com.devonulrich.genesisclient.LauncherActivity;
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
        studentId = info[0].id;
        return GenesisHTTP.login(info[0]);
    }

    protected void onPostExecute(Connection.Response result) {
        if(activity instanceof LoginActivity) {
            ((LoginActivity) activity).setLoggedIn(result != null);

            if(result != null) {
                //successfully logged in from LoginActivity
                Intent i = new Intent(activity, OverviewActivity.class);
                i.putExtra(activity.getString(R.string.id_session_id), result.cookie("JSESSIONID"));
                i.putExtra(activity.getString(R.string.id_student_id), studentId);
                activity.startActivity(i);
            } else {
                //did not log in from LoginActivity
                ((LoginActivity) activity).setErrorVisibility(true);
            }
        } else if(activity instanceof LauncherActivity) {
            if(result != null) {
                //successfully logged in from LauncherActivity
                Intent i = new Intent(activity, OverviewActivity.class);
                i.putExtra(activity.getString(R.string.id_session_id), result.cookie("JSESSIONID"));
                i.putExtra(activity.getString(R.string.id_student_id), studentId);
                activity.startActivity(i);
            } else {
                //did not log in from LauncherActivity
                Intent i = new Intent(activity, LoginActivity.class);
                i.putExtra(activity.getString(R.string.id_login_show_error), true);
                activity.startActivity(i);
            }
        }
    }
}
