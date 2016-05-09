package com.example.aneurinc.prcs_app.UI.custom_adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.activities.MapActivity;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.events.IVenue;

import java.util.Collection;
import java.util.List;

/**
 * Created by aneurinc on 21/03/2016.
 */
public class VenueFragAdapter extends ArrayAdapter<IVenue> {

    private final Activity mContext;
    private List<IVenue> mVenues;
    private static final int ROW_COLOUR1 = 0x3003a9f4;
    private static final int ROW_COLOUR2 = 0x3081d4fa;

    public VenueFragAdapter(Activity context, List<IVenue> venues) {
        super(context, R.layout.list_row_venue);
        mVenues = venues;
        mContext = context;
    }

    private int getRowColour(int position) {
        return position % 2 == 0 ? ROW_COLOUR1 : ROW_COLOUR2;
    }

    @Override
    public void addAll(Collection<? extends IVenue> collection) {
        mVenues.addAll(collection);
    }

    @Override
    public void clear() {
        mVenues.clear();
    }

    @Override
    public IVenue getItem(int position) {
        return mVenues.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        final IVenue currVenue = getItem(position);

        if (convertView == null) {

            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_venue, null);

            viewHolder = new ViewHolder();
            viewHolder.venueImage = (ImageView) convertView.findViewById(R.id.artist_image);
            viewHolder.venueName = (TextView) convertView.findViewById(R.id.artist_child_event_name);
            viewHolder.venueCity = (TextView) convertView.findViewById(R.id.city);
            viewHolder.venueMap = (ImageView) convertView.findViewById(R.id.venue_map);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int xy = Utilities.getScreenWidth(mContext) / 4;

        if (xy > 0) {
            viewHolder.venueImage.setImageBitmap(Utilities.scaleDown(currVenue.getImage(0), xy, xy));
        }
        viewHolder.venueName.setText(currVenue.getName());
        viewHolder.venueCity.setText(currVenue.getCity());

        viewHolder.venueMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.onclick));
                Intent intent = new Intent(mContext, MapActivity.class);
                intent.putExtra(MapActivity.VENUE_ID, currVenue.getID());
                mContext.startActivity(intent);
            }
        });

        convertView.setBackgroundColor(getRowColour(position));

        return convertView;
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
