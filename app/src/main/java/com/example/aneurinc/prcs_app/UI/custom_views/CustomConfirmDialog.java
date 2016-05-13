package com.example.aneurinc.prcs_app.UI.custom_views;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 06/04/2016.
 */
public class CustomConfirmDialog extends Dialog {


    /*
    * Initialise dialog with parent activity context
    */
    public CustomConfirmDialog(Activity context) {
        super(context);
    }

    /*
    * Initialise dialog
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_confirm);

        // Make modal
        setCancelable(false);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

    }
}