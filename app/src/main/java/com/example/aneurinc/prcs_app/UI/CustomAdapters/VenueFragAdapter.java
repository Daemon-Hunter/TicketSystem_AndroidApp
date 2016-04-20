package com.example.aneurinc.prcs_app.UI.CustomAdapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.jkellaway.androidapp_datamodel.datamodel.IVenue;

import java.util.List;

/**
 * Created by aneurinc on 21/03/2016.
 */
public class VenueFragAdapter extends ArrayAdapter<String> implements OnClickListener {

    private final Activity context;
    private String[] name;
    private String[] city;
    private Bitmap[] image;

    public VenueFragAdapter(Activity c, List<IVenue> venues) {
        super(c, R.layout.list_row_venue, Constants.venueNames);

        context = c;

        updateVenueList(venues);
    }

    private void updateVenueList(List<IVenue> venues) {

        name = new String[venues.size()];
        city = new String[venues.size()];
        image = new Bitmap[venues.size()];
        int i = 0;

        for (IVenue v : venues) {
            name[i] = v.getVenueName();
            city[i] = v.getVenueAddress();
            image[i] = v.getImage(0);
            i++;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = context.getLayoutInflater();
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

        if (image[position] != null) {
            // get width of single grid
            int xy = context.findViewById(R.id.venue_list).getWidth() / 4;
            // resize image to fit single grid
            viewHolder.venueImage.setImageBitmap(ImageUtils.scaleDown(image[position], xy, xy));
        }

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
        v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.onclick));
        context.startActivity(new Intent(context, MapActivity.class));
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
