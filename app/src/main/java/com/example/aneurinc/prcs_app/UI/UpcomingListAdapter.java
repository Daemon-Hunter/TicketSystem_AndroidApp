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
 * Created by aneurinc on 19/03/2016.
 */
public class UpcomingListAdapter extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

    private final Activity context;

    public UpcomingListAdapter(Activity context) {

        super(context, R.layout.list_upcoming, Constants.eventName);

        this.context = context;

        ListView list = (ListView) context.findViewById(R.id.upcoming_list);
        list.setOnItemClickListener(this);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // inflate view
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_upcoming, null, true);

        // get reference to view objects
        ImageView rowImage = (ImageView) rowView.findViewById(R.id.image);
        TextView rowName = (TextView) rowView.findViewById(R.id.name);
        TextView rowDate = (TextView) rowView.findViewById(R.id.date);

        // alternate colour of rows
        int colorPos = position % Constants.rowColour.length;
        rowView.setBackgroundColor(Constants.rowColour[colorPos]);

        // set image
        rowImage.setImageResource(Constants.eventImages[position]);

        // set text fields
        rowName.setText(Constants.eventName[position]);
        rowDate.setText(Constants.dates[position]);

        // return complete row view
        return rowView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getContext(), EventActivity.class);
        i.putExtra(EventActivity.EventImageIndex, position);
        context.startActivity(i);
    }
}
