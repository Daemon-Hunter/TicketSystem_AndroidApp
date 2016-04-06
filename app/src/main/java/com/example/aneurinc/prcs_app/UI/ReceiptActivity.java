package com.example.aneurinc.prcs_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

public class ReceiptActivity extends AppCompatActivity implements View.OnClickListener {

    public static String EventImageIndex;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        setUpToolbar();
        initOnClickListener();

        InvoiceListAdapter adapter = new InvoiceListAdapter(this);
        list = (ListView) findViewById(R.id.invoice_list);
        list.setAdapter(adapter);

    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_home_white_24dp);
        setNavigationOnClickListener(toolbar);
        toolbarTitle.setText(R.string.your_receipt);
    }

    private void setNavigationOnClickListener(Toolbar t) {
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReceiptActivity.this, MainActivity.class));
            }
        });
    }

    private void initOnClickListener() {
        ImageView btnOk = (ImageView) findViewById(R.id.btn_confirm);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.onclick));

        switch (v.getId()) {

            case R.id.btn_confirm:
                onBackPressed();
                break;

            default:
                break;
        }
    }
}
