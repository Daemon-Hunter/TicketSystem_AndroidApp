package com.example.aneurinc.prcs_app.UI;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 21/03/2016.
 */
public class VenueListAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener, View.OnClickListener {

    private final Activity context;

    public VenueListAdapter(Activity context) {
        super(context, R.layout.list_venue, Constants.venueNames);

        this.context = context;

        ListView list = (ListView) context.findViewById(R.id.venue_list);
        list.setOnItemClickListener(this);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_venue, null, true);

        ImageView rowImage = (ImageView) rowView.findViewById(R.id.image);
        TextView rowName = (TextView) rowView.findViewById(R.id.name);
        TextView rowLocation = (TextView) rowView.findViewById(R.id.location);
        ImageView rowMaps = (ImageView) rowView.findViewById(R.id.map);

        rowImage.setImageResource(Constants.venueImages[position]);
        rowName.setText(Constants.venueNames[position]);
        rowLocation.setText(Constants.locations[position]);

        rowMaps.setOnClickListener(this);

        int colorPos = position % Constants.rowColour.length;
        rowView.setBackgroundColor(Constants.rowColour[colorPos]);

        return rowView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, VenueActivity.class);
        intent.putExtra(VenueActivity.VenueImageIndex, position);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.onclick));
        context.startActivity(new Intent(context, MapActivity.class));
    }
}
