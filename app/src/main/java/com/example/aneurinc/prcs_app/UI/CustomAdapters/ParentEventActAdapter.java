package com.example.aneurinc.prcs_app.UI.CustomAdapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.Datamodel.ChildEvent;
import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Utilities.Constants;
import com.example.aneurinc.prcs_app.Utility.Validator;

import java.util.LinkedList;

/**
 * Created by aneurinc on 16/04/2016.
 */
public class ParentEventActAdapter extends ArrayAdapter<String> {

    private final Activity mContext;
    private Bitmap[] image;
    private String[] name;
    private String[] venue;
    private String[] date;

    public ParentEventActAdapter(Activity context, LinkedList<ChildEvent> childEvents) {
        super(context, R.layout.list_row_child_event, Constants.eventName);
        mContext = context;
        updateChildEventsList(childEvents);
    }

    private void updateChildEventsList(LinkedList<ChildEvent> childEvents) {

        int i = 0;
        int n = childEvents.size();
        image = new Bitmap[n];
        name = new String[n];
        venue = new String[n];
        date = new String[n];

        for (ChildEvent c : childEvents) {
            image[i] = c.getImage();
            name[i] = c.getChildEventName();
            venue[i] = c.getVenue().toString();
            date[i] = c.getChildEventStartDateTime().toString();
            i++;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            // inflate view
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_child_event, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.childEventImage = (ImageView) convertView.findViewById(R.id.child_event_image);
            viewHolder.childEventName = (TextView) convertView.findViewById(R.id.child_event_name);
            viewHolder.childEventVenue = (TextView) convertView.findViewById(R.id.child_event_venue);
            viewHolder.childEventDate = (TextView) convertView.findViewById(R.id.child_event_date);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {

            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // alternate list view row colour
        int colorPos = position % Constants.rowColour.length;
        convertView.setBackgroundColor(Constants.rowColour[colorPos]);

        int width = Validator.getScreenWidth(mContext) / 5;
        int height = Validator.getScreenHeight(mContext) / 5;
        Bitmap scaledImage = Validator.scaleDown(image[position], width, height);

        viewHolder.childEventImage.setImageBitmap(scaledImage);
        viewHolder.childEventName.setText(name[position]);
        viewHolder.childEventVenue.setText(venue[position]);
        viewHolder.childEventDate.setText(date[position]);

        return convertView;

    }

    static class ViewHolder {
        ImageView childEventImage;
        TextView childEventName;
        TextView childEventVenue;
        TextView childEventDate;
    }
}
