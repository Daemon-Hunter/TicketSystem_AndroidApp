package com.example.aneurinc.prcs_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.aneurinc.prcs_app.R;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener {

    public static String EventImageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        setUpToolbar();
        initOnClickListeners();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationOnClickListener(toolbar);
    }

    private void setNavigationOnClickListener(Toolbar t) {
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initOnClickListeners() {
        ImageView pay = (ImageView) findViewById(R.id.pay);
        pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {

            case R.id.pay:
                int imageIndex = getIntent().getExtras().getInt(EventImageIndex);
                Intent i = new Intent(this, ReceiptActivity.class);
                i.putExtra(ReceiptActivity.EventImageIndex, imageIndex);
                startActivity(i);
                break;

            default:
                break;
        }
    }
}
