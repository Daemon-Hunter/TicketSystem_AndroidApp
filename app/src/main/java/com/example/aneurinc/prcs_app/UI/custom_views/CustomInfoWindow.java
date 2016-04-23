package com.example.aneurinc.prcs_app.UI.custom_views;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by aneurinc on 06/04/2016.
 */
public class CustomInfoWindow extends View implements GoogleMap.InfoWindowAdapter {

    private Activity context;
    private String strAddress;

    public CustomInfoWindow(Activity context, String address) {
        super(context);
        this.context = context;
        this.strAddress = address;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        View view = context.getLayoutInflater().inflate(R.layout.info_window, null);

        TextView address = (TextView) view.findViewById(R.id.venue_address);

        address.setText(strAddress);

        return view;
    }
}
