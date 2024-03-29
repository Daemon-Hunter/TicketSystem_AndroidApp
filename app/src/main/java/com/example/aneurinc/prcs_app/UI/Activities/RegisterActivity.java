package com.example.aneurinc.prcs_app.UI.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.custom_views.CustomClickableSpan;
import com.google.jkellaway.androidapp_datamodel.people.Customer;
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity implements OnEditorActionListener, OnClickListener {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mForenameView;
    private AutoCompleteTextView mSurnameView;
    private AutoCompleteTextView mAddress;
    private AutoCompleteTextView mCity;
    private AutoCompleteTextView mPostcode;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.colorTeal));

        setContentView(R.layout.activity_register);

        // Set up the registration form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mForenameView = (AutoCompleteTextView) findViewById(R.id.forename);
        mSurnameView = (AutoCompleteTextView) findViewById(R.id.surname);
        mAddress = (AutoCompleteTextView) findViewById(R.id.address);
        mCity = (AutoCompleteTextView) findViewById(R.id.city);
        mPostcode = (AutoCompleteTextView) findViewById(R.id.postcode);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(this);
        Button mEmailSignInButton = (Button) findViewById(R.id.confirm_order);
        mEmailSignInButton.setOnClickListener(this);
        mLoginFormView = findViewById(R.id.form);
        mProgressView = findViewById(R.id.progress);

        setClickableSpan();
    }

    /*
    * Add link to text to login page
    */
    private void setClickableSpan() {

        TextView tvSignIn = (TextView) findViewById(R.id.tv_go_to_sign_in);
        String strSignIn = tvSignIn.getText().toString();

        SpannableString ssSignIn = new SpannableString(strSignIn);
        ssSignIn.setSpan(new CustomClickableSpan(this), 20, strSignIn.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignIn.setText(ssSignIn);
        tvSignIn.setMovementMethod(LinkMovementMethod.getInstance());

    }

    /**
     * Attempts to register the account specified by the registration form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual registration attempt is made.
     */
    private void attemptRegistration() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mForenameView.setError(null);
        mSurnameView.setError(null);
        mAddress.setError(null);
        mPostcode.setError(null);
        mPasswordView.setError(null);
        mCity.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String forename = mForenameView.getText().toString();
        String surname = mSurnameView.getText().toString();
        String address = mAddress.getText().toString();
        String postcode = mPostcode.getText().toString();
        String password = mPasswordView.getText().toString();
        String city = mCity.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // check for a valid forename, if the user entered one.
        if (TextUtils.isEmpty(forename)) {
            mForenameView.setError(getString(R.string.error_field_required));
            focusView = mForenameView;
            cancel = true;
        }

        // check for a valid surname, if the user entered one.
        if (TextUtils.isEmpty(surname)) {
            mSurnameView.setError(getString(R.string.error_field_required));
            focusView = mSurnameView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // check for a valid address, if the user entered one.
        if (TextUtils.isEmpty(address)) {
            mAddress.setError(getString(R.string.error_field_required));
            focusView = mAddress;
            cancel = true;
        }

        // check for a valid city, if the user entered one.
        if (TextUtils.isEmpty(city)) {
            mCity.setError(getString(R.string.error_field_required));
            focusView = mCity;
            cancel = true;
        }

        // check for a valid postcode, if the user entered one.
        if (TextUtils.isEmpty(postcode)) {
            mPostcode.setError(getString(R.string.error_field_required));
            focusView = mPostcode;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background1 task to
            // perform the user register attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(this, forename, surname, email, address, postcode, password);
            mAuthTask.execute();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /*
    * Handle onClick events
    * Callback fired when user clicks on a view with a listener
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_order:
                // close the soft keyboard and attempt registration task
                closeKeyboard();
                attemptRegistration();
                break;
            default:
                break;
        }
    }

    /*
    * Close the Soft Keyboard
    */
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /*
    * Called when an edit text has its contents changed or selected
    */
    @Override
    public boolean onEditorAction(TextView v, int id, KeyEvent event) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            attemptRegistration();
            return true;
        }
        return false;
    }

    /*
    * Override default transition with a fade transition
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final Activity mContext;
        private String mForename, mSurname, mEmail, mAddress, mPostcode, mPassword, mErrorMessage;


        // Pass in user details from form input
        public UserLoginTask(Activity mContext, String mForename, String mSurname,
                             String mEmail, String mAddress, String mPostcode, String mPassword) {
            this.mContext = mContext;
            this.mForename = mForename;
            this.mSurname = mSurname;
            this.mEmail = mEmail;
            this.mAddress = mAddress;
            this.mPostcode = mPostcode;
            this.mPassword = mPassword;
        }

        /*
        * Execute registration task - create new Customer object and register them
        * Return true if no issues with registration
        */
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                ICustomer mCustomer = new Customer(mForename, mSurname, mEmail, mAddress,
                        mPostcode, mPassword);
                UserWrapper.getInstance().registerUser(mCustomer);
            } catch (IOException e) {
                e.printStackTrace();
                mErrorMessage = e.getMessage();
                return false;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                mErrorMessage = e.getMessage();
                return false;
            }

            return true;
        }

        /*
        * Called once task is finished
        * If registration is successful, start login activity
        */
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                startActivity(new Intent(mContext, LogInActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {
                Toast.makeText(mContext, mErrorMessage, Toast.LENGTH_SHORT).show();
            }
        }

        /*
        * Callback fired once the task is cancelled
        */
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
