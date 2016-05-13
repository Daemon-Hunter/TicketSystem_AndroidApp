package com.example.aneurinc.prcs_app.UI.custom_views;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;

/**
 * Created by aneurinc on 06/04/2016.
 */
public class CustomInfoWindow extends View implements GoogleMap.InfoWindowAdapter {

    // Reference to parent activity
    private Activity mContext;

    // Venue object
    private IVenue mVenue;

    /*
    * Initialise information window
    */
    public CustomInfoWindow(Activity context, IVenue venue) {
        super(context);
        mContext = context;
        mVenue = venue;
    }

    /*
    * Return info window outline
    */
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /*
    * Return info window contents
    */
    @Override
    public View getInfoContents(Marker marker) {
        View view = mContext.getLayoutInflater().inflate(R.layout.info_window, null);

        TextView venue = (TextView) view.findViewById(R.id.info_window_title);
        TextView address = (TextView) view.findViewById(R.id.info_window_address);
        TextView city = (TextView) view.findViewById(R.id.info_window_city);
        TextView postcode = (TextView) view.findViewById(R.id.info_window_postcode);

        venue.setText(mVenue.getName());
        address.setText(mVenue.getAddress() + ", ");
        city.setText(mVenue.getCity() + ", ");
        postcode.setText(mVenue.getPostcode());

        return view;
    }
}
