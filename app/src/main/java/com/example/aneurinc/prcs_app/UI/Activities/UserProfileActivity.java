package com.example.aneurinc.prcs_app.UI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.fragments.FragmentType;
import com.example.aneurinc.prcs_app.UI.fragments.UserDetailsFragment;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        createToolbar();

        switchFragment(new UserDetailsFragment(), FragmentType.USER_DETAILS);

    }

    public void switchFragment(Fragment fragment, FragmentType type) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        setCustomAnimation(transaction);
        transaction.replace(R.id.user_detail_fragment, fragment, type.toString());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentTag() == FragmentType.USER_DETAILS) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
            startActivity(intent);
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        } else {
            switchFragment(new UserDetailsFragment(), FragmentType.USER_DETAILS);
        }
    }

    private void setCustomAnimation(FragmentTransaction trans) {

        switch (getFragmentTag()) {

            case NAME:
                trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                break;

            case EMAIL:
                trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                break;

            case PASSWORD:
                trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                break;

            case ADDRESS:
                trans.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
                break;

            case USER_DETAILS:
                trans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
                break;

            default:
                break;
        }

    }

    private FragmentType getFragmentTag() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentType.NAME.toString());

        if (fragment != null && fragment.isVisible()) {
            return FragmentType.NAME;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(FragmentType.EMAIL.toString());

        if (fragment != null && fragment.isVisible()) {
            return FragmentType.EMAIL;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(FragmentType.PASSWORD.toString());

        if (fragment != null && fragment.isVisible()) {
            return FragmentType.PASSWORD;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(FragmentType.ADDRESS.toString());

        if (fragment != null && fragment.isVisible()) {
            return FragmentType.ADDRESS;
        }

        return FragmentType.USER_DETAILS;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.tb_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.FRAGMENT_ID, FragmentType.PARENT_EVENT.toString());
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.my_profile);
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
