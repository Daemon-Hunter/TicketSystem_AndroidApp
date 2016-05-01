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

import java.util.List;

/**
 * Created by aneurinc on 25/04/2016.
 */
public class VenueActAdapter extends ArrayAdapter<IChildEvent> {

    private Activity mContext;
    private List<IChildEvent> mChildEvents;

    public VenueActAdapter(Activity context, List<IChildEvent> childEvents) {
        super(context, R.layout.list_row_child_events);
        mContext = context;
        mChildEvents = childEvents;
    }

    @Override
    public IChildEvent getItem(int position) {
        return mChildEvents.get(position);
    }

    @Override
    public int getCount() {
        return mChildEvents.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IChildEvent currItem = getItem(position);

        if (convertView == null) {

            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_parent_event, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.childEventImage = (ImageView) convertView.findViewById(R.id
                    .child_event_venue_image);
            viewHolder.childEventTitle = (TextView) convertView.findViewById(R.id
                    .child_event_title);
            viewHolder.childEventDate = (TextView) convertView.findViewById(R.id.child_event_date);

            convertView.setTag(viewHolder);

        } else {
            // use view holder to save resources
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // alternate colour of rows
        int colorPos = position % Constants.rowColour.length;
        convertView.setBackgroundColor(Constants.rowColour[colorPos]);

        int xy = ImageUtils.getScreenWidth(mContext) / 5;
        Bitmap scaledImage = ImageUtils.scaleDown(currItem.getImage(0), xy, xy);

        String startDate = currItem.getStartDateTime().toString();
        String endDate = currItem.getEndDateTime().toString();
        viewHolder.childEventDate.setText(startDate.substring(0, 10) + " - " + endDate.substring
                (0, 10));
        viewHolder.childEventImage.setImageBitmap(scaledImage);
        viewHolder.childEventTitle.setText(currItem.getName());

        return convertView;
    }

    static class ViewHolder {
        ImageView childEventImage;
        TextView childEventTitle;
        TextView childEventDate;
    }
}
