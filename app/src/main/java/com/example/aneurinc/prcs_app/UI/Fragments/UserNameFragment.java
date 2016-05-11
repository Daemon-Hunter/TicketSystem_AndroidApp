package com.example.aneurinc.prcs_app.UI.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
public class UserNameFragment extends Fragment implements View.OnClickListener {

    private AutoCompleteTextView mForename, mSurname;
    private UpdateDetails mUpdateTask;
    private String oldForename, oldSurname;
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
        oldForename = mForename.getText().toString();
        oldSurname = mSurname.getText().toString();

        Button updateName = (Button) view.findViewById(R.id.update_name);
        updateName.setOnClickListener(this);

        return view;

    }

    private void updateDetails() {
        if (!isTaskRunning(mUpdateTask)) {
            mUpdateTask = new UpdateDetails(mForename.getText().toString(), mSurname.getText().toString());
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
        String newForename = mForename.getText().toString();
        String newSurname = mSurname.getText().toString();
        return !(newForename.equals(oldForename) && newSurname.equals(oldSurname));
    }

    @Override
    public void onClick(View v) {
        if (valuesHaveChanged()) updateDetails();
    }

    private class UpdateDetails extends AsyncTask<Void, Void, Boolean> {

        private String newForename, newSurname;

        public UpdateDetails(String newForename, String newSurname) {
            this.newForename = newForename;
            this.newSurname = newSurname;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mUser = new Customer(mUser.getID(), newForename, newSurname, mUser.getEmail(),
                        mUser.getAddress(), mUser.getPostcode(), "");
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
