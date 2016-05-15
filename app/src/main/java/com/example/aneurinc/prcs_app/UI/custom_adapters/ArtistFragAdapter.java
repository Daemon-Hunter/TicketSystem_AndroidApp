package com.example.aneurinc.prcs_app.UI.custom_adapters;

/**
 * Created by Dominic on 14/04/2016.
 */

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
import com.google.jkellaway.androidapp_datamodel.events.IArtist;

import java.util.Collection;
import java.util.List;

public class ArtistFragAdapter extends ArrayAdapter<IArtist> {

    // Reference to parent activity
    private Activity mContext;

    // Artist list for adapter
    private List<IArtist> mArtistList;

    /*
    * Initialise artist adapter
    */
    public ArtistFragAdapter(Activity context, List<IArtist> artistList) {
        super(context, R.layout.gridview_single);
        mContext = context;
        mArtistList = artistList;
    }

    /*
    * Clear list
    */
    @Override
    public void clear() {
        mArtistList.clear();
    }

    /*
    * Add collection to list
    */
    @Override
    public void addAll(Collection<? extends IArtist> collection) {
        mArtistList.addAll(collection);
    }

    /*
    * Return list size
    */
    @Override
    public int getCount() {
        return mArtistList.size();
    }

    /*
    * Get item in list by position
    */
    @Override
    public IArtist getItem(int position) {
        return mArtistList.get(position);
    }

    /*
    * Return row view
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IArtist currArtist = getItem(position);

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

        if (currArtist.getImage(0) != null) {
            // get width of single grid
            int xy = mContext.findViewById(R.id.artist_grid_view).getWidth() / 3;
            // resize image to fit single grid
            if (xy > 0) {
                viewHolder.gridImage.setImageBitmap(Utilities.scaleDown(currArtist.getImage(0), xy, xy));
            }
        }

        viewHolder.gridText.setText(currArtist.getName());

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