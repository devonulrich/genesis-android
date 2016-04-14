package com.devonulrich.genesisclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.devonulrich.genesisclient.data.cache.OverviewCache;
import com.devonulrich.genesisclient.data.cache.SessionCache;
import com.devonulrich.genesisclient.network.MPTask;
import com.devonulrich.genesisclient.network.OverviewTask;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class OverviewActivity extends Activity {

    public static final String EXTRA_CLASS_PERIOD = "genesisclient.extra_class_per";
    public static final String EXTRA_CLASS_NAME = "genesisclient.extra_class_name";
    public static final String EXTRA_CLASS_TEACHER = "genesisclient.extra_class_teacher";
    public static final String EXTRA_CLASS_GRADE = "genesisclient.extra_class_grade";
    public static final String EXTRA_CLASS_ROOM = "genesisclient.extra_class_room";
    public static final String EXTRA_CLASS_ID = "genesisclient.extra_class_id";
    public static final String EXTRA_CLASS_MP = "genesisclient.extra_class_mp";

    String session;
    String id;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        //add the options menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overview_menu, menu);

        //set the title for the marking period
        MPTask mpt = new MPTask(this);
        mpt.execute(session, id);

        this.menu = menu;
        return true;
    }

    public void cycleMPOption() {
        MenuItem mi = menu.findItem(R.id.mp);
        String initialText = mi.getTitle().toString();

        switch(initialText) {
            case "MP1":
                setMPOption("MP2");
                break;
            case "MP2":
                setMPOption("MP3");
                break;
            case "MP3":
                setMPOption("MP4");
                break;
            case "MP4":
                setMPOption("MP1");
                break;
        }

        //remove all the cards
        RecyclerView recList = (RecyclerView) findViewById(R.id.recycler_view);
        ((OverviewAdapter) recList.getAdapter()).clearData();
        //delete the cache, so the data must be redownloaded
        OverviewCache.deleteData(this);
        //download the data
        OverviewTask ot = new OverviewTask(this);
        ot.execute(session, id, mi.getTitle().toString());
    }

    public void setMPOption(String str) {
        menu.findItem(R.id.mp).setTitle(str);
    }

    @Override
    protected void onStart() {
        //called every time the activity is put in the foreground
        super.onStart();

        if (SessionCache.exists(this)) {
            //if a session exists in the cache, then use it
            String[] ids = SessionCache.readData(this);
            //we don't need to worry about this null warning, since we already know
            //that it won't be null
            session = ids[0];
            id = ids[1];
            OverviewTask ot = new OverviewTask(this);
            ot.execute(session, id);
        } else {
            //if no session was found in the cache, then go to the LauncherActivity to either
            //manually log in, or to ask the user for login information
            Intent i = new Intent(this, LauncherActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            //if the refresh option was selected
            case R.id.refresh:
                //remove the data shown in the UI
                RecyclerView recList = (RecyclerView) findViewById(R.id.recycler_view);
                ((OverviewAdapter) recList.getAdapter()).clearData();
                //remove the data saved in the cache
                OverviewCache.deleteData(this);
                //call onStart() to refresh the data
                onStart();
                return true;
            case R.id.mp:
                //cycle through the 4 marking periods
                cycleMPOption();
            //if any other option was selected (should never happen)
            default:
                return super.onOptionsItemSelected(item);
        }
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
        //when one of the class cards was clicked, meaning that the user wants to go to that
        //specific ClassActivity

        //get all data from the card which was clicked
        String period = ((TextView) view.findViewById(R.id.card_period)).getText().toString();
        String className = ((TextView) view.findViewById(R.id.card_class)).getText().toString();
        String teacher = ((TextView) view.findViewById(R.id.card_teacher)).getText().toString();
        String grade = ((TextView) view.findViewById(R.id.card_grade)).getText().toString();
        String room = ((TextView) view.findViewById(R.id.card_room)).getText().toString();
        String classID = ((TextView) view.findViewById(R.id.card_id)).getText().toString();
        classID = classID.replace('/', ':');//the slash in the class ID is replaced with a colon in
        String mp = menu.findItem(R.id.mp).getTitle().toString();
        // the URL, so we must fix it for the URL to be correct

        //create a new intent, and put all of the necessary class info in
        Intent i = new Intent(this, ClassActivity.class);
        i.putExtra(EXTRA_CLASS_PERIOD, period);
        i.putExtra(EXTRA_CLASS_NAME, className);
        i.putExtra(EXTRA_CLASS_TEACHER, teacher);
        i.putExtra(EXTRA_CLASS_GRADE, grade);
        i.putExtra(EXTRA_CLASS_ROOM, room);
        i.putExtra(EXTRA_CLASS_ID, classID);
        i.putExtra(EXTRA_CLASS_MP, mp);
        startActivity(i);
    }
}
