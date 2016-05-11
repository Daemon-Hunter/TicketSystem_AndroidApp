/*
  * To change this license header, choose License Headers in Project Properties.
  * To change this template file, choose Tools | Templates
  * and open the template in the editor.
  */
package com.google.jkellaway.androidapp_datamodel.utilities;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author 10512691
 */
public class Formatter {
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
    private static final DecimalFormat decimalFormatter = new DecimalFormat("£0.00");

    public static String formatDate(Date date) {
        return dateFormatter.format(date);
    }

    public static String formatPrice(Double price) {

        decimalFormatter.setRoundingMode(RoundingMode.FLOOR);
        String formattedPrice = decimalFormatter.format(price);

        return formattedPrice;
    }
}