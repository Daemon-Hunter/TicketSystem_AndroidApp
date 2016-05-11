package com.example.aneurinc.prcs_app.UI.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
public class UserPasswordFragment extends Fragment implements View.OnClickListener {

    private UpdateDetails mUpdateTask;
    private EditText mPassword;
    private ICustomer mUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_password, container, false);

        mUser = (ICustomer) UserWrapper.getInstance().getUser();
        mPassword = (EditText) view.findViewById(R.id.password);

        return view;
    }

    private void updateDetails() {
        if (!isTaskRunning(mUpdateTask)) {
            mUpdateTask = new UpdateDetails(mPassword.getText().toString());
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
        return !mPassword.getText().toString().isEmpty();
    }

    @Override
    public void onClick(View v) {
        if (valuesHaveChanged()) updateDetails();
    }

    private class UpdateDetails extends AsyncTask<Void, Void, Boolean> {

        private String newPassword;

        public UpdateDetails(String newPassword) {
            this.newPassword = newPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mUser.setPassword(newPassword);
                mUser = new Customer(mUser.getID(), mUser.getFirstName(), mUser.getLastName(),
                        mUser.getEmail(), mUser.getAddress(), mUser.getPostcode(), newPassword);
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
            else message = "There was a problem. Please try again.";

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
