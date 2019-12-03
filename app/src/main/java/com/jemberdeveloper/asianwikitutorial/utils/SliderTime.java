package com.jemberdeveloper.asianwikitutorial.utils;

import android.app.Activity;
import android.content.Context;

import com.jemberdeveloper.asianwikitutorial.model.SliderModel;

import java.util.List;
import java.util.TimerTask;

import androidx.viewpager.widget.ViewPager;

public class SliderTime {

    public static class time extends TimerTask {

        private Context context;
        private ViewPager viewPager;
        private List<SliderModel> items;
        private boolean endSlide = false;

        public time(Context context, ViewPager viewPager, List<SliderModel> items) {
            this.context = context;
            this.viewPager = viewPager;
            this.items = items;
        }

        @Override
        public void run() {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!endSlide){
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        if (viewPager.getCurrentItem() == items.size() - 1){
                            endSlide = true;
                        }
                    } else {
                        viewPager.setCurrentItem(0);
                        endSlide = false;
                    }
                }
            });
        }
    }



}
