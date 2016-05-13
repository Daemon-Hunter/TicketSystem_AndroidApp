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
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.events.IChildEvent;

import java.util.List;

/**
 * Created by aneurinc on 25/04/2016.
 */
public class VenueActAdapter extends ArrayAdapter<IChildEvent> {

    // Reference to parent activity
    private Activity mContext;

    // Adapter list
    private List<IChildEvent> mChildEvents;

    // Row colours
    private static final int ROW_COLOUR1 = 0x3003a9f4;
    private static final int ROW_COLOUR2 = 0x3081d4fa;

    /*
    * Initialise adapter
    */
    public VenueActAdapter(Activity context, List<IChildEvent> childEvents) {
        super(context, R.layout.list_row_artist_child_events);
        mContext = context;
        mChildEvents = childEvents;
    }

    /*
    * Return alternate row colours
    */
    private int getRowColour(int position) {
        return position % 2 == 0 ? ROW_COLOUR1 : ROW_COLOUR2;
    }

    /*
    * Return list item by index
    */
    @Override
    public IChildEvent getItem(int position) {
        return mChildEvents.get(position);
    }

    /*
    * Return list size
    */
    @Override
    public int getCount() {
        return mChildEvents.size();
    }

    /*
    * Return row view
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IChildEvent currItem = getItem(position);

        if (convertView == null) {

            // inflate layout
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_parent_event, parent, false);

            // Set up view holder
            viewHolder = new ViewHolder();
            viewHolder.childEventImage = (ImageView) convertView.findViewById(R.id.child_event_venue_image);
            viewHolder.childEventTitle = (TextView) convertView.findViewById(R.id.child_event_title);
            viewHolder.childEventDate = (TextView) convertView.findViewById(R.id.child_event_date);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {
            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // alternate colour of rows
        convertView.setBackgroundColor(getRowColour(position));

        int xy = Utilities.getScreenWidth(mContext) / 5;
        Bitmap scaledImage = Utilities.scaleDown(currItem.getImage(0), xy, xy);

        String startDate = currItem.getStartDateTime().toString();
        String endDate = currItem.getEndDateTime().toString();
        viewHolder.childEventDate.setText(startDate.substring(0, 10) + " - " + endDate.substring(0, 10));
        viewHolder.childEventImage.setImageBitmap(scaledImage);
        viewHolder.childEventTitle.setText(currItem.getName());

        return convertView;
    }

    /*
    * Static view holder class to keep reference to row views
    */
    static class ViewHolder {
        ImageView childEventImage;
        TextView childEventTitle;
        TextView childEventDate;
    }
}
