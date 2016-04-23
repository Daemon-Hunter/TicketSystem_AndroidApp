package com.example.aneurinc.prcs_app.UI.CustomAdapters;

/**
 * Created by Dominic on 14/04/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Utilities.ImageUtils;
import com.google.jkellaway.androidapp_datamodel.events.IArtist;

import java.util.List;

public class ArtistFragAdapter extends BaseAdapter {

    private Activity mContext;
    private List<IArtist> mArtistList;

    public ArtistFragAdapter(Activity c, List<IArtist> artistList) {
        mContext = c;
        mArtistList = artistList;
    }

    @Override
    public int getCount() {
        return mArtistList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArtistList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        IArtist currArtist = (IArtist) getItem(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_single, null);

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
            viewHolder.gridImage.setImageBitmap(ImageUtils.scaleDown(currArtist.getImage(0), xy, xy));
        }

        viewHolder.gridText.setText(currArtist.getName());

        return convertView;
    }

    static class ViewHolder {
        ImageView gridImage;
        TextView gridText;
    }


}