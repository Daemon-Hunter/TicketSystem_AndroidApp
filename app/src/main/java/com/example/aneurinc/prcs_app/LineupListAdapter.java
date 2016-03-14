package com.example.aneurinc.prcs_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by aneurinc on 29/02/2016.
 */
public class LineupListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] name;
    private final Integer[] imageID;

    public LineupListAdapter(Activity context, String[] name, Integer[] imageID) {

        super(context, R.layout.lineup_list, name);

        this.context = context;
        this.name = name;
        this.imageID = imageID;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.lineup_list, null, true);
        TextView title = (TextView) rowView.findViewById(R.id.item);
        ImageView rowImage = (ImageView) rowView.findViewById(R.id.icon);
        ImageView ticketImage = (ImageView) rowView.findViewById(R.id.ticket_image);
        TextView desc = (TextView) rowView.findViewById(R.id.desc);
        LinearLayout ll = (LinearLayout) rowView.findViewById(R.id.layout_ticket);

        int colorPos = position % Constants.rowColour.length;
        int dimensions;

        if (name[position].contains("/")) {

            title.setTextColor(Color.WHITE);
            desc.setTextSize(5);
            desc.setText("");
            rowView.setBackgroundColor(Color.parseColor("#90caf9")); // Blue
            rowView.setOnClickListener(null);   // remove listener
            dimensions = 90;

        } else {

            desc.setText("Description of " + name[position]);
            rowView.setBackgroundColor(Constants.rowColour[colorPos]);
            ticketImage.setImageResource(R.drawable.ticket);
            dimensions = 120;
            addTicketListener(ll, position);

        }

        rowImage.getLayoutParams().height = dimensions;
        rowImage.getLayoutParams().width = dimensions;
        rowImage.setImageResource(imageID[position]);
        title.setText(name[position]);

        return rowView;

    }


    private void addTicketListener(LinearLayout layout, final int index) {

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.onclick));
                Log.d(MainActivity.DEBUG_TAG, "Clicked Ticket row: " + index);
                Intent i = new Intent(context, TicketActivity.class);
                i.putExtra(TicketActivity.EventImageIndex, index);
                context.startActivity(i);

            }
        });

    }
}
