package com.example.aneurinc.prcs_app.UI.custom_listeners;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by aneurinc on 23/04/2016.
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    // Gesture detector
    private final GestureDetector gestureDetector;

    /*
    * Pass in parent activity context
    * Set listener to custom listener private class
    */
    public OnSwipeTouchListener(Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    /*
    * Return gesture detector
    */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    /*
    * Custom swipe detector class
    * Calculates swipe left or right
    */
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        // Swipe and velocity threshold
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        /*
        * Determine if swipe motion is left or right movement
        * Call correct helper method
        */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    /*
    * Override in parent activity
    */
    public void onSwipeRight() {
    }

    /*
    * Override in parent activity
    */
    public void onSwipeLeft() {
    }
}
