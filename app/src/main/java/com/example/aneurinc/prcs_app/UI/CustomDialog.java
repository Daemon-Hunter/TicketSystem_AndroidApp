package com.example.aneurinc.prcs_app.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 06/04/2016.
 */
public class CustomDialog extends Dialog implements View.OnClickListener {

    private Activity context;

    public CustomDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setCancelable(false);

        setContentView(R.layout.custom_dialog);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        Button confirm = (Button) findViewById(R.id.confirm);
        Button cancel = (Button) findViewById(R.id.cancel);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:

                context.startActivity(new Intent(context, CheckoutActivity.class));

                break;

            case R.id.cancel:

                this.dismiss();

                break;

            default:
                break;
        }
    }
}
