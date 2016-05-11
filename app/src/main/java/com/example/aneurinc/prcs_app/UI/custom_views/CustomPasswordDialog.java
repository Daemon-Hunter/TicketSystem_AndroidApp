package com.example.aneurinc.prcs_app.UI.custom_views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 11/05/2016.
 */
public class CustomPasswordDialog extends Dialog {


    public CustomPasswordDialog(Activity context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_password);

    }
}
