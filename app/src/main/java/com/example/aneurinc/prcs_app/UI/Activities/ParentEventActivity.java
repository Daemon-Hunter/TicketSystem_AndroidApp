package com.example.aneurinc.prcs_app.UI.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.ParentEventActAdapter;
import com.example.aneurinc.prcs_app.UI.utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.List;

public class ParentEventActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static String PARENT_EVENT_ID;
    private IParentEvent mParentEvent;
    private List<IChildEvent> mChildEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_event);

        setupToolbar();

        getParentEvent();

    }

    private void getParentEvent() {
        ReadParentEvent task = new ReadParentEvent(this);
        task.execute();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.parent_event);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void displayParentEvent() {

        ImageView image = (ImageView) findViewById(R.id.parent_event_image);
        TextView name = (TextView) findViewById(R.id.parent_event_title);
        TextView desc = (TextView) findViewById(R.id.parent_event_description);

        int xy = ImageUtils.getScreenWidth(this) / 4;
        Bitmap scaledImage = ImageUtils.scaleDown(mParentEvent.getImage(0), xy, xy);

        image.setImageBitmap(scaledImage);
        name.setText(mParentEvent.getName());
        desc.setText(mParentEvent.getDescription());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.tb_search:
                Log.d(MainActivity.DEBUG_TAG, "Action Bar: Search");
                break;

            case R.id.tb_home:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, ChildEventActivity.class);

        i.putExtra(ChildEventActivity.CHILD_EVENT_ID, mChildEvents.get(position).getID());
        i.putExtra(ChildEventActivity.PARENT_EVENT_ID, mParentEvent.getID());

        Log.d(MainActivity.DEBUG_TAG, "onItemClick: child before " + mChildEvents.get(position).getID());
        Log.d(MainActivity.DEBUG_TAG, "onItemClick: parent before = " + mParentEvent.getID());

        startActivity(i);
    }

    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {
            case R.id.facebook:
                break;
            case R.id.twitter:
                break;
            case R.id.instagram:
                break;
        }
    }

    private class ReadParentEvent extends AsyncTask<Void, Void, IParentEvent> {

        private Activity mContext;

        public ReadParentEvent(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected IParentEvent doInBackground(Void... params) {
            mParentEvent = UserWrapper.getInstance().getParentEvent(getIntent().getExtras().getInt(PARENT_EVENT_ID));

            try {
                mChildEvents = mParentEvent.getChildEvents();
            } catch (IOException e) {
            }

            return mParentEvent;
        }

        @Override
        protected void onPostExecute(IParentEvent parentEvent) {

            if (mChildEvents.isEmpty()) {
                TextView noChildEventsMessage = (TextView) mContext.findViewById(R.id.no_child_events_message);
                ImageView noChildEventsImage = (ImageView) mContext.findViewById(R.id.no_child_events_image);
                noChildEventsMessage.setVisibility(View.VISIBLE);
                noChildEventsImage.setVisibility(View.VISIBLE);

            } else {
                ListView list = (ListView) mContext.findViewById(R.id.child_events_list);
                list.setAdapter(new ParentEventActAdapter(mContext, mChildEvents));
                list.setOnItemClickListener(ParentEventActivity.this);
            }

            displayParentEvent();
        }
    }

}
