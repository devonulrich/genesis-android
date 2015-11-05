package com.devonulrich.genesisclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.devonulrich.genesisclient.network.OverviewTask;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class OverviewActivity extends Activity {

    private String session;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        //get the session ID and student ID
        Intent intent = getIntent();
        session = intent.getStringExtra(getString(R.string.id_session_id));
        id = intent.getStringExtra(getString(R.string.id_student_id));
        if (session != null) {
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
        //instead of going back to the launcher activity, go to the home screen
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void onCardClick(View view) {
        //get all data from the card which was clicked
        String period = ((TextView) view.findViewById(R.id.card_period)).getText().toString();
        String className = ((TextView) view.findViewById(R.id.card_class)).getText().toString();
        String teacher = ((TextView) view.findViewById(R.id.card_teacher)).getText().toString();
        String grade = ((TextView) view.findViewById(R.id.card_grade)).getText().toString();
        String room = ((TextView) view.findViewById(R.id.card_room)).getText().toString();
        String classID = ((TextView) view.findViewById(R.id.card_id)).getText().toString();
        classID = classID.replace('/', ':');//the slash in the class ID is replaced with a colon in
        // the URL, so we must fix it for the URL to be correct

        //create a new intent, and put all of the necessary class info in
        Intent i = new Intent(this, ClassActivity.class);
        i.putExtra(getString(R.string.id_class_period), period);
        i.putExtra(getString(R.string.id_class_name), className);
        i.putExtra(getString(R.string.id_class_teacher), teacher);
        i.putExtra(getString(R.string.id_class_grade), grade);
        i.putExtra(getString(R.string.id_class_room), room);
        i.putExtra(getString(R.string.id_class_id), classID);

        //add the session ID and student ID, and start the activity for the specific class
        i.putExtra(getString(R.string.id_session_id), session);
        i.putExtra(getString(R.string.id_student_id), id);
        startActivity(i);
    }
}
