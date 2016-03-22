package com.example.aneurinc.prcs_app.UI;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 21/03/2016.
 */
public class VenueListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] name;
    private final String[] location;
    private final Integer[] image;

    public VenueListAdapter(Activity context, String[] name, Integer[] image, String[] location) {
        super(context, R.layout.list_venue, name);

        this.context = context;
        this.name = name;
        this.image = image;
        this.location = location;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_venue, null, true);

        ImageView rowImage = (ImageView) rowView.findViewById(R.id.image);
        TextView rowName = (TextView) rowView.findViewById(R.id.name);
        TextView rowLocation = (TextView) rowView.findViewById(R.id.location);
        ImageView rowMaps = (ImageView) rowView.findViewById(R.id.map);

        rowImage.setImageResource(image[position]);
        rowName.setText(name[position]);
        rowLocation.setText(location[position]);

        setOnClickListener(rowMaps, position);
        setOnClickListener(rowImage, position);

        int colorPos = position % Constants.rowColour.length;
        rowView.setBackgroundColor(Constants.rowColour[colorPos]);


        return rowView;
    }

    private void setOnClickListener(View v, final int index) {


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.onclick));

                switch (v.getId()) {

                    case R.id.image:
                        Intent intent = new Intent(context, VenueActivity.class);
                        intent.putExtra(VenueActivity.VenueImageIndex, index);
                        context.startActivity(intent);
                        break;

                    case R.id.map:
                        context.startActivity(new Intent(context, MapActivity.class));
                        break;

                }
            }
        });
    }

}
