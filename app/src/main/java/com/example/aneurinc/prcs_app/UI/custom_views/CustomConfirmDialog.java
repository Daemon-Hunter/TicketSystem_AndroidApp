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


    public CustomConfirmDialog(Activity context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_confirm);

        setCancelable(false);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

    }
}