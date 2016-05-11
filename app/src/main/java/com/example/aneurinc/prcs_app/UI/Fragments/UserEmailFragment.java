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
public class UserEmailFragment extends Fragment implements View.OnClickListener {

    private AutoCompleteTextView mEmail;
    private String oldEmail;
    private ICustomer mUser;
    private UpdateDetails mUpdateTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_email, container, false);

        mUser = (ICustomer) UserWrapper.getInstance().getUser();
        mEmail = (AutoCompleteTextView) view.findViewById(R.id.email);
        mEmail.setText(mUser.getEmail());
        oldEmail = mEmail.getText().toString();

        return view;
    }

    private void updateDetails() {
        if (!isTaskRunning(mUpdateTask)) {
            mUpdateTask = new UpdateDetails(mEmail.getText().toString());
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
        String newEmail = mEmail.getText().toString();
        return !newEmail.equals(oldEmail);
    }

    @Override
    public void onClick(View v) {
        if (valuesHaveChanged()) updateDetails();
    }

    private class UpdateDetails extends AsyncTask<Void, Void, Boolean> {

        private String newEmail;

        public UpdateDetails(String newEmail) {
            this.newEmail = newEmail;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mUser.setEmail(newEmail);
                mUser = new Customer(mUser.getID(), mUser.getFirstName(), mUser.getLastName(), newEmail,
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
