package com.example.aneurinc.prcs_app.UI.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.aneurinc.prcs_app.R;
import com.example.aneurinc.prcs_app.UI.Activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.CustomAdapters.TicketFragAdapter;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by aneurinc on 01/03/2016.
 */
public class TicketFragment extends Fragment {

    private ProgressBar mProgressBar;
    private ReadTickets mTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        mProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.ticket_progress);
        getTickets();
        return view;
    }

    private void getTickets() {
        mTask = new ReadTickets(getActivity());
        mTask.execute();
    }

    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onPause() {
        Log.d(MainActivity.DEBUG_TAG, "onPause: ");

        if (isTaskRunning()) {
            mTask.cancel(true);
        }

        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(MainActivity.DEBUG_TAG, "onStop: ");

        if (isTaskRunning()) {
            mTask.cancel(true);
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(MainActivity.DEBUG_TAG, "onDestroy: ");

        if (isTaskRunning()) {
            mTask.cancel(true);
        }

        super.onDestroy();
    }

    private boolean isTaskRunning() {
        return mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING;
    }


    private class ReadTickets extends AsyncTask<Void, Void, List<ITicket>> {

        private final Activity mContext;

        public ReadTickets(Activity context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected List<ITicket> doInBackground(Void... params) {
            return new LinkedList<>();
        }

        @Override
        protected void onPostExecute(List<ITicket> tickets) {
            showProgress(false);
            if (tickets != null && !tickets.isEmpty()) {
                ListView list = (ListView) mContext.findViewById(R.id.my_tickets_list);
                list.setAdapter(new TicketFragAdapter(mContext, tickets));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mTask.cancel(true);
        }
    }
}
