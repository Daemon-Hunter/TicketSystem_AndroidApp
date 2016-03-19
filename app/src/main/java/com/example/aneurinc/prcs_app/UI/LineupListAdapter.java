package com.example.aneurinc.prcs_app.UI;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 29/02/2016.
 */
public class LineupListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] name;
    private final Integer[] imageID;

    public LineupListAdapter(Activity context, String[] name, Integer[] imageID) {

        super(context, R.layout.list_lineup, name);

        this.context = context;
        this.name = name;
        this.imageID = imageID;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.list_lineup, null, true);
        TextView title = (TextView) rowView.findViewById(R.id.item);
        ImageView rowImage = (ImageView) rowView.findViewById(R.id.image);

        int colorPos = position % Constants.rowColour.length;
        int dimensions;

        if (name[position].contains("/")) {

            title.setTextColor(Color.WHITE);
            rowView.setBackgroundColor(Color.parseColor("#90caf9")); // Blue
            rowView.setOnClickListener(null);   // remove listener
            dimensions = 90;
            rowImage.setImageResource(R.drawable.calendar);


        } else {

            rowView.setBackgroundColor(Constants.rowColour[colorPos]);
            dimensions = 120;
            rowImage.setImageResource(imageID[position]);

        }

        rowImage.getLayoutParams().height = dimensions;
        rowImage.getLayoutParams().width = dimensions;
        title.setText(name[position]);

        return rowView;

    }

}
