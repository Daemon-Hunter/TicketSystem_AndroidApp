package com.example.aneurinc.prcs_app.UI.custom_adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.utilities.Utilities;
import com.google.jkellaway.androidapp_datamodel.events.IParentEvent;

import java.util.Collection;
import java.util.List;

/**
 * Created by Dominic on 15/04/2016.
 */
public class ParentEventFragAdapter extends ArrayAdapter<IParentEvent> {

    // Reference to parent activity
    private Activity mContext;

    // Adapter list
    private List<IParentEvent> mParentEvents;

    /*
    * Initialise adapter
    */
    public ParentEventFragAdapter(Activity context, List<IParentEvent> parentEvents) {
        super(context, R.layout.gridview_single);
        mContext = context;
        mParentEvents = parentEvents;
    }

    /*
    * Clear list
    */
    @Override
    public void clear() {
        mParentEvents.clear();
    }

    /*
    * Add collection to list
    */
    @Override
    public void addAll(Collection<? extends IParentEvent> collection) {
        mParentEvents.addAll(collection);
    }

    /*
    * Return list size
    */
    @Override
    public int getCount() {
        return mParentEvents.size();
    }

    /*
    * Get item from list at position index
    */
    @Override
    public IParentEvent getItem(int position) {
        return mParentEvents.get(position);
    }

    /*
    * Return row view
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IParentEvent currParentEvent = getItem(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_single, null);

            // set up view holder
            viewHolder = new ViewHolder();
            viewHolder.gridImage = (ImageView) convertView.findViewById(R.id.grid_image);
            viewHolder.gridText = (TextView) convertView.findViewById(R.id.grid_text);

            // store the holder with the view
            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        // Calculate screen width and adjust image accrodingly
        int xy = mContext.findViewById(R.id.event_grid_view).getWidth() / 3;
        if (xy > 0) {
            viewHolder.gridImage.setImageBitmap(Utilities.scaleDown(currParentEvent.getImage(0), xy, xy));
        }
        viewHolder.gridText.setText(currParentEvent.getName());

        return convertView;
    }

    /*
    * Static view holder class to keep reference to row views
    */
    static class ViewHolder {
        ImageView gridImage;
        TextView gridText;
    }

}
