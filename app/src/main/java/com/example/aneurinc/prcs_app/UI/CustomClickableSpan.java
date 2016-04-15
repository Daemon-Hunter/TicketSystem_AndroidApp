package com.example.aneurinc.prcs_app.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.example.aneurinc.prcs_app.R;

public class CustomClickableSpan extends ClickableSpan {

    private Activity context;

    public CustomClickableSpan(Activity c) {

        this.context = c;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_go_to_sign_in:
                context.onBackPressed();
                context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;

            case R.id.tv_go_to_register:
                context.startActivity(new Intent(context, RegisterActivity.class));
                context.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;

            default:
                break;

        }

    }

    @Override
    public void updateDrawState(TextPaint ds) {

        ds.setColor(Color.WHITE);
        ds.setUnderlineText(true);

    }
}
