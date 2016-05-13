package com.example.aneurinc.prcs_app.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;

public class AboutActivity extends AppCompatActivity {


    /*
    * Initialise activity
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        createToolbar();
    }

    /*
    * Initialise toolbar menu - load in menu2
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    /*
    * Handle toolbar menu clicks
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.tb_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Create toolbar - set title and add back navigation onClick listener
    */
    private void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.about_us);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}
