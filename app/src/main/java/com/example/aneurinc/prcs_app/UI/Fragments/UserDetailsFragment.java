package com.example.aneurinc.prcs_app.UI.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.activities.UserProfileActivity;
import com.example.aneurinc.prcs_app.UI.custom_adapters.UserProfileAdapter;

/**
 * Created by aneurinc on 10/05/2016.
 */
public class UserDetailsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private UserProfileActivity mUserProfileActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_details, container, false);

        ListView mListView = (ListView) view.findViewById(R.id.user_details_list);
        mListView.setAdapter(new UserProfileAdapter(getActivity()));
        mListView.setOnItemClickListener(this);

        if (getActivity() instanceof UserProfileActivity) {
            mUserProfileActivity = (UserProfileActivity) getActivity();
        }

        return view;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                mUserProfileActivity.switchFragment(new UserNameFragment());
                break;
            case 1:
                mUserProfileActivity.switchFragment(new UserEmailFragment());
                break;
            case 2:
                mUserProfileActivity.switchFragment(new UserPasswordFragment());
                break;
            case 3:
                mUserProfileActivity.switchFragment(new UserAddressFragment());
                break;
            default:
                break;
        }
    }
}
