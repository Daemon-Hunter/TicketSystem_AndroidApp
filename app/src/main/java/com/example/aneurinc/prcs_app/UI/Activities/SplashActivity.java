package com.example.aneurinc.prcs_app.UI.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aneurinc.prcs_app.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_TIME = 2000; // Splash screen transition rate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*
         * Fade in MainActivity at defined interval.
         */
        new Handler().postDelayed(new Runnable() {
            public void run() {

                SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();

                // Transition from SplashActivity to MainActivity
                overridePendingTransition(R.anim.splash_fade_in,
                        R.anim.splash_fade_out);

            }
        }, SPLASH_DISPLAY_TIME);
    }
}
