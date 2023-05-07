package com.basetechz.showbox.A_HomeFragment.Main_Activity.Inner_Fragments.A_ForYou;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
public class ImageSlider {

    private final ViewPager viewPager;
    private Timer slideTimer;
    private final Handler handler = new Handler();
    int nextItem = 0;
    public ImageSlider(ViewPager viewPager){
        this.viewPager=viewPager;
    }
    public void startSlideTimer() {
        slideTimer = new Timer();
        int SLIDE_INTERVAL = 6000;
        slideTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    int currentItem = viewPager.getCurrentItem();
                    int totalItems = Objects.requireNonNull(viewPager.getAdapter()).getCount();

                    if (totalItems != 0) {
                        nextItem = (currentItem + 1) % totalItems;
                    }
                    viewPager.setCurrentItem(nextItem);
                });
            }
        }, SLIDE_INTERVAL, SLIDE_INTERVAL);
    }

    public void stopSlideTimer() {
        if (slideTimer != null) {
            slideTimer.cancel();
            slideTimer = null;
        }
    }
}
