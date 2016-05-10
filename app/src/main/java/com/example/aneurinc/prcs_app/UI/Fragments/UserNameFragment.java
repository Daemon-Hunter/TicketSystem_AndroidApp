package com.example.aneurinc.prcs_app.UI.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.example.aneurinc.prcs_app.R;
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

/**
 * Created by aneurinc on 10/05/2016.
 */
public class UserNameFragment extends Fragment {

    private AutoCompleteTextView mForename, mSurname;
    private ICustomer mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_name, container, false);

        mUser = (ICustomer) UserWrapper.getInstance().getUser();
        mForename = (AutoCompleteTextView) view.findViewById(R.id.forename);
        mSurname = (AutoCompleteTextView) view.findViewById(R.id.surname);
        mForename.setText(mUser.getFirstName());
        mSurname.setText(mUser.getLastName());

        return view;

    }
}
