package com.devonulrich.genesisclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.devonulrich.genesisclient.network.OverviewTask;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class OverviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        //get the session ID and student ID
        Intent intent = getIntent();
        String session = intent.getStringExtra(getString(R.string.id_session_id));
        String id = intent.getStringExtra(getString(R.string.id_student_id));
        if(session != null) {
            //if the previous activity passed a session id, then try to get the overview info
            OverviewTask ot = new OverviewTask(this);
            ot.execute(session, id);
        } else {
            //if no session ID was given, then go to the launcher activity
            Intent i = new Intent(this, LauncherActivity.class);
            startActivity(i);
        }

        //configure the RecyclerView
        RecyclerView recList = (RecyclerView) findViewById(R.id.recycler_view);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(new OverviewAdapter());
        recList.setItemAnimator(new FadeInLeftAnimator());
    }

    @Override
    public void onBackPressed() {
        //instead of going back to the main activity, go to the home screen
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
