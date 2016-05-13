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
 * Created by aneurinc on 16/04/2016.
 */
public class ParentEventActAdapter extends ArrayAdapter<IChildEvent> {

    // Reference to parent activity
    private final Activity mContext;

    // Adapter list
    private List<IChildEvent> mChildEventsList;

    // Row colours
    private static final int ROW_COLOUR1 = 0x3003a9f4;
    private static final int ROW_COLOUR2 = 0x3081d4fa;

    /*
    * Initialise adapter
    */
    public ParentEventActAdapter(Activity context, List<IChildEvent> childEvents) {
        super(context, R.layout.list_row_parent_event);
        mContext = context;
        mChildEventsList = childEvents;
    }

    /*
    * Return alternate row colours
    */
    private int getRowColour(int position) {
        return position % 2 == 0 ? ROW_COLOUR1 : ROW_COLOUR2;
    }

    /*
    * Get list item at position index
    */
    @Override
    public IChildEvent getItem(int position) {
        return mChildEventsList.get(position);
    }

    /*
    * Return size of list
    */
    @Override
    public int getCount() {
        return mChildEventsList.size();
    }

    /*
    * Return row view
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IChildEvent currChildEvent = getItem(position);

        if (convertView == null) {
            // inflate view
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_parent_event, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.childEventImage = (ImageView) convertView.findViewById(R.id.child_event_venue_image);
            viewHolder.childEventName = (TextView) convertView.findViewById(R.id.child_event_title);
            viewHolder.childEventDate = (TextView) convertView.findViewById(R.id.child_event_date);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {
            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // alternate list view row colour
        convertView.setBackgroundColor(getRowColour(position));

        // Calculate screen width and resize image accordingly
        int xy = Utilities.getScreenWidth(mContext) / 5;
        Bitmap scaledImage = Utilities.scaleDown(currChildEvent.getVenue().getImage(0), xy, xy);
        viewHolder.childEventImage.setImageBitmap(scaledImage);

        viewHolder.childEventName.setText(currChildEvent.getName());

        String startDate = currChildEvent.getStartDateTime().toString().substring(0, 10);
        String endDate = currChildEvent.getEndDateTime().toString().substring(0, 10);
        viewHolder.childEventDate.setText(Utilities.formatDateDuration(startDate, endDate));

        return convertView;

    }

    /*
    * Static view holder class to keep reference to row views
    */
    static class ViewHolder {
        ImageView childEventImage;
        TextView childEventName;
        TextView childEventDate;
    }
}
