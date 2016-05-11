package com.example.aneurinc.prcs_app.UI.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.people.Customer;
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;

/**
 * Created by aneurinc on 10/05/2016.
 */
public class UserAddressFragment extends Fragment implements View.OnClickListener {

    private AutoCompleteTextView mAddress, mCity, mPostcode;
    private ICustomer mUser;
    private UpdateDetails mUpdateTask;
    private String oldAddress, oldCity, oldPostcode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_address, container, false);

        mUser = (ICustomer) UserWrapper.getInstance().getUser();
        mAddress = (AutoCompleteTextView) view.findViewById(R.id.address);
        mCity = (AutoCompleteTextView) view.findViewById(R.id.city);
        mPostcode = (AutoCompleteTextView) view.findViewById(R.id.postcode);
        mAddress.setText(mUser.getAddress());
        mCity.setText("CITY NOT IN DATABASE");
        mPostcode.setText(mUser.getPostcode());

        oldAddress = mAddress.getText().toString();
        oldCity = mCity.getText().toString();
        oldPostcode = mPostcode.getText().toString();

        return view;
    }

    private void updateDetails() {
        if (!isTaskRunning(mUpdateTask)) {
            mUpdateTask = new UpdateDetails(mAddress.getText().toString(), mCity.getText().toString(), mPostcode.getText().toString());
            mUpdateTask.execute();
        }
    }

    @Override
    public void onPause() {
        handleQuit();
        super.onPause();
    }

    @Override
    public void onStop() {
        handleQuit();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        handleQuit();
        super.onDestroy();
    }

    private void handleQuit() {
        if (isTaskRunning(mUpdateTask)) {
            mUpdateTask.cancel(true);
        }
    }

    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    private boolean valuesHaveChanged() {
        String newAddress = mAddress.getText().toString();
        String newCity = mCity.getText().toString();
        String newPostcode = mPostcode.getText().toString();
        return !(newAddress.equals(oldAddress) && newCity.equals(oldCity) && newPostcode.equals(oldPostcode));
    }

    @Override
    public void onClick(View v) {
        if (valuesHaveChanged()) updateDetails();
    }

    private class UpdateDetails extends AsyncTask<Void, Void, Boolean> {

        private String newAddress, newCity, newPostcode;

        public UpdateDetails(String newAddress, String newCity, String newPostcode) {
            this.newAddress = newAddress;
            this.newCity = newCity;
            this.newPostcode = newPostcode;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mUser = new Customer(mUser.getID(), mUser.getFirstName(), mUser.getLastName(),
                        mUser.getEmail(), newAddress, newPostcode, "");
                UserWrapper.getInstance().updateObject(mUser, DatabaseTable.CUSTOMER);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            String message;

            if (success) message = getString(R.string.update_profile);
            else message = "There was a network problem. Please try again.";

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
