package com.example.aneurinc.prcs_app.UI;

/**
 * Created by Dominic on 14/04/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.Datamodel.Artist;
import com.example.aneurinc.prcs_app.R;

import java.util.List;

public class ArtistsGridAdapter extends BaseAdapter {

    private Context context;
    private Integer[] images = Constants.artistImages;
    private String[] title;



    public ArtistsGridAdapter(Context c, List<Artist> artistList)
    {
        context = c;
        updateGridList(artistList);

    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        viewHolder.gridImage.setImageResource(images[position]);
        viewHolder.gridImage.getLayoutParams().height = 275;
        viewHolder.gridImage.getLayoutParams().width = 275;
        viewHolder.gridText.setText(title[position]);

        return convertView;
    }

    public void updateGridList(List<Artist> artists) {

        title = new String[artists.size()];
        int i = 0;
        for (Artist currArtist:artists) {

            title[i] = currArtist.getArtistName();
            i++;
        }

    }



    static class ViewHolder {
        ImageView gridImage;
        TextView gridText;
    }




}