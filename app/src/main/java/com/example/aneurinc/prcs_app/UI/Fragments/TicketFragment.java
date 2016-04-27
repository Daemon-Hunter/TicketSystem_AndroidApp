package com.example.aneurinc.prcs_app.UI.fragments;

import android.animation.Animator;
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
import com.example.aneurinc.prcs_app.UI.activities.MainActivity;
import com.example.aneurinc.prcs_app.UI.custom_adapters.TicketFragAdapter;
import com.example.aneurinc.prcs_app.UI.custom_listeners.OnSwipeTouchListener;
import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.people.ICustomer;
import com.google.jkellaway.androidapp_datamodel.tickets.ITicket;
import com.google.jkellaway.androidapp_datamodel.wrappers.UserWrapper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by aneurinc on 01/03/2016.
 */
public class TicketFragment extends Fragment implements Animator.AnimatorListener {

    private ProgressBar mProgressBar;
    private ReadTickets mTask;
    private List<ITicket> mTickets;
    private static final int ANIM_TIME = 200;
    private MainActivity mMainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        if (getActivity() instanceof MainActivity) {
            mMainActivity = (MainActivity) getActivity();
        }

        setSwipe(view);

        mProgressBar = (ProgressBar) view.getRootView().findViewById(R.id.ticket_progress);
        getTickets();
        return view;
    }

    private void setSwipe(View v) {
        v.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeRight() {
                mMainActivity.switchFragment(new VenueFragment(), FragmentType.VENUE);
            }
        });
    }

    private void getTickets() {
        mTask = new ReadTickets(getActivity());
        mTask.execute();
    }

    private void showProgress(final boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressBar.animate().setDuration(ANIM_TIME).alpha(show ? 1 : 0).setListener(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

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
        if (isTaskRunning()) {
            mTask.cancel(true);
        }
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
            Log.d(MainActivity.DEBUG_TAG, "onPreExecute: Ticket Thread started");
            showProgress(true);
        }

        @Override
        protected List<ITicket> doInBackground(Void... params) {
            try {
                ICustomer customer = (ICustomer) UserWrapper.getInstance().getUser();

                List<IBooking> bookings = customer.getBookings();
                mTickets = new LinkedList<>();
                for (IBooking booking : bookings){
                    mTickets.add(booking.getTicket());
                }
            } catch (IOException e) {

            }

            return mTickets;
        }

        @Override
        protected void onPostExecute(List<ITicket> tickets) {

            if (mContext != null && isAdded()) {
                showProgress(false);
                if (tickets != null && !tickets.isEmpty()) {
                    ListView list = (ListView) mContext.findViewById(R.id.my_tickets_list);
                    list.setAdapter(new TicketFragAdapter(mContext, tickets));
                    setSwipe(list);
                }
            }

            Log.d(MainActivity.DEBUG_TAG, "onPostExecute: Ticket thread finished");
        }

        @Override
        protected void onCancelled() {
            Log.d(MainActivity.DEBUG_TAG, "onCancelled: Ticket Thread cancelled");
            showProgress(false);
        }
    }
}
