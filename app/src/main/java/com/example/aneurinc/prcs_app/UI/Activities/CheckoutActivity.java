package com.example.aneurinc.prcs_app.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;

public class CheckoutActivity extends AppCompatActivity implements OnClickListener {

    public static String ORDER_ID;

    /*
    * Initialise activity
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        creatToolbar();

        ImageView pay = (ImageView) findViewById(R.id.pay);
        pay.setOnClickListener(this);
    }

    /*
     * Set up the toolbar
     * Set title
     * and back navigation
     */
    private void creatToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.checkout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /*
    * Override back navigation
    * Add fade transition
    */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /*
    * Initialise menu
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    /*
    * Handle toolbar item clicks
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.tb_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Handle view onClicks
    */
    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {
            // Start receipt activity and pass in Order ID
            case R.id.pay:
                Intent intent = new Intent(this, ReceiptActivity.class);
                intent.putExtra(ReceiptActivity.ORDER_ID, getIntent().getExtras().getInt(ORDER_ID));
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
