package com.devonulrich.genesisclient;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.devonulrich.genesisclient.data.cache.SessionCache;
import com.devonulrich.genesisclient.network.ClassTask;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class ClassActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        //make a back arrow visible
        ActionBar ab = getActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);

        //get the info that will be added to the UI
        Bundle extras = this.getIntent().getExtras();
        String period = extras.getString(OverviewActivity.EXTRA_CLASS_PERIOD);
        String className = extras.getString(OverviewActivity.EXTRA_CLASS_NAME);
        String teacher = extras.getString(OverviewActivity.EXTRA_CLASS_TEACHER);
        String grade = extras.getString(OverviewActivity.EXTRA_CLASS_GRADE);
        String room = extras.getString(OverviewActivity.EXTRA_CLASS_ROOM);
        String classID = extras.getString(OverviewActivity.EXTRA_CLASS_ID);

        //put the data into the UI
        this.setTitle(className);

        ((TextView) this.findViewById(R.id.class_period)).setText(getString(
                R.string.class_period, period));
        ((TextView) this.findViewById(R.id.class_grade)).setText(grade);
        ((TextView) this.findViewById(R.id.class_teacher)).setText(teacher);
        ((TextView) this.findViewById(R.id.class_room)).setText(room);

        if (SessionCache.exists(this)) {
            //if a session was found in the cache, then use it
            String[] ids = SessionCache.readData(this);
            String session = ids[0];
            String id = ids[1];
            ClassTask ct = new ClassTask(this);
            ct.execute(session, id, classID);
        } else {
            //if no session ID was found, then go to the launcher activity
            Intent i = new Intent(this, LauncherActivity.class);
            startActivity(i);
        }

        //configure the RecyclerView
        RecyclerView recList = (RecyclerView) findViewById(R.id.class_recyclerview);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(new ClassAdapter());
        recList.setItemAnimator(new FadeInLeftAnimator());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //if the item selected is the back arrow key, then finish the activity
            //and go back to the OverviewActivity
            finish();
        }
        return true;
    }

}
