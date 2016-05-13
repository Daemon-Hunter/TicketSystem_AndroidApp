package com.example.aneurinc.prcs_app.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.aneurinc.prcs_app.R;

public class SplashActivity extends AppCompatActivity {

    /*
    * Initialise the activity
    * and start Login Activity
    * Splash logic handled in theme
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
}
