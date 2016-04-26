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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_adapters.ParentEventActAdapter;
import com.example.aneurinc.prcs_app.UI.utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;

public class ParentEventActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static String PARENT_EVENT_ID;
    private IParentEvent parentEvent;
    private Activity mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_event);

        setupToolbar();

        getParentEvent();

    }

    private void getParentEvent() {
        ReadParentEvent task = new ReadParentEvent();
        task.execute();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setText(R.string.parent_event);
    }

    private void displayParentEvent() {

        ImageView image = (ImageView) findViewById(R.id.parent_event_image);
        TextView name = (TextView) findViewById(R.id.parent_event_name);
        TextView desc = (TextView) findViewById(R.id.parent_event_description);
        int height = ImageUtils.getScreenHeight(this) / 4;
        int width = ImageUtils.getScreenHeight(this) / 4;

        Bitmap scaledImage = ImageUtils.scaleDown(parentEvent.getImage(0), width, height);
        image.setImageBitmap(scaledImage);
        name.setText(parentEvent.getName());
        desc.setText(parentEvent.getDescription());

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
        i.putExtra(ChildEventActivity.CHILD_EVENT_ID, parentEvent.getChildEvent(position).getID());
        startActivity(i);

    }

    private class ReadParentEvent extends AsyncTask<Void, Void, IParentEvent> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected IParentEvent doInBackground(Void... params) {
            parentEvent = UserWrapper.getInstance().getParentEvent(getIntent().getExtras().getInt(PARENT_EVENT_ID));
            return parentEvent;
        }

        @Override
        protected void onPostExecute(IParentEvent parentEvent) {

            try {
                if (!parentEvent.getChildEvents().isEmpty() && parentEvent.getChildEvents() != null) {
                    ListView list = (ListView) mContext.findViewById(R.id.child_events_list);
                    list.setAdapter(new ParentEventActAdapter(mContext, parentEvent.getChildEvents()));
                    list.setOnItemClickListener(ParentEventActivity.this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            displayParentEvent();
        }
    }

}
