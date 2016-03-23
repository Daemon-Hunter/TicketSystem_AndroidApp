package com.example.aneurinc.prcs_app.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 29/02/2016.
 */
public class LineupListAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

    private final Activity context;
    private final String[] name;
    private final Integer[] imageID;

    public LineupListAdapter(Activity context, String[] name, Integer[] imageID) {

        super(context, R.layout.list_artist_lineup, name);

        this.context = context;
        this.name = name;
        this.imageID = imageID;

        ListView list = (ListView) context.findViewById(R.id.lineup_list);
        list.setOnItemClickListener(this);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.list_artist_lineup, null, true);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getContext(), ArtistActivity.class);
        i.putExtra(ArtistActivity.EventImageIndex, position);
        context.startActivity(i);
    }
}
