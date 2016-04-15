package com.example.aneurinc.prcs_app.UI;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

/**
 * Created by aneurinc on 11/04/2016.
 */
public class MyTicketsListAdapter extends ArrayAdapter<String> {

    private final Activity context;

    public MyTicketsListAdapter(Activity context) {

        super(context, R.layout.list_my_tickets, Constants.eventName);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // stop index out of bounds error - DELETE WHEN DATA MODEL IS PLUGGED IN
        if (position > 6) {
            position -= position;
        }

        ViewHolder viewHolder;

        if (convertView == null) {

            // inflate view
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_my_tickets, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.eventName = (TextView) convertView.findViewById(R.id.event_name);
            viewHolder.eventVenue = (TextView) convertView.findViewById(R.id.event_venue);
            viewHolder.eventCost = (TextView) convertView.findViewById(R.id.event_cost);
            viewHolder.eventImage = (ImageView) convertView.findViewById(R.id.event_image);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {
            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String eventNameStr = Constants.eventName[position];
        if (eventNameStr.length() > 21) {
            eventNameStr = eventNameStr.substring(0, 18);
            eventNameStr += "...";
        }

        viewHolder.eventName.setText(eventNameStr);
        viewHolder.eventVenue.setText(Constants.venueNames[position]);
        viewHolder.eventCost.setText(Constants.ticketCost[position]);
        viewHolder.eventImage.setImageResource(Constants.eventImages[position]);

        return convertView;
    }

    static class ViewHolder {
        TextView eventName;
        TextView eventVenue;
        TextView eventCost;
        ImageView eventImage;
    }
}
