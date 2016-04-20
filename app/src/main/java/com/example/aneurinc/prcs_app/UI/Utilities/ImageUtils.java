package com.example.aneurinc.prcs_app.UI.Utilities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;

/**
 * Created by denni on 20/04/2016.
 */
public final class ImageUtils {
    public static Bitmap scaleDown(Bitmap image, int width, int height){
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static int getScreenWidth(Activity context){
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getScreenHeight(Activity context){
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

}
