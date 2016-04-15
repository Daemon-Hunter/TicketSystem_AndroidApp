package com.example.aneurinc.prcs_app.UI;

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

import com.example.aneurinc.prcs_app.Datamodel.IVenue;
import com.example.aneurinc.prcs_app.R;

import java.util.List;

/**
 * Created by aneurinc on 21/03/2016.
 */
public class VenueListAdapter extends ArrayAdapter<String> implements OnClickListener {

    private final Activity mContext;
    private String[] name;
    private String[] city;

    // TODO get image

    public VenueListAdapter(Activity context, List<IVenue> venues) {
        super(context, R.layout.list_venue, Constants.venueNames);

        mContext = context;

        updateVenueList(venues);
    }

    private void updateVenueList(List<IVenue> venues) {

        name = new String[venues.size()];
        city = new String[venues.size()];
        int i = 0;

        for (IVenue v : venues) {
            name[i] = v.getVenueName();
            city[i] = v.getVenueAddress();
            i++;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_venue, null);

            viewHolder = new ViewHolder();
            viewHolder.venueImage = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.venueName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.venueCity = (TextView) convertView.findViewById(R.id.city);
            viewHolder.venueMap = (ImageView) convertView.findViewById(R.id.map);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.venueImage.setImageResource(Constants.venueImages[position]);
        viewHolder.venueName.setText(name[position]);
        // TODO change address to city!
        viewHolder.venueCity.setText(city[position]);
        viewHolder.venueMap.setImageResource(R.drawable.google_maps_icon);
        viewHolder.venueMap.setOnClickListener(this);

        int colorPos = position % Constants.rowColour.length;
        convertView.setBackgroundColor(Constants.rowColour[colorPos]);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.onclick));
        mContext.startActivity(new Intent(mContext, MapActivity.class));
    }

    @Override
    public int getCount() {
        return name.length;
    }

    static class ViewHolder {
        ImageView venueImage;
        ImageView venueMap;
        TextView venueName;
        TextView venueCity;
    }
}
