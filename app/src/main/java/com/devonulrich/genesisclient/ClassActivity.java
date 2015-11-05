package com.devonulrich.genesisclient;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.devonulrich.genesisclient.network.ClassTask;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class ClassActivity extends Activity {

    private String period;
    private String className;
    private String teacher;
    private String grade;
    private String room;
    private String classID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        ActionBar ab = getActionBar();
        if(ab != null) ab.setDisplayHomeAsUpEnabled(true);

        Bundle extras = this.getIntent().getExtras();
        period = extras.getString(getString(R.string.id_class_period));
        className = extras.getString(getString(R.string.id_class_name));
        teacher = extras.getString(getString(R.string.id_class_teacher));
        grade = extras.getString(getString(R.string.id_class_grade));
        room = extras.getString(getString(R.string.id_class_room));
        classID = extras.getString(getString(R.string.id_class_id));

        this.setTitle(className);

        ((TextView)this.findViewById(R.id.class_period)).setText(getString(
                R.string.class_period, period));
        ((TextView)this.findViewById(R.id.class_grade)).setText(grade);
        ((TextView)this.findViewById(R.id.class_teacher)).setText(teacher);
        ((TextView)this.findViewById(R.id.class_room)).setText(room);


        String session = extras.getString(getString(R.string.id_session_id));
        String id = extras.getString(getString(R.string.id_student_id));

        if(session != null) {
            ClassTask ct = new ClassTask(this);
            ct.execute(session, id, classID);
            Log.d("deovnulrich", id + " " + classID);
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
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}
