package com.example.aneurinc.prcs_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.aneurinc.prcs_app.R;

public class ReceiptActivity extends AppCompatActivity implements View.OnClickListener {

    public static String EventImageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initOnClickListener();
    }

    private void initOnClickListener() {
        Button btnOk = (Button) findViewById(R.id.button_ok);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_ok:
                int imageIndex = getIntent().getExtras().getInt(EventImageIndex);
                Intent i = new Intent(this, EventActivity.class);
                i.putExtra(EventActivity.EventImageIndex, imageIndex);
                startActivity(i);
                break;

            default:
                break;
        }
    }
}
