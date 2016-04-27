package com.example.aneurinc.prcs_app.UI.custom_adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.utilities.Constants;
import com.example.aneurinc.prcs_app.UI.utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;

import java.io.IOException;
import java.util.List;

/**
 * Created by aneurinc on 16/04/2016.
 */
public class ParentEventActAdapter extends ArrayAdapter<IChildEvent> {

    private final Activity mContext;
    private List<IChildEvent> mChildEventsList;

    public ParentEventActAdapter(Activity context, List<IChildEvent> childEvents) {
        super(context, R.layout.list_row_child_event);
        mContext = context;
        mChildEventsList = childEvents;
    }

    @Override
    public IChildEvent getItem(int position) {
        return mChildEventsList.get(position);
    }

    @Override
    public int getCount() {
        return mChildEventsList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IChildEvent currChildEvent = getItem(position);

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

        int xy = ImageUtils.getScreenWidth(mContext) / 5;
        Bitmap scaledImage = ImageUtils.scaleDown(currChildEvent.getImage(0), xy, xy);

        viewHolder.childEventImage.setImageBitmap(scaledImage);
        viewHolder.childEventName.setText(currChildEvent.getName());
        try {
            viewHolder.childEventVenue.setText(currChildEvent.getVenue().toString());
        } catch (IOException e) {
            e.printStackTrace();
            // TODO handle IOException
        }
        viewHolder.childEventDate.setText(currChildEvent.getStartDateTime().toString() + " - " +
                currChildEvent.getEndDateTime().toString());

        return convertView;

    }

    static class ViewHolder {
        ImageView childEventImage;
        TextView childEventName;
        TextView childEventVenue;
        TextView childEventDate;
    }
}
