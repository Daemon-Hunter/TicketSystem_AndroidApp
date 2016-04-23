package com.example.aneurinc.prcs_app.UI.CustomAdapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.MapActivity;
import com.example.aneurinc.prcs_app.UI.Utilities.Constants;
import com.example.aneurinc.prcs_app.UI.Utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;

import java.util.List;

/**
 * Created by aneurinc on 21/03/2016.
 */
public class VenueFragAdapter extends ArrayAdapter<String> implements OnClickListener {

    private final Activity mContext;
    private List<IVenue> mVenues;

    public VenueFragAdapter(Activity c, List<IVenue> venues) {
        super(c, R.layout.list_row_venue);
        mVenues = venues;
        mContext = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IVenue currVenue = mVenues.get(position);

        if (convertView == null) {

            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_venue, null);

            viewHolder = new ViewHolder();
            viewHolder.venueImage = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.venueName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.venueCity = (TextView) convertView.findViewById(R.id.city);
            viewHolder.venueMap = (ImageView) convertView.findViewById(R.id.map);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        if (currVenue.getImage(0) != null) {
            // get width of single grid
            int xy = mContext.findViewById(R.id.venue_list).getWidth() / 4;
            // resize image to fit single grid
            viewHolder.venueImage.setImageBitmap(ImageUtils.scaleDown(currVenue.getImage(0), xy, xy));
        }

        viewHolder.venueName.setText(currVenue.getName());
        // TODO change address to city!
        viewHolder.venueCity.setText(currVenue.getAddress());
        viewHolder.venueMap.setImageResource(R.drawable.google_maps_icon);
        viewHolder.venueMap.setOnClickListener(this);

        int colorPos = position % Constants.rowColour.length;
        convertView.setBackgroundColor(Constants.rowColour[colorPos]);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.onclick));
        Intent i =  new Intent(mContext, MapActivity.class);
//        i.putExtra()
        mContext.startActivity(new Intent(mContext, MapActivity.class));
    }

    @Override
    public int getCount() {
        return mVenues.size();
    }

    static class ViewHolder {
        ImageView venueImage;
        ImageView venueMap;
        TextView venueName;
        TextView venueCity;
    }
}
