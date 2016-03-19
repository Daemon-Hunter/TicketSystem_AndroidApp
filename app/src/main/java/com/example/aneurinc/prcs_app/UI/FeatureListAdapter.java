package com.example.aneurinc.prcs_app.UI;

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

/**
 * Created by aneurinc on 19/03/2016.
 */
public class FeatureListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] name;
    private final String[] date;
    private final Integer[] imageID;

    public FeatureListAdapter(Activity context, String[] name, String[] date, Integer[] imageID) {

        super(context, R.layout.list_featured, name);

        this.context = context;
        this.name = name;
        this.date = date;
        this.imageID = imageID;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.list_featured, null, true);
        ImageView rowImage = (ImageView) rowView.findViewById(R.id.image);
        TextView rowName = (TextView) rowView.findViewById(R.id.name);
        TextView rowDate = (TextView) rowView.findViewById(R.id.date);

        int colorPos = position % Constants.rowColour.length;
        rowView.setBackgroundColor(Constants.rowColour[colorPos]);

        rowImage.setImageResource(imageID[position]);
        setOnClickListener(rowImage, position);
        rowName.setText(name[position]);
        rowDate.setText(date[position]);

        return rowView;

    }

    private void setOnClickListener(ImageView image, final int index) {

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.onclick));
                Intent i = new Intent(getContext(), EventActivity.class);
                i.putExtra(EventActivity.EventImageIndex, index);
                context.startActivity(i);
            }
        });
    }
}
