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

    private Activity mContext;
    private List<String> mCustomerFields;
    private List<Integer> mImages;

    public UserProfileAdapter(Activity context) {
        super(context, R.layout.list_row_user_profile);
        mContext = context;

        mCustomerFields = new ArrayList<>();
        mCustomerFields.add("Name");
        mCustomerFields.add("Email Address");
        mCustomerFields.add("Change Password");
        mCustomerFields.add("Change Address");

        mImages = new ArrayList<>();
        mImages.add(R.drawable.ic_perm_identity_48pt_3x);
        mImages.add(R.drawable.ic_email_48pt_3x);
        mImages.add(R.drawable.ic_lock_outline_48pt_3x);
        mImages.add(R.drawable.ic_location_city_48pt_3x);
    }

    @Override
    public int getCount() {
        return mCustomerFields.size();
    }

    private Integer getImage(int position) { return mImages.get(position); }

    @Override
    public String getItem(int position) {
        return mCustomerFields.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder;

        if (convertView == null) {

            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_user_profile, parent, false);

            mViewHolder = new ViewHolder();
            mViewHolder.image = (ImageView) convertView.findViewById(R.id.item_image);
            mViewHolder.detail = (TextView) convertView.findViewById(R.id.user_detail);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.detail.setText(getItem(position));
        mViewHolder.image.setImageDrawable(mContext.getDrawable(getImage(position)));

        return convertView;
    }

    static class ViewHolder {
        TextView detail;
        ImageView image;
    }
}
