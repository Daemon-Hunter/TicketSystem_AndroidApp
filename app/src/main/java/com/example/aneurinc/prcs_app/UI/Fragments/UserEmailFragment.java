package com.example.aneurinc.prcs_app.UI.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.custom_views.CustomPasswordDialog;
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
    private EditText mPassword;
    private CustomPasswordDialog mDialog;
    private AuthenticatePassword mAuthTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_email, container, false);

        mUser = (ICustomer) UserWrapper.getInstance().getUser();
        mEmail = (AutoCompleteTextView) view.findViewById(R.id.email);

        mEmail.setText(mUser.getEmail());
        oldEmail = mEmail.getText().toString();

        mDialog = new CustomPasswordDialog(getActivity());
        mDialog.create();

        mPassword = (EditText) mDialog.findViewById(R.id.password);

        Button confirmPassword = (Button) mDialog.findViewById(R.id.confirm_password);
        confirmPassword.setOnClickListener(this);

        Button updateEmail = (Button) view.findViewById(R.id.update_email);
        updateEmail.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void updateDetails() {
        if (!isTaskRunning(mUpdateTask)) {
            mUpdateTask = new UpdateDetails(mEmail.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString());
            mUpdateTask.execute();
        }
    }

    private void authenticatePassword() {
        if (!isTaskRunning(mAuthTask)) {
            mAuthTask = new AuthenticatePassword(getActivity(), mPassword.getText().toString(), mUser.getEmail());
            mAuthTask.execute();
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
        if (isTaskRunning(mAuthTask)) {
            mAuthTask.cancel(true);
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
        switch (v.getId()) {
            case R.id.update_email:
                if (valuesHaveChanged()) mDialog.show();
                break;
            case R.id.confirm_password:
                authenticatePassword();
            default:
                break;
        }
    }

    private class AuthenticatePassword extends AsyncTask<Void, Void, Boolean> {

        private String userEmail, userPassword;
        private Activity mContext;

        public AuthenticatePassword(Activity mContext, String userPassword, String userEmail) {
            this.mContext = mContext;
            this.userPassword = userPassword;
            this.userEmail = userEmail;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Log.d(MainActivity.DEBUG_TAG, "doInBackground: user password = " + userPassword);
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

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                updateDetails();
                mDialog.cancel();
            } else Toast.makeText(mContext, R.string.invalid_password, Toast.LENGTH_SHORT).show();
        }
    }

    private class UpdateDetails extends AsyncTask<Void, Void, Boolean> {

        private String newEmail, userEmail, userPassword;

        public UpdateDetails(String newEmail, String userEmail, String userPassword) {
            this.newEmail = newEmail;
            this.userEmail = userEmail;
            this.userPassword = userPassword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                mUser = new Customer(mUser.getID(), mUser.getFirstName(), mUser.getLastName(), newEmail, mUser.getAddress(), mUser.getPostcode(), userPassword);
                UserWrapper.getInstance().updateObject(mUser, DatabaseTable.CUSTOMER);
                UserWrapper.getInstance().loginUser(userEmail, userPassword);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (IllegalArgumentException e) {
                Log.d(MainActivity.DEBUG_TAG, "doInBackground: password = " + userPassword);
                Log.d(MainActivity.DEBUG_TAG, "doInBackground: email = " + userEmail);
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            String message;

            if (success) message = getString(R.string.profile_updated);
            else message = getString(R.string.network_problem);

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
