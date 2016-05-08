package com.google.jkellaway.androidapp_datamodel.utilities;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by aneurinc on 07/05/2016.
 */
public final class Utilities {

    public static String formatPrice(Double price) {

        DecimalFormat df = new DecimalFormat("£0.00");
        df.setRoundingMode(RoundingMode.FLOOR);
        String formattedPrice = df.format(price);

        return formattedPrice;
    }

}
