package com.example.aneurinc.prcs_app.UI.custom_adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aneurinc.prcs_app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aneurinc on 10/05/2016.
 */
public class UserProfileAdapter extends ArrayAdapter<String> {

    // Reference to parent activity
    private Activity mContext;

    // List of customer field and images
    private List<String> mCustomerFields;
    private List<Integer> mImages;

    /*
    * Initialise adapter
    */
    public UserProfileAdapter(Activity context) {
        super(context, R.layout.list_row_user_profile);
        mContext = context;

        // Initialise lists
        mCustomerFields = new ArrayList<>();
        mCustomerFields.add("Name");
        mCustomerFields.add("Email Address");
        mCustomerFields.add("Change Password");
        mCustomerFields.add("Change Address");

        mImages = new ArrayList<>();
        mImages.add(R.drawable.ic_perm_identity);
        mImages.add(R.drawable.ic_email);
        mImages.add(R.drawable.ic_lock);
        mImages.add(R.drawable.ic_location_city);
    }

    /*
    * Return list size
    */
    @Override
    public int getCount() {
        return mCustomerFields.size();
    }

    /*
    * Return image by position
    * */
    private Integer getImage(int position) { return mImages.get(position); }

    /*
    * Return customer field string by position
    */
    @Override
    public String getItem(int position) {
        return mCustomerFields.get(position);
    }

    /*
    * Return row view
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder;

        if (convertView == null) {

            // inflate layout
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_user_profile, parent, false);

            // Set up view holder
            mViewHolder = new ViewHolder();
            mViewHolder.image = (ImageView) convertView.findViewById(R.id.item_image);
            mViewHolder.detail = (TextView) convertView.findViewById(R.id.user_detail);

            // store the holder with the view
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.detail.setText(getItem(position));
        mViewHolder.image.setImageDrawable(mContext.getDrawable(getImage(position)));

        return convertView;
    }

    /*
    * Static view holder class to keep reference to row views
    */
    static class ViewHolder {
        TextView detail;
        ImageView image;
    }
}
