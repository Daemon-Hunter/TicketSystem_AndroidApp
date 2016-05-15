package com.example.aneurinc.prcs_app.UI.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_views.CustomPasswordDialog;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.people.Customer;
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;

/**
 * Created by aneurinc on 10/05/2016.
 */
public class UserPasswordFragment extends Fragment implements View.OnClickListener {

    // Reference to parent activity
    private UpdateDetails mUpdateTask;

    // UI references
    private EditText mOldPassword, mNewPassword;

    // The user
    private ICustomer mUser;

    // Authentication task
    private AuthenticatePassword mAuthTask;

    // Confirmation password dialog
    private CustomPasswordDialog mDialog;

    /*
    * Initialise fragment components and load layout
    */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_password, container, false);

        mUser = (ICustomer) UserWrapper.getInstance().getUser();
        mOldPassword = (EditText) view.findViewById(R.id.password);

        mDialog = new CustomPasswordDialog(getActivity());
        mDialog.create();
        mNewPassword = (EditText) mDialog.findViewById(R.id.password);
        Button confirmPassword = (Button) mDialog.findViewById(R.id.confirm_password);
        Button updatePassword = (Button) view.findViewById(R.id.update_password);
        confirmPassword.setOnClickListener(this);
        updatePassword.setOnClickListener(this);

        return view;
    }

    /*
    * Call update Async task if it is not already running
    */
    private void updateDetails() {
        if (!isTaskRunning(mUpdateTask)) {
            mUpdateTask = new UpdateDetails(mOldPassword.getText().toString(), mUser.getEmail());
            mUpdateTask.execute();
        }
    }

    /*
    * Call authenticate Async task if it is not already running
    */
    private void authenticatePassword() {
        if (!isTaskRunning(mAuthTask)) {
            mAuthTask = new AuthenticatePassword(getActivity(), mNewPassword.getText().toString(), mUser.getEmail());
            mAuthTask.execute();
        }
    }

    /*
    * Called when the fragment is paused
    * Cancel all running threads
    */
    @Override
    public void onPause() {
        handleQuit();
        super.onPause();
    }

    /*
    * Called when the fragment is stopped
    * Cancel all running threads
    */
    @Override
    public void onStop() {
        handleQuit();
        super.onStop();
    }

    /*
    * Called when the fragment is destroyed
    * Cancel all running threads
    */
    @Override
    public void onDestroy() {
        handleQuit();
        super.onDestroy();
    }

    /*
    * Called when fragment is paused, stopped or destroyed
    * Checks if reading thread is running and cancels it if necessary
    */
    private void handleQuit() {
        if (isTaskRunning(mUpdateTask)) {
            mUpdateTask.cancel(true);
        }
    }

    /*
    * Check if passed in thread is in the Running state
    */
    private boolean isTaskRunning(AsyncTask task) {
        return task != null && task.getStatus() == AsyncTask.Status.RUNNING;
    }

    /*
    * Compares the new values equal the old values to see if there is a change
    */
    private boolean valuesHaveChanged() {
        return !mOldPassword.getText().toString().isEmpty();
    }

    /*
   * Handles onclick listener
   */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_password:
                if (valuesHaveChanged()) mDialog.show();
                break;
            case R.id.confirm_password:
                authenticatePassword();
            default:
                break;
        }
    }

    /*
    * Hide soft keyboard
    */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isAcceptingText()) imm.showSoftInputFromInputMethod(getView().getWindowToken(), 0);
    }

    /*
    * Async task class to update user details that have been changed on the form
    */
    private class UpdateDetails extends AsyncTask<Void, Void, Boolean> {

        private String newPassword, userEmail;

        /*
        * User input from the form is passed in
        */
        public UpdateDetails(String newPassword, String userEmail) {
            this.newPassword = newPassword;
            this.userEmail = userEmail;
        }

        /*
       * Update task is handled here
       * New user is created with details and passed to update method in wrapper
       * Boolean is returned to indicate success of database operation
       * */
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mUser = new Customer(mUser.getID(), mUser.getFirstName(), mUser.getLastName(), mUser.getEmail(), mUser.getAddress(), mUser.getPostcode(), newPassword);
                UserWrapper.getInstance().updateObject(mUser, DatabaseTable.CUSTOMER);
                UserWrapper.getInstance().loginUser(userEmail, newPassword);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        /*
       * Callback fired once async task is completed
       * Boolean success determines if async task finished properly or not
       * UI is updated accordingly
       */
        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            mUpdateTask = null;
            String message;

            if (success) message = getString(R.string.profile_updated);
            else message = getString(R.string.network_problem);

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        /*
       * Callback fired if thread is cancelled
       */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            mUpdateTask = null;
        }
    }

    /*
    * Authenticate async task checks the customers password against the database
    */
    private class AuthenticatePassword extends AsyncTask<Void, Void, Boolean> {

        private String userEmail, userPassword;
        private Activity mContext;

        /*
        * Pass in reference to parent activity and username and password
        */
        public AuthenticatePassword(Activity mContext, String userPassword, String userEmail) {
            this.mContext = mContext;
            this.userPassword = userPassword;
            this.userEmail = userEmail;
        }

        /*
       * Thread task is handled here. Login is attempted with user credentials
       * If login is valid, true is returned
       */
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                UserWrapper.getInstance().loginUser(userEmail, userPassword);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        /*
        * Callback fired once async task is completed
        * Boolean success determines if async task finished properly or not
        * Dialog is closed if task was successful
        */
        @Override
        protected void onPostExecute(Boolean success) {
            mAuthTask = null;
            super.onPostExecute(success);
            if (success) {
                updateDetails();
                mDialog.cancel();
            } else Toast.makeText(mContext, R.string.invalid_password, Toast.LENGTH_SHORT).show();
        }

        /*
        * Callback fired if thread is cancelled
        */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            mAuthTask = null;
        }
    }
}
