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
        ActionBar ab = getActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);

        Bundle extras = this.getIntent().getExtras();
        String period = extras.getString(getString(R.string.id_class_period));
        String className = extras.getString(getString(R.string.id_class_name));
        String teacher = extras.getString(getString(R.string.id_class_teacher));
        String grade = extras.getString(getString(R.string.id_class_grade));
        String room = extras.getString(getString(R.string.id_class_room));
        String classID = extras.getString(getString(R.string.id_class_id));

        this.setTitle(className);

        ((TextView) this.findViewById(R.id.class_period)).setText(getString(
                R.string.class_period, period));
        ((TextView) this.findViewById(R.id.class_grade)).setText(grade);
        ((TextView) this.findViewById(R.id.class_teacher)).setText(teacher);
        ((TextView) this.findViewById(R.id.class_room)).setText(room);

        if (SessionCache.exists(this)) {
            String[] ids = SessionCache.readData(this);
            String session = ids[0];
            String id = ids[1];
            ClassTask ct = new ClassTask(this);
            ct.execute(session, id, classID);
        } else {
            //if no session ID was given, then go to the launcher activity
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
            finish();
        }
        return true;
    }

}
